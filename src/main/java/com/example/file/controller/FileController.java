package com.example.file.controller;


import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.file.model.FileMetaData;
import com.example.file.service.FileService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    // PDF 뷰어 (inline)
    @GetMapping("/view/{id}")
    public void viewFile(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        FileMetaData fileMetaData = fileService.getFile(id);

        File file = new File(fileMetaData.getFilePath());
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/pdf"); // PDF 뷰어
        response.setHeader("Content-Disposition", "inline; filename=\"" + 
                           URLEncoder.encode(fileMetaData.getOriginalName(), "UTF-8") + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    // 다운로드
    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        FileMetaData fileMetaData = fileService.getFile(id);

        File file = new File(fileMetaData.getFilePath());
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" +
                           URLEncoder.encode(fileMetaData.getOriginalName(), "UTF-8") + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
