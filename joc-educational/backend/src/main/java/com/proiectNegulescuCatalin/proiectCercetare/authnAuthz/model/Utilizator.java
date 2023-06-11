package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class Utilizator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_USER")
    private Integer id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FK_ROLE")
    private Integer fkRole;

    @Column(name = "NUME")
    private String nume;

    @Column(name = "PRENUME")
    private String prenume;

//
//
//    @ManyToMany(fetch = FetchType.EAGER) //
//    private Collection<Rol> roluriUtilizator = new ArrayList<>();



}
