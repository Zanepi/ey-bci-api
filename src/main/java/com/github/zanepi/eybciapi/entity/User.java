package com.github.zanepi.eybciapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID user_id;
    @NotNull
    private String name;
    @NotNull
    @Email
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @CreatedDate
    @Column
    private Date created;
    @LastModifiedDate
    @Column
    private Date modified;
    @Column
    @ColumnDefault("true")
    private boolean active = true;
    @Column
    private Date last_login;
    @Column
    private String token;

    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "phone_id")
    private List<Phone> phones;

}
