package com.example.comment.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.answer.model.Answer;
import com.example.answer.service.AnswerService;
import com.example.comment.model.Comment;
import com.example.comment.model.dto.CommentForm;
import com.example.comment.service.CommentService;
import com.example.question.model.Question;
import com.example.question.service.QuestionService;
import com.example.user.model.SiteUser;
import com.example.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/question/{id}")
    public String createQuestionComment(CommentForm commentForm, Model model,
                                        @PathVariable("id") Long id) {
        model.addAttribute("actionUrl", "/comment/create/question/" + id);
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/answer/{id}")
    public String createAnswerComment(CommentForm commentForm, Model model,
                                      @PathVariable("id") Long id) {
        model.addAttribute("actionUrl", "/comment/create/answer/" + id);
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{type}/{id}")
    public String createComment(@PathVariable("type") String type,
                                @PathVariable("id") Long id,
                                @Valid CommentForm commentForm,
                                BindingResult bindingResult,
                                Principal principal) {

        SiteUser user = Optional.ofNullable(this.userService.getUser(principal.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment c;
        if ("question".equals(type)) {
            Question question = Optional.ofNullable(this.questionService.getQuestion(id))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "question not found"));
            c = this.commentService.create(question, user, commentForm.getContent());
        } else if ("answer".equals(type)) {
            Answer answer = Optional.ofNullable(this.answerService.getAnswer(id))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "answer not found"));
            c = this.commentService.create(answer, user, commentForm.getContent());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown type");
        }

        return String.format("redirect:/question/detail/%s", c.getQuestionId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyComment(CommentForm commentForm, @PathVariable("id") Long id,
                                Principal principal) {
        Comment c = this.commentService.getComment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (!c.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentForm.setContent(c.getContent());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm,
                                BindingResult bindingResult,
                                Principal principal,
                                @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment c = this.commentService.getComment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (!c.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        c = this.commentService.modify(c, commentForm.getContent());
        return String.format("redirect:/question/detail/%s", c.getQuestionId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Long id) {
        Comment c = this.commentService.getComment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (!c.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.commentService.delete(c);
        return String.format("redirect:/question/detail/%s", c.getQuestionId());
    }
}
