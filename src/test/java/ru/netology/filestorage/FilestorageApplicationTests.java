package ru.netology.filestorage;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@Transactional
class FilestorageApplicationTests {
    public static final PostgreSQLContainer container;

    static {
        container = new PostgreSQLContainer("postgres").withUsername("user").withPassword("pass");
        container.start();
    }

    @DynamicPropertySource
    static void setContainerProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
    }
}
