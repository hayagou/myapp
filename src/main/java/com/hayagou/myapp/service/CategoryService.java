package com.hayagou.myapp.service;

import com.hayagou.myapp.entity.Category;
import com.hayagou.myapp.model.dto.CategoryDto;
import com.hayagou.myapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryList(int page){
        Page<Category> list = categoryRepository.findAll(PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<CategoryDto> categoryList = new ArrayList<>();
        for (Category category: list) {
            categoryList.add(CategoryDto.builder().categoryName(category.getName()).build());
        }
        return categoryList;
    }
    @Transactional
    public Long createCategory(CategoryDto categoryDto){
        Category category = categoryRepository.save(Category.builder().name(categoryDto.getCategoryName()).build());
        return category.getCategoryId();
    }

    @Transactional
    public void deleteCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findByName(categoryDto.getCategoryName());
        categoryRepository.delete(category);
    }

}
