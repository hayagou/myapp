package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.entity.Category;
import com.hayagou.myapp.model.dto.CategoryDeleteDto;
import com.hayagou.myapp.model.dto.CategoryRequestDto;
import com.hayagou.myapp.model.dto.CategoryResponseDto;
import com.hayagou.myapp.model.dto.PaginationDto;
import com.hayagou.myapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public PaginationDto getCategoryList(int page, int size) {

        int totalCount = (int) categoryRepository.count();

        // 데이터가 존재 하지 않을 경우
        if (totalCount == 0) {
            throw new CResourceNotExistException();
        }

        int totalPage = totalCount / size;

        if (totalCount % size > 0) {
            totalPage++;
        }

        if (totalPage < page) {
            page = totalPage;
        }
        Page<Category> list = categoryRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<CategoryRequestDto> categoryList = new ArrayList<>();
        for (Category category : list) {
            categoryList.add(CategoryRequestDto.builder().categoryName(category.getName()).build());
        }

        PaginationDto paginationDto = PaginationDto.builder().items(Collections.singletonList(categoryList)).currentPage(page).totalPage(totalPage)
                .totalCount(totalCount).build();

        return paginationDto;
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.save(Category.builder().name(categoryRequestDto.getCategoryName()).build());
//        return category.getCategoryId();

        return CategoryResponseDto.builder().categoryId(category.getCategoryId())
                .categoryName(category.getName())
                .createdAt(category.getCreatedAt())
                .modifiedAt(category.getModifiedAt())
                .build();

    }

    @Transactional
    public CategoryDeleteDto deleteCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findByName(categoryRequestDto.getCategoryName());
        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder().categoryId(category.getCategoryId())
                .categoryName(category.getName())
                .createdAt(category.getCreatedAt())
                .modifiedAt(category.getModifiedAt())
                .build();
        categoryRepository.delete(category);
        CategoryDeleteDto categoryDeleteDto = (CategoryDeleteDto) categoryResponseDto;
        categoryDeleteDto.setDeleted(true);
        return categoryDeleteDto;
    }

}
