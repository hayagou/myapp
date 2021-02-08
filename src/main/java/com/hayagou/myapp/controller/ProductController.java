//package com.hayagou.myapp.controller;
//
//import com.hayagou.myapp.model.dto.*;
//import com.hayagou.myapp.model.response.ListResponse;
//import com.hayagou.myapp.model.response.DataResponse;
//import com.hayagou.myapp.service.CategoryService;
//import com.hayagou.myapp.service.ProductService;
//import com.hayagou.myapp.service.ResponseService;
//import io.swagger.annotations.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@Api(tags = {"4. Product"})
//@RestController
//@RequiredArgsConstructor
//public class ProductController {
//    private final ResponseService responseService;
//    private final CategoryService categoryService;
//    private final ProductService productService;
//
//    @ApiOperation(value = "카테고리 목록 조회", notes = "카테고리 목록을 조회한다.")
//    @GetMapping("/category")
//    public ListResponse<CategoryDto> category(@ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
//        return responseService.getListResult(categoryService.getCategoryList(page));
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "카테고리 등록", notes = "카테고리를 등록한다.")
//    @PostMapping("/category")
//    public DataResponse<Long> category(@Valid @ModelAttribute CategoryDto categoryDto) {
//        return responseService.getResponse(categoryService.createCategory(categoryDto));
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "카테고리 삭제", notes = "카테고리를 삭제한다.")
//    @DeleteMapping("/category")
//    public CommonResponse deleteBoard(@Valid @ModelAttribute CategoryDto categoryDto) {
//        categoryService.deleteCategory(categoryDto);
//        return responseService.getSuccessResult();
//    }
//
////    @ApiOperation(value = "게시판 글 검색 리스트", notes = "게시판 게시글 검색 리스트를 조회한다.")
////    @GetMapping(value = "/{boardName}/posts/{keyword}")
////    public ListResult<PostListDto> posts(@PathVariable String boardName, @PathVariable String keyword, @ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
////        return responseService.getListResult(postService.getSearchPosts(boardName, keyword, page));
////    }
////
////
//    @ApiOperation(value = "상품 리스트", notes = "상품 리스트를 조회한다.")
//    @GetMapping(value = "/{category}/products")
//    public ListResponse<ProductResponseDto> products(@PathVariable String category, @ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
//        return responseService.getListResult(productService.getProducts(category, page));
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "상품 등록", notes = "상품을 등록한다.")
//    @PostMapping(value = "/product")
//    public DataResponse<Long> product(@ModelAttribute ProductRequestDto productRequestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return responseService.getResponse(productService.registrationProduct(email, productRequestDto));
//    }
//
//    @ApiOperation(value = "상품 상세", notes = "상품 상세정보를 조회한다.")
//    @GetMapping(value = "/product/{productId}")
//    public DataResponse<ProductResponseDto> proudct(@PathVariable long productId) {
//        return responseService.getResponse(productService.getProduct(productId));
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "상품 수정", notes = "상품 정보를 수정한다.")
//    @PutMapping(value = "/product/{productId}")
//    public DataResponse<ProductResponseDto> post(@PathVariable long productId, @Valid @ModelAttribute ProductRequestDto productRequestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return responseService.getResponse(productService.updateProduct(email, productId, productRequestDto));
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "상품 삭제", notes = "상품을 삭제한다.")
//    @DeleteMapping(value = "/product/{productId}")
//    public CommonResponse deletePost(@PathVariable long productId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        productService.delProduct(email, productId);
//        return responseService.getSuccessResult();
//    }
//}
