package ru.netology.filestorage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.filestorage.model.entity.File;

import java.util.Optional;

@Repository
public class FileRepository extends JpaRepository<File, Long> {
    @Query("from File f where f.userCredentials.id = :id and f.name = :name")
    Optional<File> findFileByNameEquals(@Param("id") Long id, @Param("name") String filename);

    @Query(value = "from File f where f.userCredentials.id = :id",
            countQuery = "select count(f) from File f where f.userCredentials.id = :id")
    Page<File> findFilesByUserCredentialsId(@Param("id") Long id, Pageable pageable);

}
