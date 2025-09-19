package com.example.question.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.question.model.Question;
import com.example.question.respository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	
	public List<Question> getList() {
		return this.questionRepository.findAll();
	}
}
