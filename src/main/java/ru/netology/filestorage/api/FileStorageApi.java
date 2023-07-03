package ru.netology.filestorage.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;

import java.util.Optional;

public interface FileStorageApi {
    Page<FileDto> listFiles(Optional<String> sort,
                            Optional<Integer> page,
                            Optional<Integer> limit);

    ResponseEntity<MultiValueMap<String, Object>> getFile(String filename);
    ResponseEntity<FileDto> uploadFile(String filename, MultipartFile file);

    void editFilename(String oldFilename, EditNameRequest editNameRequest);

    void deleteFile(String filename);
}
