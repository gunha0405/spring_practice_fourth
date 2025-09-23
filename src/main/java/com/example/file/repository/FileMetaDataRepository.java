package com.example.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.file.model.FileMetaData;

public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long>{

}
