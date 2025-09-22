package com.example.comment.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.answer.model.Answer;
import com.example.comment.model.Comment;
import com.example.comment.repository.CommentRepository;
import com.example.exception.BusinessException;
import com.example.exception.DataNotFoundException;
import com.example.question.model.Question;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	
    private final CommentRepository commentRepository;

    public Comment create(Question question, SiteUser author, String content) {
        Comment c = new Comment();
        c.setContent(content);
        c.setCreateDate(LocalDateTime.now());
        c.setQuestion(question);
        c.setAuthor(author);
        return this.commentRepository.save(c);
    }

    public Comment create(Answer answer, SiteUser author, String content) {
        Comment c = new Comment();
        c.setContent(content);
        c.setCreateDate(LocalDateTime.now());
        c.setAnswer(answer);
        c.setAuthor(author);
        return this.commentRepository.save(c);
    }
    
    public Comment getComment(Long id) {
        return this.commentRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("error.comment.notFound"));
    }

    public Comment modify(Comment c, String content, SiteUser currentUser) {
        if (!c.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new BusinessException("error.noPermission");
        }
        c.setContent(content);
        c.setModifyDate(LocalDateTime.now());
        return this.commentRepository.save(c);
    }

    public void delete(Comment c, SiteUser currentUser) {
        if (!c.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new BusinessException("error.noPermission");
        }
        this.commentRepository.delete(c);
    }
}
