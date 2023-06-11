package com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.repository;

import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Utilizator;
import com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.model.Clasa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasaRepository extends JpaRepository<Clasa, String> {
}


