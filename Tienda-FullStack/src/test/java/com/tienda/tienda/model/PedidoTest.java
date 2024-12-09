package com.tienda.tienda.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setDetalles(Arrays.asList(
                crearDetallePedido(1L, new BigDecimal("10.50"), 2), // Subtotal: 21.00
                crearDetallePedido(2L, new BigDecimal("5.00"), 1)   // Subtotal: 5.00
        ));
    }

    @Test
    void testCalcularMontoTotal() {
        // Ejecutar
        pedido.calcularMontoTotal();

        // Verificar
        assertEquals(new BigDecimal("26.00"), pedido.getMontoTotal(), "El monto total debe ser la suma de los subtotales");
    }

    @Test
    void testCalcularMontoTotalConDetallesVacios() {
        // Configurar
        pedido.setDetalles(Collections.emptyList());

        // Ejecutar
        pedido.calcularMontoTotal();

        // Verificar
        // Si no hay detalles, se asume que el monto total debe ser 0
        assertEquals(BigDecimal.ZERO, pedido.getMontoTotal(), "Si no hay detalles, el monto total debe ser 0");
    }

    @Test
void testCalcularMontoTotalConDetallesNulos() {
    // Configurar
    pedido.setDetalles(null);

    // Verificar que se lance NullPointerException al llamar calcularMontoTotal()
    assertThrows(NullPointerException.class, () -> pedido.calcularMontoTotal(), 
        "Debería lanzar NullPointerException si detalles es null");
}


    @Test
    void testOnCreateSetsFechaCreacionAndFechaActualizacion() {
        // Ejecutar
        pedido.onCreate();

        // Verificar
        assertNotNull(pedido.getFechaCreacion(), "La fecha de creación no debe ser nula");
        assertNotNull(pedido.getFechaActualizacion(), "La fecha de actualización no debe ser nula");
        assertEquals(pedido.getFechaCreacion(), pedido.getFechaActualizacion(), "Las fechas de creación y actualización deben ser iguales al crearse");
    }

    @Test
    void testOnUpdateSetsFechaActualizacion() {
        // Configurar
        LocalDateTime fechaAnterior = LocalDateTime.now().minusDays(1);
        pedido.setFechaActualizacion(fechaAnterior);

        // Ejecutar
        pedido.onUpdate();

        // Verificar
        assertNotNull(pedido.getFechaActualizacion(), "La fecha de actualización no debe ser nula");
        assertTrue(pedido.getFechaActualizacion().isAfter(fechaAnterior), "La fecha de actualización debe ser posterior a la anterior");
    }

    @Test
    void testGettersAndSetters() {
        // Probando id
        pedido.setId(100L);
        assertEquals(100L, pedido.getId(), "El id debe coincidir con el valor seteado");

        // Probando fechas
        LocalDateTime ahora = LocalDateTime.now();
        pedido.setFechaCreacion(ahora);
        pedido.setFechaActualizacion(ahora.plusHours(1));
        assertEquals(ahora, pedido.getFechaCreacion(), "La fecha de creación debe coincidir con el valor seteado");
        assertEquals(ahora.plusHours(1), pedido.getFechaActualizacion(), "La fecha de actualización debe coincidir con el valor seteado");

        // Probando montoTotal
        pedido.setMontoTotal(new BigDecimal("123.45"));
        assertEquals(new BigDecimal("123.45"), pedido.getMontoTotal(), "El monto total debe coincidir con el valor seteado");

        // Probando detalles
        DetallePedido detalle = crearDetallePedido(3L, new BigDecimal("2.50"), 3); // Subtotal: 7.50
        pedido.setDetalles(Arrays.asList(detalle));
        assertEquals(1, pedido.getDetalles().size(), "Debe haber un detalle seteado");
        assertEquals(detalle, pedido.getDetalles().get(0), "El detalle debe coincidir con el elemento seteado");
    }

    private DetallePedido crearDetallePedido(Long id, BigDecimal precioUnitario, int cantidad) {
        DetallePedido detalle = new DetallePedido();
        detalle.setId(id);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setCantidad(cantidad);
        detalle.calcularSubtotal();
        return detalle;
    }
}
