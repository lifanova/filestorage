package ru.netology.filestorage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.filestorage.mapper.Mapper;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.model.entity.User;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.service.impl.FileServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class FileServiceTest {
    private static final String FILENAME = "testFile";
    private static final String NEW_FILENAME = "updatedFile";
    private static final String TEXT = "Hello, world!";
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private AuthService authService;

    @Mock
    private Mapper mapper;

    private FileEntity file;
    private User user;

    @BeforeEach
    void setUp() {
        fileService = new FileServiceImpl(fileRepository, authService, mapper);

        User user = authService.getByUsername("admin");
        file = new FileEntity(1L, FILENAME, user);

        openMocks(this);
    }

    @Test
    @DisplayName("FileService. Список файлов в хранилище")
    void listFiles() {
        Page<FileEntity> pageFile = new PageImpl<>(Collections.singletonList(file));

        when(fileRepository.findFilesByUserId(anyLong(), any())).thenReturn(pageFile);
        Page<Object> pageObject = new PageImpl<>(List.of(new Object()));
        when(mapper.mapEntityIntoDto(any(), any())).thenReturn(pageObject);

        fileService.filesList(Optional.empty(), Optional.empty(), Optional.of(3));
    }

    @Test
    @DisplayName("FileService. Загрузка файла в хранилище")
    void uploadFile() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, TEXT.getBytes());

        when(fileRepository.saveAndFlush(any())).thenReturn(file);
        //when(mapper.mapEntityIntoDto(any(), any())).thenReturn(new FileDto());

        fileService.uploadFile(mockFile.getName(), mockFile);
    }

    @Test
    @DisplayName("FileService. Получение файла в хранилище")
    void getFile() {
        when(fileRepository.findByName(anyLong(), anyString())).thenReturn(Optional.of(file));

        fileService.getFile(file.getName());
    }

    @Test
    @DisplayName("FileService. Изменение названия файла")
    void editFilename() {
        when(fileRepository.findByName(anyLong(), anyString())).thenReturn(Optional.of(file));

        fileService.editFilename(FILENAME, new EditNameRequest(NEW_FILENAME));
    }

    @Test
    @DisplayName("FileService. Удаление файла")
    void deleteFile() {
        when(fileRepository.findByName(anyLong(), anyString())).thenReturn(Optional.of(file));

        fileService.deleteFile(file.getName());
    }
}
