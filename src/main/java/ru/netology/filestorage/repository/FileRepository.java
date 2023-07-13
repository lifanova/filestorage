package ru.netology.filestorage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.filestorage.model.entity.FileEntity;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    @Query("from FileEntity f where f.user.id = :id and f.name = :name")
    Optional<FileEntity> findByName(@Param("id") Long id, @Param("name") String filename);

    @Query(value = "from FileEntity f where f.user.id = :id",
            countQuery = "select count(f) from FileEntity f where f.user.id = :id")
    Page<FileEntity> findFilesByUserId(@Param("id") Long id, Pageable pageable);

}
