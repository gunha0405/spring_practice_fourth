package com.example.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user.model.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long>{
	Optional<SiteUser> findByusername(String username);
	Optional<SiteUser> findByEmail(String email);
}
