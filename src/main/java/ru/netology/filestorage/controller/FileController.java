package ru.netology.filestorage.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.api.FileStorageApi;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.service.impl.FileServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping(path = "/cloud")
public class FileController implements FileStorageApi {
    private final FileServiceImpl fileService;

    public FileController(FileServiceImpl fileService) {
        this.fileService = fileService;
    }

    /**
     * Список файлов из хранилища
     *
     * @param sort
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(path = "/list")
    public Page<FileDto> listFiles(@RequestParam Optional<String> sort,
                                   @RequestParam Optional<Integer> page,
                                   @RequestParam Optional<Integer> limit) {
        return fileService.filesList(sort, page, limit);
    }

    /**
     * Получить файл по имени
     *
     * @param filename
     * @return
     */
    @GetMapping(path = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> getFile(@RequestParam String filename) {
        FileEntity fileEntity = fileService.getFile(filename);

        return ResponseEntity.ok(createFormData(fileEntity));
    }

    /**
     * Загрузка (uploading) файла в хранилище
     *
     * @param filename
     * @param file
     * @return
     */
    @PostMapping(path = "/file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> uploadFile(@RequestParam String filename, @RequestParam MultipartFile file) {
        FileDto savedFile = fileService.uploadFile(filename, file);

        return ResponseEntity.ok().body(savedFile);
    }

    /**
     * Редактирование файла, существующего в хранилище
     *
     * @param oldFilename
     * @param editNameRequest
     */
    @PutMapping(path = "/file", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void editFilename(@RequestParam("filename") String oldFilename, @RequestBody EditNameRequest editNameRequest) {
        fileService.editFilename(oldFilename, editNameRequest);
    }

    /**
     * Удаление файла из хранилища
     *
     * @param filename
     */
    @DeleteMapping(path = "/file")
    public void deleteFile(@RequestParam String filename) {
        fileService.deleteFile(filename);
    }


    private MultiValueMap<String, Object> createFormData(FileEntity fileEntity){
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
