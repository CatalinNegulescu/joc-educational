package com.proiectNegulescuCatalin.proiectCercetare.security;

import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.filtre.FiltruAutentificare;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.filtre.FiltruAutorizare;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/utilizator/obtine-utilizatori").hasAuthority("ROL_ELEV");
        http.authorizeRequests().antMatchers("/api/**", "/utilizator/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers( "/date-personale-utilizator/**").hasAnyRole("ROL_ELEV");
      http.authorizeRequests().antMatchers( PUT,"/salvare-date-personale/**").hasAnyRole("ROL_ELEV");
        http.authorizeRequests().antMatchers("/utilizator/login/**").permitAll();
        http.authorizeRequests().antMatchers("/utilizator/creare-cont/**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/utilizator/obtine-utilizatori/**").hasAuthority("ROL_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/utilizator/rol/**").hasAuthority("ROL_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/clasa-elevi/all/**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/quiz/**").hasAnyAuthority("ROL_ELEV", "ROL_PROFESOR");
        http.authorizeRequests().antMatchers(POST, "/quiz/**").hasAnyAuthority("ROL_ELEV", "ROL_PROFESOR");

        http.authorizeRequests().antMatchers("/utilizator/obtine-roluri/**").permitAll();

        // http.authorizeRequests().antMatchers(POST, "/utilizator/salvare/admin/**").hasAuthority("ROL_MANAGER");

        // http.authorizeRequests().anyRequest().permitAll();
        //schimb pt teste
        http.authorizeRequests().anyRequest().authenticated();
        http.cors();
        http.addFilter(new FiltruAutentificare(authenticationManagerBean()));
        http.addFilterBefore(new FiltruAutorizare(), UsernamePasswordAuthenticationFilter.class);
    }



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource()
//    {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws  Exception{
        return super.authenticationManagerBean();
    }
}
