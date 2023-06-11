package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.dto.DatePersonaleUtilizator;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Login;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Rol;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Utilizator;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.repository.UtilizatorRepository;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.service.UtilizatorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin("*")
@RequestMapping("/utilizator")
@Slf4j
public class UtilizatorController {
    @Autowired
    private final UtilizatorService utilizatorService;
    private final UtilizatorRepository utilizatorRepository;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody Login utilizator){
        Boolean raspuns = false;
        boolean passwordMatch = false;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(utilizator.getPassword());

        try {
            String dbPassword = utilizatorRepository.findByUsername(utilizator.getUsername()).getPassword();
            passwordMatch = passwordEncoder.matches(utilizator.getPassword(), dbPassword);

            System.out.println("dbPassword " + dbPassword);
            System.out.println("password " + encodedPassword);
        }catch (Exception exception){
            log.info("Am iesit din try catch");
        }
        if(passwordMatch)
        {
            System.out.println("Parola introdusa " + utilizator.getPassword() + " este corecta.");
            raspuns = true;
        } else {
            System.out.println("Credentiale gresite");
        }
        return new ResponseEntity<>(raspuns, HttpStatus.OK);
    }

    @PostMapping("/creare-cont")
    public ResponseEntity<Utilizator> salvareUtilizator(@RequestBody Utilizator utilizator){
        Utilizator u1 = utilizatorService.saveUtilizator(utilizator);
        return new ResponseEntity<>(u1,HttpStatus.OK);
    }

    @GetMapping("/date-personale-utilizator")
    public ResponseEntity<DatePersonaleUtilizator> datePersonaleUtilizator(HttpServletRequest request){
        DatePersonaleUtilizator datePersonaleUtilizator =  utilizatorService.getUserPersonalData(utilizatorService.getUsernameFromRequestToken(request));
        return new ResponseEntity<>(datePersonaleUtilizator,HttpStatus.OK);
    }

    @PutMapping("/salvare-date-personale")
    public ResponseEntity<Utilizator> salvareDatePersonale(@RequestBody Utilizator utilizator, HttpServletRequest request){
        Utilizator utilizatorActualizat = utilizatorService.updateUserPersonalData(utilizator, request);
        return new ResponseEntity<>(utilizatorActualizat,HttpStatus.OK);
    }

    @GetMapping("/obtine-utilizatori")
    public ResponseEntity<List<Utilizator>> obtineUtilizatori(){
        return new ResponseEntity<>(utilizatorService.obtineUtilizatori(),HttpStatus.OK);
    }

    @GetMapping("/obtine-roluri/lista")
    public ResponseEntity<List<Rol>> obtineRoluri(){
        return new ResponseEntity<>(utilizatorService.obtineRoluri(),HttpStatus.OK);
    }

    @PostMapping("/rol/salvare-rol")
    public ResponseEntity<Rol> salvareRol(@RequestBody Rol rol){
        return new ResponseEntity<>(utilizatorService.saveRol(rol),HttpStatus.OK);
    }

    @PostMapping("/rol/adaugare-rol-la-utilizator")
    public ResponseEntity<?> adaugareaRoluluiLaUtilizator(@RequestBody RolToUtilizatorForm rolToUtilizatorForm){
        utilizatorService.adaugaRolUnuiUtilizator(rolToUtilizatorForm.usernameAutentificare, rolToUtilizatorForm.numeRol);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headerAutorizare = request.getHeader(AUTHORIZATION);
        if(headerAutorizare != null && headerAutorizare.startsWith("Bearer ")){
            try{
                String refreshToken = headerAutorizare.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC512("A4YZ4B07FJQ945Y97EJ0O5MU9WXZ4TK0Z0QP4KWY6OQAASCHIC4IBI4NG6QZA8GR4CGZM090IW370XDMAEXIGCVKEX2EY839LMPB8U7ZT70TRETKFEEZU8X8WWIHQPCOZGMGMTHYE7RE0G5ZANZS0OXCF1CJUHF9GZOGMLAWVWGVS5Y313P3UP0CSUBYXX4AVL9VPILIPI0IQCYS19UM0276HCME3809GKVFEM4EB7WFF7KJY3C24LYLPB0KRJEUK3O43GF00N5DD2LFSLO61HUX6SE4GG7XOF92PPZT0QJA".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT =jwtVerifier.verify(refreshToken);
                String usernameAutentificare = decodedJWT.getSubject();
                Utilizator utilizator = utilizatorService.getUtilizatorAutentificare(usernameAutentificare);
                Stream<String> stream = Stream.of(utilizatorService.getRolById(utilizator.getFkRole()).getNumeRol());

                String tokenDeAcces = JWT.create().withSubject(utilizator.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roluri", stream.collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("tokenAcces", tokenDeAcces);
                tokens.put("tokenRefresh", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                final Cookie accesCookie = new Cookie("tokenAccesCookie", tokenDeAcces);
                accesCookie.setMaxAge(600);
                response.addCookie(accesCookie);

                final Cookie refreshCookie = new Cookie("tokenRefreshCookie", refreshToken);
                refreshCookie.setMaxAge(3600);
                response.addCookie(refreshCookie);
                log.info("Am generat cookie-urile");

                response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
                response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, enctype");
                response.setHeader("Access-Control-Max-Age", "3600");
                System.out.println("Am atasat cookie-urile");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);


            } catch (Exception exception){
                log.error("Eroare: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                //response.sendError(FORBIDDEN.value());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> eroare = new HashMap<>();
                eroare.put("Mesaj-eroare:", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), eroare);
            }


        }else {
            throw new RuntimeException("Refresh token-ul lipseste");

        }
    }

}
@Data
class RolToUtilizatorForm{
    public String usernameAutentificare;
    public String numeRol;
}