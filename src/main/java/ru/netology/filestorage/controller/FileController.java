package ru.netology.filestorage.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.File;
import ru.netology.filestorage.service.FileService;

import java.io.IOException;
import java.util.Optional;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> uploadFile(@RequestParam String filename, @RequestParam MultipartFile file) throws IOException {
        FileDto savedFile = fileService.uploadFile(filename, file);
        return new ResponseEntity<>(savedFile, HttpStatus.OK);
    }

    @GetMapping(path = "file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> getFile(@RequestParam String filename) throws IOException {

        File file = fileService.getFile(filename);
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        ByteArrayResource byteArrayResource = new ByteArrayResource(file.getFile());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getMimetype()));
        headers.setContentLength(file.getSize());
        HttpEntity<Resource> entity = new HttpEntity<>(byteArrayResource, headers);
        formData.add("file", entity);

        return ResponseEntity.ok(formData);
    }

    @PutMapping(path = "file", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void editFilename(@RequestParam("filename") String oldFilename, @RequestBody EditNameRequest editNameRequest) throws FileNotFoundException {
        fileService.editFilename(oldFilename, editNameRequest);
    }

    @DeleteMapping(path = "file")
    public void deleteFile(@RequestParam String filename) throws FileNotFoundException {
        fileService.deleteFile(filename);
    }

    @GetMapping(path = "/list")
    public Page<FileDto> listFiles(@RequestParam Optional<String> sort,
                                   @RequestParam Optional<Integer> page,
                                   @RequestParam Optional<Integer> limit) {
        return fileService.listFiles(sort, page, limit);
    }

}
