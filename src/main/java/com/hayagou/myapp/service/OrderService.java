package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Order;
import com.hayagou.myapp.entity.Product;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.repository.OrderRepository;
import com.hayagou.myapp.repository.ProductRepository;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void order(String email, Long productId){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(CResourceNotExistException::new);
        Order order = Order.builder().user(user).product(product).build();
        orderRepository.save(order);
    }
}
