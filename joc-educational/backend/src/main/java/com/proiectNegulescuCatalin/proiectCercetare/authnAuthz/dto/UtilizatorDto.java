package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilizatorDto {


    private String numeUtilizator;
    private String usernameAutentificare;
    private String password;
    private String role;
}
