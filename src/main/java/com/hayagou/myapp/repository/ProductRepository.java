package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.Category;
import com.hayagou.myapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category , Pageable pageable);
//    비슷한상품이름 검색 고쳐야할부분
//    Page<Product> findProductsByNameLike(String keyword, Pageable pageable);
}
