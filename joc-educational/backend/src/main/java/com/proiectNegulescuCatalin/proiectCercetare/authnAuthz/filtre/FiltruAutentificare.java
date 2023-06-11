package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.filtre;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Login;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Utilizator;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.repository.UtilizatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class FiltruAutentificare extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    public FiltruAutentificare(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/utilizator/login");
    }

    @Value("${secret.key}")
    private String secretKey;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Login loginUser = new Login();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            loginUser = objectMapper
                    .readValue(request.getInputStream(), Login.class);
        } catch (IOException e) {
            log.info("Am exceptie la citirea credentialelor");
            throw new RuntimeException(e);
        }

       log.info("Username-ul este {}", loginUser.getUsername());
       log.info("Parola este {} ", loginUser.getPassword());
       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());

       log.info("token autentificare " , authenticationToken);
        System.out.println("token autentificare " + authenticationToken);
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, enctype");
        response.setHeader("Access-Control-Max-Age", "7200");
       return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User)authResult.getPrincipal();

        log.info("username ", user.getUsername());
        Algorithm algorithm = Algorithm.HMAC512(secretKey.getBytes());  // aici ar fi cheia de criptare, in mod normal as avea pt fiecare use
                                                                        // cate o cheie, pe care o aduc aici decriptata si dupa o adaug aici din alta clasa utilitara
        String tokenAcces = JWT.create().withSubject(user.getUsername())
                                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                                        .withIssuer(request.getRequestURL().toString())
                                        .withClaim("roluri", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                                        .sign(algorithm);
        String refreshToken = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("tokenAcces", tokenAcces);
        tokens.put("tokenRefresh", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        final Cookie accesCookie = new Cookie("tokenAccesCookie", tokenAcces);
        accesCookie.setMaxAge(3600);
        response.addCookie(accesCookie);

        final Cookie refreshCookie = new Cookie("tokenRefreshCookie", refreshToken);
        refreshCookie.setMaxAge(7200);
        response.addCookie(refreshCookie);
        log.info("Am generat cookie-urile");

//        tokens.put("id", String.valueOf(utilizator.getId()));
//        tokens.put("firstName", utilizator.getPrenume());
//        tokens.put("lastName", utilizator.getNume());

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, enctype");
        response.setHeader("Access-Control-Max-Age", "7200");
        System.out.println("Am atasat cookie-urile");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
