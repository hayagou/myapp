package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.entity.Category;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageable);
//    비슷한상품이름 검색 고쳐야할부분
    @Query(
        value = "SELECT p FROM Product p where :#{#category.categoryId} = p.category.categoryId and p.name like %:keyword% or p.desc like %:keyword% ",
        countQuery = "SELECT count(p.productId) FROM Product p, Category c where :#{#category.categoryId} = p.category.categoryId and p.desc like %:keyword% "
    )
    Page<Product> findAllSearch(Category category, String keyword, Pageable pageable);

    int countAllByCategory(Category category);

    @Query(value = "SELECT count(p.productId) FROM Product p, Category c where :#{#category.categoryId} = p.category.categoryId and p.name like %:keyword% or p.desc like  %:keyword%")
    int countAllBySearch(Category category, String keyword);
}
