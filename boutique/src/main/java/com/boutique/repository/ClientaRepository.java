package com.boutique.repository;

import com.boutique.domain.Clienta;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;

public interface ClientaRepository extends JpaRepository<Clienta, Long> {
    Optional<Clienta> findByCorreo(String correo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Clienta c where c.correo = :correo")
    Clienta bloquearPorCorreo(String correo);
}
