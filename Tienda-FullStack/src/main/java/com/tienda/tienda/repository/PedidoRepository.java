package com.tienda.tienda.repository;

import com.tienda.tienda.model.EstadoPedido;
import com.tienda.tienda.model.Pedido;
import com.tienda.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
    List<Pedido> findByUsuarioId(Long usuarioId);
    List<Pedido> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);



    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByUsuarioIdAndEstado(Long usuarioId, EstadoPedido estado);
    List<Pedido> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}