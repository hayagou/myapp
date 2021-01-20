package com.hayagou.myapp.controller;

import com.hayagou.myapp.model.dto.BoardDto;
import com.hayagou.myapp.model.dto.CartResponseDto;
import com.hayagou.myapp.model.response.CommonResult;
import com.hayagou.myapp.model.response.ListResult;
import com.hayagou.myapp.model.response.SingleResult;
import com.hayagou.myapp.service.CartService;
import com.hayagou.myapp.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"7. Cart"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    private final ResponseService responseService;
    private final CartService cartService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "장바구니 조회", notes = "장바구니을 조회한다.")
    @GetMapping
    public ListResult<CartResponseDto> carts(@ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getListResult(cartService.getCarts(email, page));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "장바구니 추가", notes = "장바구니에 상품을 추가한다.")
    @PostMapping("/{productId}")
    public SingleResult<Long> addCart(@Valid @PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getSingleResult(cartService.addCart(email, productId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "장바구니 상품 삭제", notes = "장바구니에서 상품을 삭제한다.")
    @DeleteMapping("/{cartId}")
    public CommonResult removeCart(@Valid @PathVariable Long cartId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        cartService.deleteCart(email, cartId);
        return responseService.getSuccessResult();
    }

}
