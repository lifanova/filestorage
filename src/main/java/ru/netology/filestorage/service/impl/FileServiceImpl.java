package ru.netology.filestorage.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.exception.ErrorInputData;
import ru.netology.filestorage.mapper.MapperUtil;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.model.entity.User;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.service.FileService;
import ru.netology.filestorage.service.UsersService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final UsersService usersService;
    private final MapperUtil mapperUtil;

    public FileServiceImpl(FileRepository fileRepository, UsersService usersService, MapperUtil mapperUtil) {
        this.fileRepository = fileRepository;
        this.usersService = usersService;
        this.mapperUtil = mapperUtil;
    }

    public Page<FileDto> filesList(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = usersService.getByUsername(username);
        // todo  - user check on existing

        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));
        Page<FileEntity> pageFile = fileRepository.findFilesByUserId(user.getId(), pageRequest);

        return mapperUtil.mapEntityIntoDto(pageFile, FileDto.class);
    }

    public FileEntity getFile(String filename) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = usersService.getByUsername(username);
        // todo  - user check on existing

        Optional<FileEntity> entity = fileRepository.findByName(user.getId(), filename);

        if (entity.isEmpty()) {
            throw new ErrorInputData("Файл по запросу не найден!");
        }

        return entity.get();
    }

    public FileDto uploadFile(String filename, MultipartFile multipartFile) {
        FileEntity file = null;
        try {
            file = createFileFromRequest(filename, multipartFile);
        } catch (IOException e) {
            throw new ErrorInputData(e.getMessage());
        }

        FileEntity savedFile = fileRepository.saveAndFlush(file);

        return mapperUtil.mapEntityIntoDto(savedFile, FileDto.class);
    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = usersService.getByUsername(username);
        // todo  - user check on existing

        Optional<FileEntity> entity = fileRepository.findByName(user.getId(), oldFilename);
        if (entity.isEmpty()) {
            throw new ErrorInputData("Файл по запросу не найден!");
        }

        FileEntity updatedFileEntity = entity.get();
        updatedFileEntity.setName(editNameRequest.getNewFilename());
        updatedFileEntity.setUpdated(LocalDateTime.now());

        fileRepository.saveAndFlush(updatedFileEntity);
    }

    public void deleteFile(String filename) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = usersService.getByUsername(username);
        // todo  - user check on existing

        Optional<FileEntity> entity = fileRepository.findByName(user.getId(), filename);
        if (entity.isEmpty()) {
            throw new ErrorInputData("Файл по запросу не найден!");
        }

        // TODO: вернуть ответ
        fileRepository.deleteById(entity.get().getId());
    }


    public FileEntity createFileFromRequest(String filename, MultipartFile multipartFile) throws IOException {
        FileEntity file = new FileEntity();
        file.setName(filename);
        file.setBytes(multipartFile.getBytes());
        file.setSize(multipartFile.getBytes().length);
        file.setMimetype(multipartFile.getContentType());
        file.setCreated(LocalDateTime.now());
        file.setUpdated(LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = usersService.getByUsername(username);
        file.setUser(user);

        return file;
    }


}
