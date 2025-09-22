package com.example.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.exception.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	
	private CategoryService categoryService;
	
	private final CategoryRepository categoryRepository;

    public Category getCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
    }

    public List<Category> getAllCategories() {
    	List<Category> categories = categoryRepository.findAll();
    	return categories;
    }

	public Category getCategoryById(Long id) {
		Optional<Category> c = categoryRepository.findById(id);
		if(c.isPresent()) {
			return c.get();
		}else {
			throw new DataNotFoundException("category not found");
		}
		
	}
	
}
