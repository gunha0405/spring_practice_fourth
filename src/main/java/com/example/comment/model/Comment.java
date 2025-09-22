package com.example.comment.model;

import java.time.LocalDateTime;

import com.example.answer.model.Answer;
import com.example.question.model.Question;
import com.example.user.model.SiteUser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;
	
	@ManyToOne
	private SiteUser author;
	
	@ManyToOne
	private Question question;
	
	@ManyToOne
	private Answer answer;
	
	public Long getQuestionId() {
        Long result = null;
        if (this.question != null) {
            result = this.question.getId();
        } else if (this.answer != null) {
            result = this.answer.getQuestion().getId();
        }
        return result;
    }

	
}
