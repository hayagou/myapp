package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CResourceNotExistException;

import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Product;


import com.hayagou.myapp.entity.ProductOrder;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.ProductOrderResponseDto;
import com.hayagou.myapp.repository.ProductOrderRepository;
import com.hayagou.myapp.repository.ProductRepository;
import com.hayagou.myapp.repository.UserRepository;
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
@Transactional
public class ProductOrderService {
    private final ProductOrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void order(String email, Long productId){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(CResourceNotExistException::new);
        ProductOrder productOrder = ProductOrder.builder().user(user).product(product).build();
        orderRepository.save(productOrder);
    }

    public List<ProductOrderResponseDto> getOrders(int page){
        Page<ProductOrder> list = orderRepository.findAll(PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<ProductOrderResponseDto> orderList = new ArrayList<>();
        for (ProductOrder productOrder: list) {
            orderList.add(ProductOrderResponseDto.builder().orderId(productOrder.getOrderId()).createAt(productOrder.getCreatedAt()).price(productOrder.getProduct().getPrice()).productName(productOrder.getProduct().getName()).createAt(productOrder.getCreatedAt()).build());
        }
        return orderList;
    }

    public List<ProductOrderResponseDto> getMyOrder(String email , int page){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);

        Page<ProductOrder> list = orderRepository.findAllByUser(user, PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<ProductOrderResponseDto> orderList = new ArrayList<>();
        for (ProductOrder productOrder: list) {
            orderList.add(ProductOrderResponseDto.builder().orderId(productOrder.getOrderId()).userName(productOrder.getUser().getName()).createAt(productOrder.getCreatedAt()).price(productOrder.getProduct().getPrice()).productName(productOrder.getProduct().getName()).createAt(productOrder.getCreatedAt()).build());
        }
        return orderList;
    }

}
