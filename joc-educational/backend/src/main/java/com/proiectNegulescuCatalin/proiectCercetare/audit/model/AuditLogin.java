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
@Table(name = "AUDIT_LOGIN")
public class AuditLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_LOGIN")
    private Integer id;

    @Column(name = "FK_USER")
    private Integer fkUser;

    @Column(name = "LOGGED_AT")
    private Date loggedAt;
}