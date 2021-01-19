package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CNotOwnerException;
import com.hayagou.myapp.advice.exception.CProductNotFoundException;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Category;
import com.hayagou.myapp.entity.Product;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.ProductRequestDto;
import com.hayagou.myapp.model.dto.ProductResponseDto;
import com.hayagou.myapp.repository.CategoryRepository;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category findCategory(String categoryName) {
        return Optional.ofNullable(categoryRepository.findByName(categoryName)).orElseThrow(CResourceNotExistException::new);
    }


//    상품등록
    @Transactional
    public Long registrationProduct(String email, ProductRequestDto productRequestDto){
        Category category = findCategory(productRequestDto.getCategoryName());
        Product product = Product.builder().category(category).user(userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new)).name(productRequestDto.getName()).price(productRequestDto.getPrice()).build();
        return productRepository.save(product).getProductId();
    }

//    상품 단건 조회
    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(CProductNotFoundException::new);
        ProductResponseDto productResponseDto = ProductResponseDto.builder().categoryName(product.getCategory().getName()).productId(product.getProductId()).name(product.getName()).price(product.getPrice()).build();
        return productResponseDto;
    }

//    상품조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts(String categoryName, int page){
        Category category = findCategory(categoryName);
        Page<Product> list = productRepository.findByCategory(category , PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<ProductResponseDto> productList = new ArrayList<>();
        for (Product product: list) {
            productList.add(ProductResponseDto.builder().productId(product.getProductId()).categoryName(product.getCategory().getName()).price(product.getPrice()).name(product.getName()).build());
        }
        return productList;
    }

//    상품삭제
    @Transactional
    public boolean delProduct(String email, Long productId){
        Product product = productRepository.findById(productId).orElseThrow(CResourceNotExistException::new);
        User user = product.getUser();
        if(!email.equals(user.getEmail())){
            throw new CNotOwnerException();
        }
        productRepository.deleteById(productId);
        return true;

    }

//    상품 수정
    @Transactional
    public ProductResponseDto updateProduct(String email, Long productId, ProductRequestDto productRequestDto){
        Product product = productRepository.findById(productId).orElseThrow(CResourceNotExistException::new);
        User user = product.getUser();
        if(!email.equals(user.getEmail())){
            throw new CNotOwnerException();
        }
        product.updateProduct(productRequestDto.getName(), productRequestDto.getPrice());
        return getProduct(productId);
    }
}
