package org.metaphorce.booster.repository;

import org.metaphorce.booster.entity.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    List<Paquete> findByStatus(char status);
    List<Paquete> findAllByOrderByFechaEnvioAsc();
    List<Paquete> findByStatusOrderByFechaEnvioAsc(char status);
}
