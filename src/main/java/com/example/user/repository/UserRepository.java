package com.example.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user.model.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long>{

}
