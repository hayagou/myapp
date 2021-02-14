package com.hayagou.myapp.controller;

import com.hayagou.myapp.model.dto.CartResponseDto;
import com.hayagou.myapp.model.dto.PaginationDto;
import com.hayagou.myapp.model.response.ListResponse;
import com.hayagou.myapp.model.response.DataResponse;
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
    public DataResponse<PaginationDto> carts(@ApiParam(value = "page", defaultValue = "1")@RequestParam int page,
                                             @ApiParam(value = "size", defaultValue = "10")@RequestParam int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getResponse(cartService.getCarts(email, page, size));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "장바구니 추가", notes = "장바구니에 상품을 추가한다.")
    @PostMapping("/{productId}")
    public DataResponse addCart(@Valid @PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getResponse(cartService.addCart(email, productId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "장바구니 상품 삭제", notes = "장바구니에서 상품을 삭제한다.")
    @DeleteMapping("/{cartId}")
    public DataResponse removeCart(@Valid @PathVariable Long cartId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return responseService.getResponse(cartService.deleteCart(email, cartId));
    }

}
