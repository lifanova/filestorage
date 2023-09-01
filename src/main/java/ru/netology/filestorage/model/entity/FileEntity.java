package ru.netology.filestorage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String mimetype;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime updated;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(nullable = false)
    private byte[] bytes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FileEntity(String name, User user) {
        this.name = name;
        this.user = user;
    }
    public FileEntity(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }
}
