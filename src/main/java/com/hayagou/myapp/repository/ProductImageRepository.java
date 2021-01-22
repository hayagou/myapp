package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("select p from ProductImage p where p.product.productId = :id")
    List<ProductImage> findAllByProductId(Long id);
}
