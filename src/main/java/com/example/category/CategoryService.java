package com.example.category;

import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.example.exception.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {	
	
	private final CategoryRepository categoryRepository;
	
	private final MessageSource messageSource;

    public Category getCategory(String name) {
    	String msg = messageSource.getMessage("error.category.notFound", null, LocaleContextHolder.getLocale());
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException(msg));
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
			String msg = messageSource.getMessage("error.category.notFound", null, LocaleContextHolder.getLocale());
			throw new DataNotFoundException(msg);
		}
		
	}
	
}
