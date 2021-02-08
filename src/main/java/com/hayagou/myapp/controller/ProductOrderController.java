//package com.hayagou.myapp.controller;
//
//import com.hayagou.myapp.model.dto.ProductOrderResponseDto;
//import com.hayagou.myapp.model.response.ListResponse;
//import com.hayagou.myapp.service.ProductOrderService;
//import com.hayagou.myapp.service.ResponseService;
//import io.swagger.annotations.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@Api(tags = {"5. Order"})
//@RequiredArgsConstructor
//@RestController
//@RequestMapping(value = "/order")
//public class ProductOrderController {
//    private final ProductOrderService productOrderService;
//    private final ResponseService responseService;
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "상품 주문", notes = "상품을 주문한다.")
//    @PostMapping("/{productId}")
//    public CommonResponse order(@Valid @PathVariable Long productId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        productOrderService.order(email, productId);
//        return responseService.getSuccessResult();
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "상품 주문 조회", notes = "상품 주문 내역 조회.")
//    @GetMapping()
//    public ListResponse<ProductOrderResponseDto> order(@ApiParam(value = "페이지", defaultValue = "1") @RequestParam int page) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return responseService.getListResult(productOrderService.getMyOrder(email, page));
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "모든 상품 주문 조회(관리자 기능)", notes = "상품 주문 내역 조회.")
//    @GetMapping("/admin")
//    public ListResponse<ProductOrderResponseDto> getAmdinOrder(@ApiParam(value = "페이지", defaultValue = "1") @RequestParam int page) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return responseService.getListResult(productOrderService.getOrders(page));
//    }
//
//
//}
