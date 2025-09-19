package com.example.question.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.question.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{
	Question findBySubject(String subject);
	Question findBySubjectAndContent(String subject, String content);
	List<Question> findBySubjectLike(String subject);
}
