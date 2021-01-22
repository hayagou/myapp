package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CFileException;
import com.hayagou.myapp.advice.exception.CFileNotFoundException;
import com.hayagou.myapp.config.FileProperties;
import com.hayagou.myapp.repository.FileRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private final Path fileLocation;
    public FileService(FileProperties fileProperties) {
        this.fileLocation = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.fileLocation);
        }catch (Exception e){
            throw new CFileException();
        }
    }

    public String storeFile(MultipartFile multipartFile){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        try{
            if(fileName.contains("..")){
                throw new CFileException();
            }

            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        }catch(IOException e){
            throw new CFileException();
        }
    }

    public Resource loadFile(String fileName){
        try{
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new CFileNotFoundException();
            }

        } catch (MalformedURLException e) {
            throw new CFileNotFoundException();
        }
    }



}
