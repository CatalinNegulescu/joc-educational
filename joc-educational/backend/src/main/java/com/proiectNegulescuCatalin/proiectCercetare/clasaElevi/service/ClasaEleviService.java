package com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.service;

import com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.model.Clasa;
import com.proiectNegulescuCatalin.proiectCercetare.clasaElevi.repository.ClasaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClasaEleviService {

    @Autowired
    ClasaRepository clasaRepository;

    public List<Clasa> getClaseElevi(){
        List<Clasa> list = new ArrayList<>();
        try {
            list = clasaRepository.findAll();
            System.out.printf("Prima clasa "  + list.get(2).getDescription());
        }catch (Exception e){
            log.info("Exceptie obtinere lista clase de elevi");
        }
        return list;
    }

}
