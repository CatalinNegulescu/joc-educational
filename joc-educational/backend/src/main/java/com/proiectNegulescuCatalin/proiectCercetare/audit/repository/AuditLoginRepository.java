package com.proiectNegulescuCatalin.proiectCercetare.audit.repository;

import com.proiectNegulescuCatalin.proiectCercetare.audit.model.AuditLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLoginRepository extends JpaRepository<AuditLogin, Integer> {
}
