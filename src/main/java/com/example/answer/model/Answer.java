package com.example.answer.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.comment.model.Comment;
import com.example.question.model.Question;
import com.example.user.model.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate; 
    
    private LocalDateTime modifyDate;
    
    @ManyToOne
    private Question question;  
    
    @ManyToOne
    private SiteUser author;
    
    @ManyToMany
    Set<SiteUser> voter;
    
    @OneToMany(mappedBy = "answer")
    private List<Comment> commentList;
}