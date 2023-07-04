package ru.netology.filestorage.mapper;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.model.entity.File;
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
    public File createFileFromRequest(String filename, MultipartFile multipartFile) throws IOException {
        File file = new File();
        file.setName(filename);
        file.setFile(multipartFile.getBytes());
        file.setSize(multipartFile.getBytes().length);
        file.setMimetype(multipartFile.getContentType());
        file.setLastedited(LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //TODO
        //UserCredentials userCredentials = userCredentialsService.loadUserCredentialsByUsername(username);
        //file.setUserCredentials(userCredentials);
        return file;
    }

    public File editFilename(File file, String newFilename) {
        file.setName(newFilename);
        file.setLastedited(LocalDateTime.now());
        return file;
    }

    public Long getFileOwnerUserCredentialsId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return null;//userCredentialsService.loadUserCredentialsByUsername(username).getId();
    }

}
