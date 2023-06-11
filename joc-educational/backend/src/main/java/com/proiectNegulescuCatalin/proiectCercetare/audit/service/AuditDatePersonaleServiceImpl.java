package com.proiectNegulescuCatalin.proiectCercetare.audit.service;

import com.proiectNegulescuCatalin.proiectCercetare.audit.repository.AuditDatePersonaleRepository;
import com.proiectNegulescuCatalin.proiectCercetare.audit.repository.AuditLoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuditDatePersonaleServiceImpl {
    @Autowired
    private final AuditDatePersonaleRepository auditDatePersonaleRepository;

}
