package com.tienda.tienda.dto;

import com.tienda.tienda.model.EstadoPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoDTOTest {

    @Test
    void testPedidoDTO_SettersAndGetters() {
        // Crear un PedidoDTO
        PedidoDTO pedido = new PedidoDTO();
        
        // Configurar valores para los campos
        pedido.setId(1L);
        pedido.setNombreUsuario("usuarioPrueba");
        pedido.setEstado(EstadoPedido.EN_PROCESO);
        pedido.setMontoTotal(BigDecimal.valueOf(500.75));
        pedido.setDireccionEnvio("Calle Falsa 123");
        pedido.setFechaCreacion(LocalDateTime.of(2024, 12, 1, 10, 0));
        pedido.setFechaActualizacion(LocalDateTime.of(2024, 12, 2, 15, 30));

        // Crear un DetallePedidoDTO
        PedidoDTO.DetallePedidoDTO detalle = new PedidoDTO.DetallePedidoDTO();
        detalle.setNombreProducto("Producto 1");
        detalle.setCantidad(3);
        detalle.setPrecioUnitario(BigDecimal.valueOf(100.25));
        detalle.setSubtotal(BigDecimal.valueOf(300.75));

        // Configurar la lista de detalles en el PedidoDTO
        pedido.setDetalles(List.of(detalle));

        // Verificar que los valores se hayan configurado correctamente
        assertEquals(1L, pedido.getId());
        assertEquals("usuarioPrueba", pedido.getNombreUsuario());
        assertEquals(EstadoPedido.EN_PROCESO, pedido.getEstado());
        assertEquals(BigDecimal.valueOf(500.75), pedido.getMontoTotal());
        assertEquals("Calle Falsa 123", pedido.getDireccionEnvio());
        assertEquals(LocalDateTime.of(2024, 12, 1, 10, 0), pedido.getFechaCreacion());
        assertEquals(LocalDateTime.of(2024, 12, 2, 15, 30), pedido.getFechaActualizacion());
        assertNotNull(pedido.getDetalles());
        assertEquals(1, pedido.getDetalles().size());
        assertEquals("Producto 1", pedido.getDetalles().get(0).getNombreProducto());
        assertEquals(3, pedido.getDetalles().get(0).getCantidad());
        assertEquals(BigDecimal.valueOf(100.25), pedido.getDetalles().get(0).getPrecioUnitario());
        assertEquals(BigDecimal.valueOf(300.75), pedido.getDetalles().get(0).getSubtotal());
    }

    @Test
    void testDetallePedidoDTO_EqualsAndHashCode() {
        // Crear dos DetallePedidoDTO con los mismos valores
        PedidoDTO.DetallePedidoDTO detalle1 = new PedidoDTO.DetallePedidoDTO();
        detalle1.setNombreProducto("Producto A");
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(BigDecimal.valueOf(150.00));
        detalle1.setSubtotal(BigDecimal.valueOf(300.00));

        PedidoDTO.DetallePedidoDTO detalle2 = new PedidoDTO.DetallePedidoDTO();
        detalle2.setNombreProducto("Producto A");
        detalle2.setCantidad(2);
        detalle2.setPrecioUnitario(BigDecimal.valueOf(150.00));
        detalle2.setSubtotal(BigDecimal.valueOf(300.00));

        // Verificar que sean iguales y tengan el mismo hashCode
        assertEquals(detalle1, detalle2);
        assertEquals(detalle1.hashCode(), detalle2.hashCode());
    }

    @Test
    void testPedidoDTO_ToString() {
        // Crear un PedidoDTO
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(1L);
        pedido.setNombreUsuario("usuarioPrueba");

        // Verificar que el método toString genera la salida esperada
        String expected = "PedidoDTO(id=1, nombreUsuario=usuarioPrueba, detalles=null, estado=null, " +
                          "montoTotal=null, direccionEnvio=null, fechaCreacion=null, fechaActualizacion=null)";
        assertEquals(expected, pedido.toString());
    }
    @Test
void testDetallePedidoDTO_NullValues() {
    // Crear un DetallePedidoDTO con valores nulos
    PedidoDTO.DetallePedidoDTO detalle = new PedidoDTO.DetallePedidoDTO();
    detalle.setNombreProducto(null);
    detalle.setCantidad(null);
    detalle.setPrecioUnitario(null);
    detalle.setSubtotal(null);

    // Verificar que los valores sean nulos
    assertNull(detalle.getNombreProducto());
    assertNull(detalle.getCantidad());
    assertNull(detalle.getPrecioUnitario());
    assertNull(detalle.getSubtotal());
}
@Test
void testPedidoDTO_NotEquals() {
    PedidoDTO pedido1 = new PedidoDTO();
    pedido1.setId(1L);

    PedidoDTO pedido2 = new PedidoDTO();
    pedido2.setId(2L);

    // Verificar que no sean iguales
    assertNotEquals(pedido1, pedido2);

    // Verificar que los hashCodes sean diferentes
    assertNotEquals(pedido1.hashCode(), pedido2.hashCode());
}
@Test
void testPedidoDTO_EmptyDetalles() {
    PedidoDTO pedido = new PedidoDTO();
    pedido.setDetalles(null);

    // Verificar que los detalles sean null
    assertNull(pedido.getDetalles());
}
@Test
void testPedidoDTO_EqualsReflexivity() {
    PedidoDTO pedido = new PedidoDTO();
    pedido.setId(1L);

    // Verificar que el pedido sea igual a sí mismo
    assertEquals(pedido, pedido);

    // Verificar que no sea igual a null
    assertNotEquals(pedido, null);
}
@Test
void testPedidoDTO_MultipleDetalles() {
    PedidoDTO.DetallePedidoDTO detalle1 = new PedidoDTO.DetallePedidoDTO();
    detalle1.setNombreProducto("Producto 1");
    detalle1.setCantidad(1);
    detalle1.setPrecioUnitario(BigDecimal.valueOf(100.00));
    detalle1.setSubtotal(BigDecimal.valueOf(100.00));

    PedidoDTO.DetallePedidoDTO detalle2 = new PedidoDTO.DetallePedidoDTO();
    detalle2.setNombreProducto("Producto 2");
    detalle2.setCantidad(2);
    detalle2.setPrecioUnitario(BigDecimal.valueOf(200.00));
    detalle2.setSubtotal(BigDecimal.valueOf(400.00));

    PedidoDTO pedido = new PedidoDTO();
    pedido.setDetalles(List.of(detalle1, detalle2));

    // Verificar que los detalles se configuraron correctamente
    assertEquals(2, pedido.getDetalles().size());
    assertEquals("Producto 1", pedido.getDetalles().get(0).getNombreProducto());
    assertEquals("Producto 2", pedido.getDetalles().get(1).getNombreProducto());
}
@Test
void testDetallePedidoDTO_LargeValues() {
    PedidoDTO.DetallePedidoDTO detalle = new PedidoDTO.DetallePedidoDTO();
    detalle.setCantidad(Integer.MAX_VALUE);
    detalle.setPrecioUnitario(BigDecimal.valueOf(Double.MAX_VALUE));

    // Calcular subtotal
    detalle.setSubtotal(BigDecimal.valueOf(Double.MAX_VALUE).multiply(BigDecimal.valueOf(Integer.MAX_VALUE)));

    // Verificar los valores
    assertEquals(Integer.MAX_VALUE, detalle.getCantidad());
    assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), detalle.getPrecioUnitario());
}

}
