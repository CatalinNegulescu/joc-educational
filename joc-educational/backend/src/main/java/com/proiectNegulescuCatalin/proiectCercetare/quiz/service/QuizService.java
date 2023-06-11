package com.proiectNegulescuCatalin.proiectCercetare.quiz.service;

import com.proiectNegulescuCatalin.proiectCercetare.quiz.dto.IntrebareSalvareDto;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.dto.PunctajDto;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.model.Intrebare;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.model.Punctaj;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.repository.IntrebareRepository;
import com.proiectNegulescuCatalin.proiectCercetare.quiz.repository.PunctajRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuizService {

    @Autowired
    IntrebareRepository intrebareRepository;

    @Autowired
    PunctajRepository punctajRepository;

    @Value("${secret.key}")
    private String secretKey;

    public void saveIntrebare(IntrebareSalvareDto intrebareToSave, String token){

        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token);

        Claims claims = jws.getBody();
        String username = claims.getSubject();

        System.out.println("User " + username );
        Intrebare intrebare1 = new Intrebare();
        intrebare1.setIdClasa(intrebareToSave.getClassId());
        intrebare1.setText(intrebareToSave.getQuestion());
        intrebare1.setVariantaRaspunsA(intrebareToSave.getQuestionResponse1());
        intrebare1.setVariantaRaspunsB(intrebareToSave.getQuestionResponse2());
        intrebare1.setVariantaRaspunsC(intrebareToSave.getQuestionResponse3());
        intrebare1.setVariantaRaspunsD(intrebareToSave.getQuestionResponse4());
        intrebare1.setVariantaRaspunsCorect(intrebareToSave.getQuestionValidResponse());
        intrebare1.setCreatedAt(LocalDateTime.now());
        intrebare1.setCreatedBy(username);

        intrebareRepository.save(intrebare1);

    }

    public List<Intrebare> getListIntrebariByClassId(String idClasa){
        List<Intrebare> list = new ArrayList<>();
        try{
            list = intrebareRepository.findAllByIdClasa(idClasa);
            return list;
        } catch (NoResultException e){
            log.info("Nu avem o lista de intrebari pentru id-ul clasei " + idClasa);
            return list;
        }
    }

    public void savePunctaj(PunctajDto punctajDto, String token){

        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token);
        Claims claims = jws.getBody();
        String username = claims.getSubject();
        Punctaj punctaj = new Punctaj();
        punctaj.setScore(punctajDto.getScore());
        punctaj.setFkClass(punctajDto.getClassId());
        punctaj.setTestTime(punctajDto.getTestTimeInSec());
        punctaj.setUsername(username);
        punctaj.setCreatedBy(username);
        punctaj.setCreatedAt(LocalDateTime.now());

        punctajRepository.save(punctaj);

    }

}
