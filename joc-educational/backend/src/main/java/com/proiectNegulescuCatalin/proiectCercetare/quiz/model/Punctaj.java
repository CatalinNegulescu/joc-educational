package com.proiectNegulescuCatalin.proiectCercetare.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCORE")
public class Punctaj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_SCORE")
    private Integer id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "FK_CLASS")
    private String fkClass;

    @Column(name = "SCORE")
    private String score;

    @Column(name = "TEST_TIME")
    private String testTime;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;
}
