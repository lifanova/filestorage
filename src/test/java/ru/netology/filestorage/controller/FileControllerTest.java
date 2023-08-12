package ru.netology.filestorage.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.filestorage.model.dto.EditNameRequest;
import ru.netology.filestorage.model.dto.FileDto;
import ru.netology.filestorage.model.entity.FileEntity;
import ru.netology.filestorage.service.FileService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

@WebMvcTest(value = FileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    @DisplayName("Список файлов пользователя")
    void listFiles() throws Exception {
        FileDto fileDto = new FileDto(1L, "file", 10L, MediaType.TEXT_PLAIN_VALUE, LocalDateTime.now());

        Page<FileDto> page = new PageImpl<>(List.of(fileDto));

        when(fileService.filesList(any(), any(), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.content").isArray())
                .andExpect((ResultMatcher) jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("Загрузка файлов в хранилище")
    void uploadFile() throws Exception {
        FileDto fileDto = new FileDto(1L, "file", 10L, MediaType.TEXT_PLAIN_VALUE, LocalDateTime.now());
        when(fileService.uploadFile(anyString(), any(MultipartFile.class))).thenReturn(fileDto);

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/file").file(mockMultipartFile)
                        .param("filename", "myfile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value(fileDto.getId()))
                .andExpect((ResultMatcher) jsonPath("$.name").value(fileDto.getName()))
                .andExpect((ResultMatcher) jsonPath("$.size").value(fileDto.getSize()))
                .andExpect((ResultMatcher) jsonPath("$.mimetype").value(fileDto.getMimetype()))
                .andExpect((ResultMatcher) jsonPath("$.lastedited").exists());

        verify(fileService, times(1)).uploadFile(anyString(), any(MultipartFile.class));
    }

    @Test
    @DisplayName("Получение файла")
    void getFile() throws Exception {
        FileEntity file = new FileEntity();
        file.setId(1L);
        file.setMimetype(MediaType.TEXT_PLAIN_VALUE);
        file.setBytes("Hello".getBytes());

        when(fileService.getFile(anyString())).thenReturn(file);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/file")
                        .param("filename", "filetodownload"))
                .andDo(print())
                .andExpect((ResultMatcher) content().contentTypeCompatibleWith(MediaType.MULTIPART_FORM_DATA))
                .andExpect((ResultMatcher) content().string(containsString("Hello")))
                .andExpect(status().isOk());

        verify(fileService, times(1)).getFile(anyString());
    }

    @Test
    @DisplayName("Изменение названия файла")
    void editFilename() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/file")
                        .param("filename", "old")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filename\": \"new\"}"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(fileService, times(1)).editFilename(anyString(), any(EditNameRequest.class));
    }

    @Test
    @DisplayName("Удаление файла")
    void deleteFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/file")
                        .param("filename", "filetodelete"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(fileService, times(1)).deleteFile(anyString());
    }
}
