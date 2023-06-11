package com.proiectNegulescuCatalin.proiectCercetare.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunctajDto {

    private String classId;
    private String score;
    private String testTimeInSec;
}
