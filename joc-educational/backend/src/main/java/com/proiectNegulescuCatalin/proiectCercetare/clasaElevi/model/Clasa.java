package com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CLASS")
public class Clasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CLASS")
    private Integer id;

    @Column(name = "DESCRIPTION")
    private String description;
}
