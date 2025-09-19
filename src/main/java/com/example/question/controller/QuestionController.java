package com.example.question.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.question.model.Question;
import com.example.question.respository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionRepository questionRepository;
	
	@GetMapping("/list")
	public String list(Model model) {
		List<Question> questionList = this.questionRepository.findAll();
		model.addAttribute("questionList", questionList);
		return "question_list";
	}
	
}
