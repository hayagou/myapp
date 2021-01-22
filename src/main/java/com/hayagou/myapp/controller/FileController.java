package com.hayagou.myapp.controller;

import com.google.common.net.HttpHeaders;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.entity.File;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.model.dto.FileResponseDto;
import com.hayagou.myapp.repository.FileRepository;
import com.hayagou.myapp.repository.PostRepository;
import com.hayagou.myapp.service.BoardService;
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

@Api(tags = {"8. file"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/file")
public class FileController {

    private final FileService fileService;
    private final FileRepository fileRepository;
    private final PostRepository postRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{postId}/upload")
    public FileResponseDto uploadFile(@PathVariable Long postId, @RequestParam("file") MultipartFile multipartFile) {
        String fileName = fileService.storeFile(multipartFile);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file")
                .path("/download/")
                .path(fileName)
                .toUriString();

        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);

        File file = File.builder()
                .fileName(fileName)
                .uri(fileDownloadUri)
                .fileType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .post(post)
                .build();

        fileRepository.save(file);

        return new FileResponseDto(fileName, fileDownloadUri, multipartFile.getContentType(), multipartFile.getSize());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{postId}/uploadMultipleFiles")
    public List<FileResponseDto> uploadMultipleFiles(@PathVariable Long postId, @RequestParam("files") MultipartFile[] multipartFiles){
        return Arrays.asList(multipartFiles)
                .stream()
                .map(file -> uploadFile(postId, file))
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{postId}/files")
    public List<FileResponseDto> downloadFilesInfoInPost(@PathVariable Long postId){
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        List<File> fileList = fileRepository.findAllByPost(post);

        return fileList.stream().map(fileInfo -> FileResponseDto.builder().fileName(fileInfo.getFileName()).fileType(fileInfo.getFileType()).fileDownloadUri(fileInfo.getUri()).size(fileInfo.getSize()).build()).collect(Collectors.toList());
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
