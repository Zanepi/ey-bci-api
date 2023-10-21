package com.github.zanepi.eybciapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity(name = "phones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "phone_id")
    private UUID phone_id;

    @Column
    private String countrycode;
    @Column
    private String citycode;
    @Column
    private String number;
    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date modified;

    @ManyToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
