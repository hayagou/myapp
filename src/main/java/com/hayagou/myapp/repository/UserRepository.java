package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    boolean existsByEmail(String email);

}
