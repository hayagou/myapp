package com.hayagou.myapp.repository;


import com.hayagou.myapp.entity.Cart;
import com.hayagou.myapp.entity.Category;
import com.hayagou.myapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findAllByUser(User user, Pageable pageable);
    int countByUser(User user);
}
