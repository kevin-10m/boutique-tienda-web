package com.boutique.repository;

import com.boutique.domain.Pedido;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByNumeroAndClientaCorreo(String numero, String correo);
}
