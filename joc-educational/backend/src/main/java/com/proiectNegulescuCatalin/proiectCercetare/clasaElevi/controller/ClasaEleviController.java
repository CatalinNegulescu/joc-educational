package com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.controller;

import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.dto.DatePersonaleUtilizator;
import com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.model.Clasa;
import com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.service.ClasaEleviService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/clasa-elevi")
@Slf4j
public class ClasaEleviController {

    @Autowired
    ClasaEleviService clasaEleviService;

    @GetMapping("/all")
    public ResponseEntity<List<Clasa>> claseElevi(HttpServletRequest request){
        return new ResponseEntity<>(clasaEleviService.getClaseElevi(), HttpStatus.OK);
    }
}
