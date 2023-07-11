package ru.netology.filestorage.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "userCredentials", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileEntity> files = new HashSet<>();

    @Column(columnDefinition = "boolean default true")
    private boolean AccountNonExpired;

    @Column(columnDefinition = "boolean default true")
    private boolean AccountNonLocked;

    @Column(columnDefinition = "boolean default true")
    private boolean CredentialsNonExpired;

    @Column(columnDefinition = "boolean default true")
    private boolean Enabled;

}
