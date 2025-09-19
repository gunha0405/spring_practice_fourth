package com.example.answer.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.answer.model.Answer;
import com.example.answer.repository.AnswerRepository;
import com.example.question.model.Question;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerRepository answerRepository;


    public void create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
    }
}
