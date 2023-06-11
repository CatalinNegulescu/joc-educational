package com.proiectNegulescuCatalin.proiectCercetare.quiz.controller;

import com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.model.Clasa;
import com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.service.ClasaEleviService;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.dto.IntrebareSalvareDto;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.dto.PunctajDto;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.model.Intrebare;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.service.QuizService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/quiz")
@Slf4j
public class QuizController {

    @Autowired
    QuizService quizService;

    @Value("${secret.key}")
    private String secretKey;

    @PostMapping("/salvare-intrebare")
    public ResponseEntity<?> saveIntrebare(@RequestBody IntrebareSalvareDto intrebare, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        quizService.saveIntrebare(intrebare, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/intrebari/by-clasa/{idClasa}")
    public ResponseEntity<List<Intrebare>> claseElevi(@PathVariable("idClasa") String idClasa, HttpServletRequest request){
        return new ResponseEntity<>(quizService.getListIntrebariByClassId(idClasa), HttpStatus.OK);
    }

    @PostMapping("/salvare-punctaj")
    public ResponseEntity<?> savePunctaj(@RequestBody PunctajDto punctajDto, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        quizService.savePunctaj(punctajDto, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
