package com.example.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
