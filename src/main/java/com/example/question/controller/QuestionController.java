package com.example.question.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.answer.model.Answer;
import com.example.answer.model.dto.AnswerForm;
import com.example.answer.service.AnswerService;
import com.example.category.Category;
import com.example.category.CategoryService;
import com.example.exception.BusinessException;
import com.example.question.model.Question;
import com.example.question.model.dto.QuestionForm;
import com.example.question.service.QuestionService;
import com.example.user.model.SiteUser;
import com.example.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final AnswerService answerService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "filter", defaultValue = "") String filter) {
        Page<Question> paging = this.questionService.getList(page, kw, filter);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model,
                         @PathVariable("id") Long id,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "sort", defaultValue = "new") String sort) {
        Question question = this.questionService.getQuestion(id);
        Page<Answer> paging = this.answerService.getAnswersByQuestion(question, page, sort);

        model.addAttribute("question", question);
        model.addAttribute("answerPaging", paging);
        model.addAttribute("answerForm", new AnswerForm());
        model.addAttribute("sort", sort);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm, Model model) {
        List<Category> categories = this.categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 @RequestParam("files") List<MultipartFile> files, 
                                 Model model) throws IOException {
    	try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("categories", categoryService.getAllCategories());
                return "question_form";
            }

            SiteUser siteUser = this.userService.getUser(principal.getName());
            if (siteUser == null) {
                throw new BusinessException("error.user.notFound");
            }

            this.questionService.create(
                questionForm.getSubject(),
                questionForm.getContent(),
                questionForm.getCategoryId(),
                files,
                siteUser
            );
            return "redirect:/question/list";
        } catch (Exception e) {
            e.printStackTrace(); // 무슨 예외인지 먼저 확인
            throw e;
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm,
                                 @PathVariable("id") Long id,
                                 Principal principal) {
        Question question = this.questionService.getQuestion(id);
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Question question = this.questionService.getQuestion(id);
        SiteUser currentUser = this.userService.getUser(principal.getName());
        this.questionService.modify(question,
                                    questionForm.getSubject(),
                                    questionForm.getContent(),
                                    currentUser);

        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser currentUser = this.userService.getUser(principal.getName());
        this.questionService.delete(question, currentUser);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
