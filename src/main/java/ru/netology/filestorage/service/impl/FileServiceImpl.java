package ru.netology.filestorage.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.exception.ErrorInputData;
import ru.netology.filestorage.mapper.FileUtil;
import ru.netology.filestorage.mapper.MapperUtil;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.service.FileService;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileUtil fileUtil;
    private final MapperUtil mapperUtil;

    public FileServiceImpl(FileRepository fileRepository, FileUtil fileUtil, MapperUtil mapperUtil) {
        this.fileRepository = fileRepository;
        this.fileUtil = fileUtil;
        this.mapperUtil = mapperUtil;
    }

    public Page<FileDto> filesList(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {
        Long userCredentialsId = fileUtil.getFileOwnerUserCredentialsId();
        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));
        Page<FileEntity> pageFile = fileRepository.findFilesByUserCredentialsId(userCredentialsId, pageRequest);

        return mapperUtil.mapEntityIntoDto(pageFile, FileDto.class);
    }

    public FileEntity getFile(String filename) {
        Optional<FileEntity> entity = fileRepository.findByName(fileUtil.getFileOwnerUserCredentialsId(), filename);

        if (entity.isEmpty()) {
            throw new ErrorInputData("Файл по запросу не найден!");
        }

        return entity.get();
    }

    public FileDto uploadFile(String filename, MultipartFile multipartFile) {
        FileEntity file = null;
        try {
            file = fileUtil.createFileFromRequest(filename, multipartFile);
        } catch (IOException e) {
            throw new ErrorInputData(e.getMessage());
        }

        FileEntity savedFile = fileRepository.saveAndFlush(file);

        return mapperUtil.mapEntityIntoDto(savedFile, FileDto.class);
    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) {
        Optional<FileEntity> entity = fileRepository.findByName(fileUtil.getFileOwnerUserCredentialsId(), oldFilename);
        if (entity.isEmpty()) {
            throw new ErrorInputData("Файл по запросу не найден!");
        }

        FileEntity withNewFilename = fileUtil.editFilename(entity.get(), editNameRequest.getNewFilename());

        fileRepository.saveAndFlush(withNewFilename);
    }

    public void deleteFile(String filename) {
        Optional<FileEntity> entity = fileRepository.findByName(fileUtil.getFileOwnerUserCredentialsId(), filename);
        if (entity.isEmpty()) {
            throw new ErrorInputData("Файл по запросу не найден!");
        }

        // TODO: вернуть ответ
        fileRepository.deleteById(entity.get().getId());
    }
}
