package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CNotOwnerException;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Cart;
import com.hayagou.myapp.entity.Product;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.CartResponseDto;
import com.hayagou.myapp.repository.CartRepository;
import com.hayagou.myapp.repository.ProductRepository;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    final private CartRepository cartRepository;
    final private UserRepository userRepository;
    final private ProductRepository productRepository;

    // 카트 추가
    public Long addCart(String email, Long productId) {
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(CResourceNotExistException::new);
        return cartRepository.save(Cart.builder().user(user).product(product).build()).getCartId();

    }

    // 카트 리스트
    public List<CartResponseDto> getCarts(String email, int page) {
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        Page<Cart> list = cartRepository.findAllByUser(user, PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<CartResponseDto> cartResponseDtoList = new ArrayList<>();
        for(Cart cart : list){
            cartResponseDtoList.add(CartResponseDto.builder().cartId(cart.getCartId()).productName(cart.getProduct().getName()).build());
        }
        return cartResponseDtoList;

    }

    // 카트 삭제
    public void deleteCart(String email, Long cartId){
        Cart cart = cartRepository.findById(cartId).orElseThrow(CResourceNotExistException::new);
        if(!email.equals(cart.getUser().getEmail())){
            throw new CNotOwnerException();
        }
        cartRepository.deleteById(cartId);
    }
}
