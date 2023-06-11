package com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.service;

import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.dto.DatePersonaleUtilizator;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Rol;
import com.proiectNegulescuCatalin.proiectCercetare.authnAuthz.model.Utilizator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UtilizatorService {

    Utilizator saveUtilizator(Utilizator utilizator);

    DatePersonaleUtilizator getUserPersonalData(String username);

    Utilizator updateUserPersonalData(Utilizator utilizator, HttpServletRequest request);
    String getUsernameFromRequestToken(HttpServletRequest request);
    Rol saveRol(Rol rol);

    Rol getRolById(Integer id);
    void adaugaRolUnuiUtilizator(String usernameAutentificare, String numeRol);
    Utilizator getUtilizatorAutentificare(String usernameAutentificare);
    List<Utilizator> obtineUtilizatori();
    List<Rol> obtineRoluri();

}
