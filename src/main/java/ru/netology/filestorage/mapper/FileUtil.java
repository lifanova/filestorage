package ru.netology.filestorage.mapper;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.service.impl.UserCredentialsServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class FileUtil {
    private final UserCredentialsServiceImpl userCredentialsService;

    public FileUtil(UserCredentialsServiceImpl userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    //TODO: оформить через билдер
    public FileEntity createFileFromRequest(String filename, MultipartFile multipartFile) throws IOException {
        FileEntity file = new FileEntity();
        file.setName(filename);
        file.setBytes(multipartFile.getBytes());
        file.setSize(multipartFile.getBytes().length);
        file.setMimetype(multipartFile.getContentType());
        file.setCreated(LocalDateTime.now());
        file.setUpdated(LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //TODO
        //UserCredentials userCredentials = userCredentialsService.loadUserCredentialsByUsername(username);
        //file.setUserCredentials(userCredentials);
        return file;
    }

    public FileEntity editFilename(FileEntity file, String newFilename) {
        file.setName(newFilename);
        file.setUpdated(LocalDateTime.now());

        return file;
    }

    public Long getFileOwnerUserCredentialsId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return null;//userCredentialsService.loadUserCredentialsByUsername(username).getId();
    }

    public static MultiValueMap<String, Object> createFormData(FileEntity fileEntity){
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        ByteArrayResource byteArrayResource = new ByteArrayResource(fileEntity.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(fileEntity.getMimetype()));
        headers.setContentLength(fileEntity.getSize());

        HttpEntity<Resource> entity = new HttpEntity<>(byteArrayResource, headers);
        formData.add("file", entity);

        return formData;
    }

}
