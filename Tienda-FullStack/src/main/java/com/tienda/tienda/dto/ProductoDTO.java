package com.tienda.tienda.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

import com.tienda.tienda.model.Categoria;

public class ProductoDTO {

    @Data
    public static class CrearProductoRequest {
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        private String nombre;

        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
        private String descripcion;

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal precio;

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;

        private String urlImagen;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "categoria_id")
        private Categoria categoria;
    }

    @Data
    public static class ProductoResponse {
        private Long id;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private Integer stock;
        private String urlImagen;
        private Boolean activo;
    }
}