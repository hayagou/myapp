package com.hayagou.myapp.repository;


import com.hayagou.myapp.entity.ProductOrder;
import com.hayagou.myapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    Page<ProductOrder> findAll(Pageable pageable);
    Page<ProductOrder> findAllByUser(User usesr, Pageable pageable);
    int countByUser(User user);
}
