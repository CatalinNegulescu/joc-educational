package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.repository;

import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Utilizator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilizatorRepository extends JpaRepository<Utilizator, Integer> {

    Utilizator findByUsername(String username);
    //Utilizator findUtilizatorByUsernameAutentificareAndPassword(String usernameAutentificare, String password);

}
