package com.tienda.tienda.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DetallePedidoTest {

    private DetallePedido detallePedido;

    @BeforeEach
    void setUp() {
        detallePedido = new DetallePedido();
        detallePedido.setCantidad(2);
        detallePedido.setPrecioUnitario(BigDecimal.valueOf(50.0));
    }

    @Test
    void testCalcularSubtotal() {
        // Act
        detallePedido.calcularSubtotal();

        // Assert
        assertNotNull(detallePedido.getSubtotal());
        assertEquals(BigDecimal.valueOf(100.0), detallePedido.getSubtotal());
    }

    @Test
    void testSetCantidadYRecalculo() {
        // Act
        detallePedido.setCantidad(5);
        detallePedido.calcularSubtotal();

        // Assert
        assertEquals(BigDecimal.valueOf(250.0), detallePedido.getSubtotal());
    }

    @Test
    void testSetPrecioUnitarioYRecalculo() {
        // Act
        detallePedido.setPrecioUnitario(BigDecimal.valueOf(75.0));
        detallePedido.calcularSubtotal();

        // Assert
        assertEquals(BigDecimal.valueOf(150.0), detallePedido.getSubtotal());
    }

    @Test
    void testRelacionConPedido() {
        // Arrange
        Pedido pedido = new Pedido();
        detallePedido.setPedido(pedido);

        // Assert
        assertNotNull(detallePedido.getPedido());
        assertEquals(pedido, detallePedido.getPedido());
    }

    @Test
    void testRelacionConProducto() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Producto de prueba");
        detallePedido.setProducto(producto);

        // Assert
        assertNotNull(detallePedido.getProducto());
        assertEquals(producto, detallePedido.getProducto());
        assertEquals("Producto de prueba", detallePedido.getProducto().getNombre());
    }

    @Test
    void testValoresIniciales() {
        // Assert
        assertNull(detallePedido.getId());
        assertNull(detallePedido.getPedido());
        assertNull(detallePedido.getProducto());
        assertEquals(Integer.valueOf(2), detallePedido.getCantidad());
        assertEquals(BigDecimal.valueOf(50.0), detallePedido.getPrecioUnitario());
    }
    @Test
void testHashCode() {
    // Arrange
    DetallePedido otroDetalle = new DetallePedido();
    otroDetalle.setCantidad(2);
    otroDetalle.setPrecioUnitario(BigDecimal.valueOf(50.0));

    // Assert
    assertEquals(detallePedido.hashCode(), otroDetalle.hashCode());
}
@Test
void testEquals() {
    // Arrange
    DetallePedido otroDetalle = new DetallePedido();
    otroDetalle.setCantidad(2);
    otroDetalle.setPrecioUnitario(BigDecimal.valueOf(50.0));

    // Act & Assert
    assertTrue(detallePedido.equals(otroDetalle));
    assertFalse(detallePedido.equals(null));
    assertFalse(detallePedido.equals(new Object())); // Comparar con otro tipo de objeto
}
@Test
void testCanEqual() {
    // Arrange
    DetallePedido otroDetalle = new DetallePedido();

    // Act & Assert
    assertTrue(detallePedido.canEqual(otroDetalle));
    assertFalse(detallePedido.canEqual(new Object())); // No debería ser igual con otro tipo de objeto
}
@Test
void testToString() {
    // Act
    String toString = detallePedido.toString();

    // Assert
    assertNotNull(toString);
    assertTrue(toString.contains("cantidad=2"));
    assertTrue(toString.contains("precioUnitario=50.0"));
}

}
