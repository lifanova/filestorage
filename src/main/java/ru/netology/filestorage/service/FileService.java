package ru.netology.filestorage.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.File;

import java.util.Optional;

public interface FileService {
    Page<FileDto> listFiles(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit);
    File getFile(String filename);
    FileDto uploadFile(String filename, MultipartFile multipartFile);
    void editFilename(String oldFilename, EditNameRequest editNameRequest);
    void deleteFile(String filename);
}
