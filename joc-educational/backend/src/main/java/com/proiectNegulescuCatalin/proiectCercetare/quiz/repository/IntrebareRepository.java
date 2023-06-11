package com.proiectNegulescuCatalin.proiectCercetare.quiz.repository;

import com.proiectNegulescuCatalin.proiectCercetare.quiz.model.Intrebare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntrebareRepository extends JpaRepository<Intrebare, String> {

    List<Intrebare> findAllByIdClasa(String idClasa);
}
