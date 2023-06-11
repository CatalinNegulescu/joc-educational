package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.filtre;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class FiltruAutorizare extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/utilizator/login") || request.getServletPath().equals("/utilizator/token/refresh")){
            filterChain.doFilter(request, response);
        }else {
            String headerAutorizare = request.getHeader(AUTHORIZATION);
            if(headerAutorizare != null && headerAutorizare.startsWith("Bearer ")){
                try{
                    String token = headerAutorizare.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC512("A4YZ4B07FJQ945Y97EJ0O5MU9WXZ4TK0Z0QP4KWY6OQAASCHIC4IBI4NG6QZA8GR4CGZM090IW370XDMAEXIGCVKEX2EY839LMPB8U7ZT70TRETKFEEZU8X8WWIHQPCOZGMGMTHYE7RE0G5ZANZS0OXCF1CJUHF9GZOGMLAWVWGVS5Y313P3UP0CSUBYXX4AVL9VPILIPI0IQCYS19UM0276HCME3809GKVFEM4EB7WFF7KJY3C24LYLPB0KRJEUK3O43GF00N5DD2LFSLO61HUX6SE4GG7XOF92PPZT0QJA".getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT =jwtVerifier.verify(token);
                    String usernameAutentificare = decodedJWT.getSubject();
                    String[] roluriUtilizator = decodedJWT.getClaim("roluri").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roluriUtilizator).forEach(rol -> {
                        authorities.add(new SimpleGrantedAuthority(rol));
                    });

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameAutentificare,
                            null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);

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
                filterChain.doFilter(request,response);

            }
        }
    }
}
