package ru.netology.filestorage.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.mapper.FileUtil;
import ru.netology.filestorage.mapper.MapperUtil;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.File;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.service.FileService;

import java.io.FileNotFoundException;
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

    public Page<FileDto> listFiles(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {
        Long userCredentialsId = fileUtil.getFileOwnerUserCredentialsId();
        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));
        Page<File> pageFile = fileRepository.findFilesByUserCredentialsId(userCredentialsId, pageRequest);
        return mapperUtil.mapEntityPageIntoDtoPage(pageFile, FileDto.class);
    }

    public File getFile(String filename) {
        return fileRepository.findFileByNameEquals(fileUtil.getFileOwnerUserCredentialsId(), filename).orElseThrow(FileNotFoundException::new);
    }

    public FileDto uploadFile(String filename, MultipartFile multipartFile) {
        File file = fileUtil.createFileFromRequest(filename, multipartFile);

        File savedFile = fileRepository.saveAndFlush(file);

        return mapperUtil.mapEntityIntoDto(savedFile, FileDto.class);
    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) {
        File file = fileRepository.findFileByNameEquals(fileUtil.getFileOwnerUserCredentialsId(), oldFilename).orElseThrow(FileNotFoundException::new);
        File withNewFilename = fileUtil.editFilename(file, editNameRequest.getNewFilename());
        fileRepository.saveAndFlush(withNewFilename);
    }

    public void deleteFile(String filename) {
        File file = fileRepository.findFileByNameEquals(fileUtil.getFileOwnerUserCredentialsId(), filename).orElseThrow(FileNotFoundException::new);
        fileRepository.deleteById(file.getId());
    }
}
