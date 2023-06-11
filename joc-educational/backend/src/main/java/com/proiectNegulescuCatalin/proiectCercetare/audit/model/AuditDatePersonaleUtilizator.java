package com.proiectNegulescuCatalin.proiectCercetare.audit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUDIT_USER_PERSONAL_DATA")
public class AuditDatePersonaleUtilizator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_PERSONAL_DATA")
    private Integer idPersonalData;

    @Column(name = "FK_USER")
    private Integer fkUser;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NUME")
    private String nume;

    @Column(name = "PRENUME")
    private String prenume;

    @Column(name = "MODIFIED_AT")
    private Date modifiedAt;
}