package ru.netology.filestorage.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.exception.ErrorInputData;
import ru.netology.filestorage.exception.FileStorageError;
import ru.netology.filestorage.exception.UserNotFoundException;
import ru.netology.filestorage.mapper.Mapper;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.model.entity.User;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.service.AuthService;
import ru.netology.filestorage.service.FileService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final AuthService authService;
    private final Mapper mapper;

    public FileServiceImpl(FileRepository fileRepository, AuthService authService, Mapper mapper) {
        this.fileRepository = fileRepository;
        this.authService = authService;
        this.mapper = mapper;
    }

    public Page<FileDto> filesList(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getByUsername(username);
        if (user == null) {
            throw new FileStorageError("[filesList]: ERROR: Пользователь отсутствует в БД!");
        }

        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));
        Page<FileEntity> pageFile = fileRepository.findFilesByUserId(user.getId(), pageRequest);

        return mapper.mapEntityIntoDto(pageFile, FileDto.class);
    }

    public FileEntity getFile(String filename) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getByUsername(username);
        if (user == null) {
            throw new FileStorageError("[getFile]: ERROR: Пользователь отсутствует в БД!");
        }

        Optional<FileEntity> entity = fileRepository.findByName(user.getId(), filename);

        if (entity.isEmpty()) {
            throw new FileStorageError("[getFile]: ERROR: Файл по запросу не найден!");
        }

        return entity.get();
    }

    public FileDto uploadFile(String filename, MultipartFile multipartFile) {
        FileEntity file = null;
        try {
            file = createFileFromRequest(filename, multipartFile);
        } catch (IOException e) {
            throw new ErrorInputData("[uploadFile]: Error: " + e.getMessage());
        }

        FileEntity savedFile = fileRepository.saveAndFlush(file);

        return mapper.mapEntityIntoDto(savedFile, FileDto.class);
    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getByUsername(username);
        if (user == null) {
            throw new FileStorageError("[editFilename]: ERROR: Пользователь отсутствует в БД!");
        }

        Optional<FileEntity> entity = fileRepository.findByName(user.getId(), oldFilename);
        if (entity.isEmpty()) {
            throw new FileStorageError("Файл по запросу не найден!");
        }

        FileEntity updatedFileEntity = entity.get();
        updatedFileEntity.setName(editNameRequest.getNewFilename());
        updatedFileEntity.setUpdated(LocalDateTime.now());

        fileRepository.saveAndFlush(updatedFileEntity);
    }

    public void deleteFile(String filename) {
        if (filename.isBlank()) {
            throw new ErrorInputData("[deleteFile]: ERROR: не указано название файла!");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getByUsername(username);

        if (user == null) {
            throw new FileStorageError("[deleteFile]: ERROR: Пользователь отсутствует в БД!");
        }

        Optional<FileEntity> entity = fileRepository.findByName(user.getId(), filename);
        if (entity.isEmpty()) {
            throw new FileStorageError("[deleteFile]: ERROR: Файл по запросу не найден!");
        }

        // TODO: вернуть ответ
        fileRepository.deleteById(entity.get().getId());
    }


    public FileEntity createFileFromRequest(String filename, MultipartFile multipartFile) throws IOException {
        if (filename.isBlank()) {
            throw new ErrorInputData("[createFileFromRequest]: ERROR: не указано название файла!");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getByUsername(username);

        if (user == null) {
            throw new FileStorageError("[createFileFromRequest]: ERROR: Пользователь отсутствует в БД!");
        }

        FileEntity file = new FileEntity(filename, user);
        file.setBytes(multipartFile.getBytes());
        file.setSize(multipartFile.getBytes().length);
        file.setMimetype(multipartFile.getContentType());
        file.setCreated(LocalDateTime.now());
        file.setUpdated(LocalDateTime.now());

        return file;
    }
}
