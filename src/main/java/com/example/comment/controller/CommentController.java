package com.example.comment.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.answer.service.AnswerService;
import com.example.comment.model.Comment;
import com.example.comment.model.dto.CommentForm;
import com.example.comment.service.CommentService;
import com.example.exception.BusinessException;
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
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        SiteUser user = this.userService.getUser(principal.getName());
        Comment c;
        if ("question".equals(type)) {
            c = this.commentService.create(this.questionService.getQuestion(id), user, commentForm.getContent());
        } else if ("answer".equals(type)) {
            c = this.commentService.create(this.answerService.getAnswer(id), user, commentForm.getContent());
        } else {
            throw new BusinessException("error.comment.unknownType");
        }

        return String.format("redirect:/question/detail/%s", c.getQuestionId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyCommentForm(CommentForm commentForm,
                                    @PathVariable("id") Long id,
                                    Principal principal) {
        Comment c = this.commentService.getComment(id);
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

        SiteUser user = this.userService.getUser(principal.getName());
        Comment c = this.commentService.getComment(id);
        this.commentService.modify(c, commentForm.getContent(), user);

        return String.format("redirect:/question/detail/%s", c.getQuestionId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Long id) {
        SiteUser user = this.userService.getUser(principal.getName());
        Comment c = this.commentService.getComment(id);
        this.commentService.delete(c, user);

        return String.format("redirect:/question/detail/%s", c.getQuestionId());
    }
}
