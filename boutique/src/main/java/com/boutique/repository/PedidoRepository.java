package com.boutique.repository;

import com.boutique.domain.EstadoPedido;
import com.boutique.domain.Pedido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByNumeroAndClientaCorreo(String numero, String correo);

    List<Pedido> findByClientaCorreoOrderByFechaDesc(String correo);

    List<Pedido> findAllByOrderByFechaDesc();

    long countByEstado(EstadoPedido estado);
}
