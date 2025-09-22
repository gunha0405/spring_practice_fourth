package com.example.question.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.category.Category;
import com.example.category.CategoryRepository;
import com.example.exception.DataNotFoundException;
import com.example.question.model.Question;
import com.example.question.respository.QuestionRepository;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    public Page<Question> getList(int page, String kw, String filter) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));

        if ("answer".equals(filter)) {
            return questionRepository.findQuestionsOrderByLatestAnswer(pageable);
        } else if ("comment".equals(filter)) {
            return questionRepository.findQuestionsOrderByLatestComment(pageable);
        } else {
            return questionRepository.findByKeyword(kw, pageable);
        }
    }

    public Question getQuestion(Long id) {
        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("error.question.notFound"));
        question.setViewCount(question.getViewCount() + 1);
        return question;
    }

    public void create(String subject, String content, Long categoryId, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(user);
        q.setCreateDate(LocalDateTime.now());

        if (categoryId != null) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            q.setCategory(category.orElse(null));
        }

        this.questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content, SiteUser currentUser) {
        if (!question.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new com.example.exception.BusinessException("error.noPermission");
        }
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question, SiteUser currentUser) {
        if (!question.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new com.example.exception.BusinessException("error.noPermission");
        }
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}
