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
@Table(name = "QUESTION")
public class Intrebare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_QUESTION")
    private Integer id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "FK_CLASS")
    private String idClasa;

    @Column(name = "RESPONSE_A")
    private String variantaRaspunsA;

    @Column(name = "RESPONSE_B")
    private String variantaRaspunsB;

    @Column(name = "RESPONSE_C")
    private String variantaRaspunsC;

    @Column(name = "RESPONSE_D")
    private String variantaRaspunsD;

    @Column(name = "RESPONSE_VALID")
    private String variantaRaspunsCorect;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;
}
