package com.proiectNegulescuCatalin.proiectCercetare.audit.repository;

import com.proiectNegulescuCatalin.proiectCercetare.audit.model.AuditDatePersonaleUtilizator;
import com.proiectNegulescuCatalin.proiectCercetare.audit.model.AuditLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditDatePersonaleRepository extends JpaRepository<AuditDatePersonaleUtilizator, Integer> {
}
