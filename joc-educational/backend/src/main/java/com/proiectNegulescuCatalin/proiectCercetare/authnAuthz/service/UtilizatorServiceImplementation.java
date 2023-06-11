package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.proiectNegulescuCatalin.proiectCercetare.audit.model.AuditDatePersonaleUtilizator;
import com.proiectNegulescuCatalin.proiectCercetare.audit.model.AuditLogin;
import com.proiectNegulescuCatalin.proiectCercetare.audit.repository.AuditDatePersonaleRepository;
import com.proiectNegulescuCatalin.proiectCercetare.audit.repository.AuditLoginRepository;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.dto.DatePersonaleUtilizator;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Rol;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Utilizator;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.repository.RolRepository;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.repository.UtilizatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UtilizatorServiceImplementation implements UtilizatorService, UserDetailsService {
    @Autowired
    private final UtilizatorRepository utilizatorRepository;
    @Autowired
    private final AuditLoginRepository auditLoginRepository;
    @Autowired
    private final AuditDatePersonaleRepository auditDatePersonaleRepository;


    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String usernameAutentificare) throws UsernameNotFoundException {
        Utilizator utilizator = utilizatorRepository.findByUsername(usernameAutentificare);
        String roleName = rolRepository.getRolById(utilizator.getFkRole()).getNumeRol();

        log.info("Nume rol: ", roleName);

        if(utilizator == null){
            log.info("Utilizatorul nu exista in baza de date");
            throw new UsernameNotFoundException("Utilizatorul nu exista in baza de date");
        } else{
            log.info("Utilizatorul {} exista in baza de date", usernameAutentificare);

        }
        AuditLogin auditLogin = new AuditLogin();
        auditLogin.setFkUser(utilizator.getFkRole());
        auditLogin.setLoggedAt(new Date());
        auditLoginRepository.save(auditLogin);
        Collection<SimpleGrantedAuthority> autorizari = new ArrayList<>();
//        utilizator.getRoluriUtilizator().forEach( rol -> {
//            autorizari.add(new SimpleGrantedAuthority(rol.getNumeRol()));
//        });
       // autorizari.add(new SimpleGrantedAuthority(utilizator.getRole()));
        autorizari.add(new SimpleGrantedAuthority(roleName));
        return new org.springframework.security.core.userdetails.User(utilizator.getUsername(), utilizator.getPassword(), autorizari);
    }


    @Override
    public Utilizator saveUtilizator(Utilizator utilizator) {
        log.info("Urmeaza sa salvez un nou utilizator,{} in db", utilizator.getUsername());

        // Versiunea 1
//        String salt = PasswordUtils.getSalt(30);
//        String mySecurePassword = PasswordUtils.generateSecurePassword(utilizator.getPassword(), salt);
//        utilizator.setPassword(mySecurePassword);

        // Versiunea 2
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(utilizator.getPassword());
        utilizator.setPassword(encodedPassword);
        utilizator.setFkRole(1); // TO DO sa modific doar pentru rolul de utilizator
        Utilizator utilizatorReturnat =  utilizatorRepository.save(utilizator);
        log.info("Id util returnat", utilizatorReturnat.getId());
        log.info("Nume util returnat", utilizatorReturnat.getUsername());
        return  utilizatorReturnat;
    }


    @Override
    public DatePersonaleUtilizator getUserPersonalData(String username) {
        Utilizator utilizator = utilizatorRepository.findByUsername(username);
        DatePersonaleUtilizator datePersonaleUtilizator = modelMapper.map(utilizator, DatePersonaleUtilizator.class);
        return datePersonaleUtilizator;
    }

    @Override
    @Transactional
    public Utilizator updateUserPersonalData(Utilizator utilizator, HttpServletRequest request) {
        Utilizator utilizatorDb = utilizatorRepository.findByUsername(getUsernameFromRequestToken(request));
        utilizator.setId(utilizatorDb.getId());
        utilizator.setFkRole((utilizatorDb.getFkRole()));
        utilizator.setPassword(utilizatorDb.getPassword());
        AuditDatePersonaleUtilizator auditDatePersonaleUtilizator = modelMapper.map(utilizatorDb, AuditDatePersonaleUtilizator.class);
        auditDatePersonaleUtilizator.setFkUser(utilizatorDb.getId());
        auditDatePersonaleUtilizator.setModifiedAt(new Date());
        Utilizator utilizatorActualizat = utilizatorRepository.save(utilizator);

        auditDatePersonaleRepository.save(auditDatePersonaleUtilizator);
        return utilizatorActualizat;
    }

    public String getUsernameFromRequestToken(HttpServletRequest request){
        String headerAutorizare = request.getHeader(AUTHORIZATION);
        String username = "";
        if(headerAutorizare != null && headerAutorizare.startsWith("Bearer ")) {
            try {
                String refreshToken = headerAutorizare.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC512("A4YZ4B07FJQ945Y97EJ0O5MU9WXZ4TK0Z0QP4KWY6OQAASCHIC4IBI4NG6QZA8GR4CGZM090IW370XDMAEXIGCVKEX2EY839LMPB8U7ZT70TRETKFEEZU8X8WWIHQPCOZGMGMTHYE7RE0G5ZANZS0OXCF1CJUHF9GZOGMLAWVWGVS5Y313P3UP0CSUBYXX4AVL9VPILIPI0IQCYS19UM0276HCME3809GKVFEM4EB7WFF7KJY3C24LYLPB0KRJEUK3O43GF00N5DD2LFSLO61HUX6SE4GG7XOF92PPZT0QJA".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                username = decodedJWT.getSubject();
            }catch (Exception exception) {
                log.error("Eroare: {}", exception.getMessage());
            }
        }
        return username;
    }
    @Override
    public Rol saveRol(Rol rol) {
        log.info("Urmeaza sa salvez un nou rol, {} in db", rol.getNumeRol());
        return rolRepository.save(rol);
    }


    public Rol getRolById(Integer id) {

        return rolRepository.getRolById(id);
    }

    @Override
    public void adaugaRolUnuiUtilizator(String usernameAutentificare, String numeRol) { // ar mai fi ok si validarile pt nume

        log.info("Adaug rolul {}, utilizatorului {}", numeRol, usernameAutentificare);
        Utilizator utilizator = utilizatorRepository.findByUsername((usernameAutentificare));
        Rol rol = rolRepository.findByNumeRol(numeRol);
        log.info("Rol id " , rol.getId());
        //utilizator.getRoluriUtilizator().add(rol); // TO DO am ramas sa adaug rolul catre utilizator

    }

    @Override
    public Utilizator getUtilizatorAutentificare(String usernameAutentificare) {
        log.info("Obtin utilizatorul {}", usernameAutentificare);
        return utilizatorRepository.findByUsername(usernameAutentificare);
    }

    @Override
    public List<Utilizator> obtineUtilizatori() {
        log.info("Obtin toti utilizatorii");
        return utilizatorRepository.findAll();
    }
    @Override
    public List<Rol> obtineRoluri() {
        log.info("Obtin toate rolurile");
        return rolRepository.findAll();
    }


}
