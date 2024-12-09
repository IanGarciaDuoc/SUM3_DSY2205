package com.tienda.tienda.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPedidoTest {

    @Test
    void testCantidadDeEstados() {
        // Assert
        assertEquals(6, EstadoPedido.values().length, "La enumeración EstadoPedido debe tener 6 estados.");
    }

    @Test
    void testEstadosDefinidos() {
        // Act & Assert
        assertNotNull(EstadoPedido.valueOf("PENDIENTE"));
        assertNotNull(EstadoPedido.valueOf("CONFIRMADO"));
        assertNotNull(EstadoPedido.valueOf("EN_PROCESO"));
        assertNotNull(EstadoPedido.valueOf("ENVIADO"));
        assertNotNull(EstadoPedido.valueOf("ENTREGADO"));
        assertNotNull(EstadoPedido.valueOf("CANCELADO"));
    }

    @Test
    void testOrdenDeEstados() {
        // Act
        EstadoPedido[] estados = EstadoPedido.values();

        // Assert
        assertEquals(EstadoPedido.PENDIENTE, estados[0]);
        assertEquals(EstadoPedido.CONFIRMADO, estados[1]);
        assertEquals(EstadoPedido.EN_PROCESO, estados[2]);
        assertEquals(EstadoPedido.ENVIADO, estados[3]);
        assertEquals(EstadoPedido.ENTREGADO, estados[4]);
        assertEquals(EstadoPedido.CANCELADO, estados[5]);
    }

    @Test
    void testConversionAString() {
        // Act & Assert
        assertEquals("PENDIENTE", EstadoPedido.PENDIENTE.toString());
        assertEquals("CONFIRMADO", EstadoPedido.CONFIRMADO.toString());
        assertEquals("EN_PROCESO", EstadoPedido.EN_PROCESO.toString());
        assertEquals("ENVIADO", EstadoPedido.ENVIADO.toString());
        assertEquals("ENTREGADO", EstadoPedido.ENTREGADO.toString());
        assertEquals("CANCELADO", EstadoPedido.CANCELADO.toString());
    }
}
