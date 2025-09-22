package com.example.answer.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.answer.model.Answer;
import com.example.answer.repository.AnswerRepository;
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
        this.answerRepository.save(answer);
        return answer;
    }
    
    public Answer getAnswer(Long id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
    
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
    
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
    
}
