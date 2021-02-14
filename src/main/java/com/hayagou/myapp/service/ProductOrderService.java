package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CResourceNotExistException;

import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Product;


import com.hayagou.myapp.entity.ProductOrder;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.PaginationDto;
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
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductOrderService {
    private final ProductOrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ProductOrderResponseDto order(String email, Long productId){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(CResourceNotExistException::new);
        ProductOrder productOrder = ProductOrder.builder().user(user).product(product).build();
        orderRepository.save(productOrder);

        return ProductOrderResponseDto.builder().orderId(productOrder.getOrderId()).productName(productOrder.getProduct().getName())
                .createAt(productOrder.getCreatedAt())
                .price(productOrder.getProduct().getPrice())
                .userName(productOrder.getUser().getName())
                .build();
    }

    public PaginationDto getOrders(int page, int size){

        int totalCount = (int) orderRepository.count();

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

        Page<ProductOrder> list = orderRepository.findAll(PageRequest.of(page-1, size,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<ProductOrderResponseDto> orderList = new ArrayList<>();
        for (ProductOrder productOrder: list) {
            orderList.add(ProductOrderResponseDto.builder().orderId(productOrder.getOrderId()).createAt(productOrder.getCreatedAt()).price(productOrder.getProduct().getPrice()).productName(productOrder.getProduct().getName()).createAt(productOrder.getCreatedAt()).build());
        }
        PaginationDto paginationDto = PaginationDto.builder().totalPage(totalPage).currentPage(page).totalCount(totalCount).items(Collections.singletonList(orderList)).build();
        return paginationDto;
    }

    public PaginationDto getMyOrder(String email , int page, int size){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);

        int totalCount = (int) orderRepository.countByUser(user);

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



        Page<ProductOrder> list = orderRepository.findAllByUser(user, PageRequest.of(page-1, size,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<ProductOrderResponseDto> orderList = new ArrayList<>();
        for (ProductOrder productOrder: list) {
            orderList.add(ProductOrderResponseDto.builder().orderId(productOrder.getOrderId()).userName(productOrder.getUser().getName()).createAt(productOrder.getCreatedAt()).price(productOrder.getProduct().getPrice()).productName(productOrder.getProduct().getName()).createAt(productOrder.getCreatedAt()).build());
        }

        PaginationDto paginationDto = PaginationDto.builder().totalPage(totalPage).currentPage(page).totalCount(totalCount).items(Collections.singletonList(orderList)).build();
        return paginationDto;
    }

}
