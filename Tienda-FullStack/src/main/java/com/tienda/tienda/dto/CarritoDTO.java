

package com.tienda.tienda.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.*;

import jakarta.validation.constraints.Size;

public class CarritoDTO {
    @Data
    public static class AgregarProductoRequest {
        private Long productoId;
        private Integer cantidad;
    }

    @Data
    public static class CarritoItemResponse {
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }

    @Data
    public static class CarritoResponse {
        private List<CarritoItemResponse> items;
        private BigDecimal total;
        private Integer cantidadItems;
    }

    @Data
    public static class FinalizarCompraRequest {
        @NotBlank(message = "La dirección de envío es requerida")
    @Size(min = 10, max = 255, message = "La dirección debe tener entre 10 y 255 caracteres")
    private String direccionEnvio;
    }
}