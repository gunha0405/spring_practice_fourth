package com.example.file.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.file.FileUtil;
import com.example.file.model.FileMetaData;
import com.example.file.repository.FileMetaDataRepository;
import com.example.question.model.Question;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileMetaDataRepository fileRepo;

    public FileMetaData saveFile(MultipartFile file, String folder, Question question) throws IOException {
        String savedName = FileUtil.saveFile(file, folder);

        FileMetaData meta = new FileMetaData();
        meta.setOriginalName(file.getOriginalFilename());
        meta.setSavedName(savedName);
        meta.setFilePath(FileUtil.getUploadRoot() + File.separator + folder + File.separator + savedName);
        meta.setQuestion(question);

        return fileRepo.save(meta);
    }

    public List<FileMetaData> saveFiles(List<MultipartFile> files, String folder, Question question) throws IOException {
        List<FileMetaData> result = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                result.add(saveFile(file, folder, question));
            }
        }
        return result;
    }
    
    public FileMetaData getFile(Long id) {
        return fileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }
    
}
