package com.hayagou.myapp.controller;

import com.google.common.net.HttpHeaders;
import com.hayagou.myapp.advice.exception.CProductNotFoundException;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.entity.File;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.entity.Product;
import com.hayagou.myapp.entity.ProductImage;
import com.hayagou.myapp.model.dto.FileResponseDto;
import com.hayagou.myapp.repository.FileRepository;
import com.hayagou.myapp.repository.PostRepository;
import com.hayagou.myapp.repository.ProductImageRepository;
import com.hayagou.myapp.repository.ProductRepository;
import com.hayagou.myapp.service.FileService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"9. ProductImageFile"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/product")
public class ProductImageController {
    private final FileService fileService;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{productId}/upload")
    public FileResponseDto uploadFile(@PathVariable Long productId, @RequestParam("file") MultipartFile multipartFile) {
        String fileName = fileService.storeFile(multipartFile);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/product")
                .path("/download/")
                .path(fileName)
                .toUriString();
        Product product = productRepository.findById(productId).orElseThrow(CProductNotFoundException::new);

        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .product(product)
                .build();

        productImageRepository.save(productImage);

        return new FileResponseDto(fileName, fileDownloadUri, multipartFile.getContentType(), multipartFile.getSize());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{productId}/uploadMultipleFiles")
    public List<FileResponseDto> uploadMultipleFiles(@PathVariable Long productId, @RequestParam("files") MultipartFile[] multipartFiles){
        return Arrays.asList(multipartFiles)
                .stream()
                .map(file -> uploadFile(productId, file))
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{productId}/files")
    public List<FileResponseDto> downloadFilesInfoInPost(@PathVariable Long productId){
        List<ProductImage> imageList = productImageRepository.findAllByProductId(productId);

        return imageList.stream().map(fileInfo -> FileResponseDto.builder().fileName(fileInfo.getFileName()).fileType(fileInfo.getFileType()).fileDownloadUri(fileInfo.getFileDownloadUri()).size(fileInfo.getSize()).build()).collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileService.loadFile(fileName);

        String contentType = null;

        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(contentType ==null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
