package com.tienda.tienda.dto;

import com.tienda.tienda.model.EstadoPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private String nombreUsuario;
    private List<DetallePedidoDTO> detalles;
    private EstadoPedido estado;
    private BigDecimal montoTotal;
    private String direccionEnvio;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @Data
    public static class DetallePedidoDTO {
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}