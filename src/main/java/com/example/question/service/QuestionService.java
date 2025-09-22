package com.example.question.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.category.Category;
import com.example.category.CategoryRepository;
import com.example.exception.DataNotFoundException;
import com.example.file.model.FileMetaData;
import com.example.file.service.FileService;
import com.example.question.model.Question;
import com.example.question.respository.QuestionRepository;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public Page<Question> getList(int page, String kw, String filter) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));

        if ("answer".equals(filter)) {
            return questionRepository.findQuestionsOrderByLatestAnswer(pageable);
        } else if ("comment".equals(filter)) {
            return questionRepository.findQuestionsOrderByLatestComment(pageable);
        } else {
            return questionRepository.findByKeyword(kw, pageable);
        }
    }

    public Question getQuestion(Long id) {
        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("error.question.notFound"));
        question.setViewCount(question.getViewCount() + 1);
        return question;
    }

    public void create(String subject, String content, Long categoryId, List<MultipartFile> files, SiteUser user) throws IOException {
    	Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(user);
        q.setViewCount(0);
        q.setCreateDate(LocalDateTime.now());
        if (categoryId != null) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            q.setCategory(category.orElse(null));
        }
        Question savedQ = questionRepository.save(q);
        
        List<FileMetaData> fileList = fileService.saveFiles(files, "question", savedQ);
        savedQ.getFiles().addAll(fileList);
        
    }

    public void modify(Question question, String subject, String content, SiteUser currentUser) {
        if (!question.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new com.example.exception.BusinessException("error.noPermission");
        }
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question, SiteUser currentUser) {
        if (!question.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new com.example.exception.BusinessException("error.noPermission");
        }
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}
