package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CNotOwnerException;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Cart;
import com.hayagou.myapp.entity.Product;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.CartDeleteDto;
import com.hayagou.myapp.model.dto.CartResponseDto;
import com.hayagou.myapp.model.dto.PaginationDto;
import com.hayagou.myapp.repository.CartRepository;
import com.hayagou.myapp.repository.ProductRepository;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    final private CartRepository cartRepository;
    final private UserRepository userRepository;
    final private ProductRepository productRepository;

    // 카트 추가
    public CartResponseDto addCart(String email, Long productId) {
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(CResourceNotExistException::new);
        Cart cart = cartRepository.save(Cart.builder().user(user).product(product).build());
        return CartResponseDto.builder().cartId(cart.getCartId()).productName(cart.getProduct().getName()).build();

    }

    // 카트 리스트
    public PaginationDto getCarts(String email, int page, int size) {
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);

        int totalCount = (int) cartRepository.countByUser(user);

        // 데이터가 존재 하지 않을 경우
        if(totalCount == 0){
            throw new CResourceNotExistException();
        }

        int totalPage = totalCount / size;

        if (totalCount % size > 0) {
            totalPage++;
        }

        if (totalPage < page) {
            page = totalPage;
        }

        Page<Cart> list = cartRepository.findAllByUser(user, PageRequest.of(page-1, size,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<CartResponseDto> cartResponseDtoList = new ArrayList<>();
        for(Cart cart : list){
            cartResponseDtoList.add(CartResponseDto.builder().cartId(cart.getCartId()).productName(cart.getProduct().getName()).build());
        }

        PaginationDto paginationDto = PaginationDto.builder().totalPage(totalPage).totalCount(totalCount).currentPage(page).items(Collections.singletonList(cartResponseDtoList)).build();
        return paginationDto;

    }

    // 카트 삭제
    public CartDeleteDto deleteCart(String email, Long cartId){
        Cart cart = cartRepository.findById(cartId).orElseThrow(CResourceNotExistException::new);
        if(!email.equals(cart.getUser().getEmail())){
            throw new CNotOwnerException();
        }
        CartDeleteDto cartDeleteDto = (CartDeleteDto) CartResponseDto.builder().cartId(cart.getCartId()).productName(cart.getProduct().getName()).build();
        cartRepository.deleteById(cartId);
        cartDeleteDto.setDeleted(true);
        return cartDeleteDto;
    }
}
