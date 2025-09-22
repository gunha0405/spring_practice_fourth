package com.example.answer.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.answer.model.Answer;
import com.example.answer.repository.AnswerRepository;
import com.example.exception.BusinessException;
import com.example.exception.DataNotFoundException;
import com.example.question.model.Question;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Page<Answer> getAnswersByQuestion(Question question, int page, String sort) {
        Pageable pageable;
        if ("vote".equals(sort)) {
            pageable = PageRequest.of(page, 5);
            return answerRepository.findByQuestionOrderByVoteCount(question, pageable);
        } else {
            pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("createDate")));
            return answerRepository.findByQuestion(question, pageable);
        }
    }

    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        return this.answerRepository.save(answer);
    }

    public Answer getAnswer(Long id) {
        return this.answerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("error.answer.notFound"));
    }

    public void modify(Answer answer, String content, SiteUser currentUser) {
        if (!answer.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new BusinessException("error.noPermission");
        }
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer, SiteUser currentUser) {
        if (!answer.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new BusinessException("error.noPermission");
        }
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}
