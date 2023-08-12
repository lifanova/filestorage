package ru.netology.filestorage.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.filestorage.model.entity.FileEntity;

import java.io.FileNotFoundException;

@Sql({"/sql/insertFile.sql"})
public class FileRepositoryTest {
    @Autowired
    private FileRepository fileRepository;

    @Test
    public void findFileByName() throws FileNotFoundException {
        FileEntity expectedFile = new FileEntity();
        expectedFile.setName("Image");
        expectedFile.setSize(222000);
        expectedFile.setMimetype("image/png");
        expectedFile.setBytes(new byte[]{49, 50, 51, 52});

        FileEntity actualFile = fileRepository.findByName(1L, "Image").orElseThrow(FileNotFoundException::new);

        Assertions.assertThat(actualFile).usingRecursiveComparison().ignoringFields("id", "lastedited", "userCredentials").isEqualTo(expectedFile);
    }


    @Test
    public void findFilesByUserId() {
        Long expectedNumOfElements = 3L;
        Long actualNumOfElements = fileRepository.findFilesByUserId(1L, PageRequest.ofSize(10)).getTotalElements();
        org.junit.jupiter.api.Assertions.assertEquals(expectedNumOfElements, actualNumOfElements);
    }
}
