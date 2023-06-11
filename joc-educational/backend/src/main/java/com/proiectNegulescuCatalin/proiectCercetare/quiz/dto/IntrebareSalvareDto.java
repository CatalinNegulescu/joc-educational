package com.proiectNegulescuCatalin.proiectCercetare.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntrebareSalvareDto {

    private String classId;
    private String question;
    private String questionResponse1;
    private String questionResponse2;
    private String questionResponse3;
    private String questionResponse4;
    private String questionValidResponse;
}
