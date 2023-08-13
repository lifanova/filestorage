package ru.netology.filestorage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.netology.filestorage.mapper.Mapper;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.service.impl.FileServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class FileServiceTest {
    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private Mapper mapper;

    private FileEntity file;

    @BeforeEach
    void setUp() {
        file = new FileEntity();
        file.setId(1L);
        file.setName("testFile");

        openMocks(this);
    }

    @Test
    void listFiles() {
        Page<FileEntity> pageFile = new PageImpl<>(Collections.singletonList(file));
        when(fileRepository.findFilesByUserId(anyLong(), any())).thenReturn(pageFile);
        Page<Object> pageObject = new PageImpl<>(List.of(new Object()));
        when(mapper.mapEntityIntoDto(any(), any())).thenReturn(pageObject);

        fileService.filesList(Optional.empty(), Optional.empty(), Optional.of(3));
    }

    @Test
    void uploadFile() throws IOException {
        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        when(fileRepository.saveAndFlush(any())).thenReturn(file);
        //when(mapper.mapEntityIntoDto(any(), any())).thenReturn(new FileDto());

        fileService.uploadFile(mockMultipartFile.getName(), mockMultipartFile);
    }

    @Test
    void getFile() throws FileNotFoundException {
        when(fileRepository.findByName(anyLong(), anyString())).thenReturn(Optional.of(file));

        fileService.getFile(file.getName());
    }

    @Test
    void editFilename() throws FileNotFoundException {
        when(fileRepository.findByName(anyLong(), anyString())).thenReturn(Optional.of(file));

        fileService.editFilename("oldname", new EditNameRequest("newname"));
    }

    @Test
    void deleteFile() throws FileNotFoundException {
        when(fileRepository.findByName(anyLong(), anyString())).thenReturn(Optional.of(file));

        fileService.deleteFile(file.getName());
    }
}
