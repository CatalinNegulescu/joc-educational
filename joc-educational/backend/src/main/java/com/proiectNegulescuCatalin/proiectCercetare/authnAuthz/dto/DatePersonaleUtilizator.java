package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatePersonaleUtilizator {

    private String id;
    private String username;
    private String email;
    private String nume;
    private String prenume;

}



