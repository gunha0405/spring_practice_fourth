package com.example.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	Page<Comment> findByAuthorUsername(String username, Pageable pageable);
}
