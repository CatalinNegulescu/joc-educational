package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.repository;

import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Rol findByNumeRol(String numeRol);

    Rol getRolById(Integer id);
}
