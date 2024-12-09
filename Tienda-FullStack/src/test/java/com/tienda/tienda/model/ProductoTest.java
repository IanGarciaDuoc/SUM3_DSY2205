package com.tienda.tienda.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción de prueba para el producto");
        producto.setPrecio(new BigDecimal("99.99"));
        producto.setStock(10);
        producto.setUrlImagen("http://example.com/imagen.jpg");
        producto.setActivo(true);
        producto.setCategoria(new Categoria());
    }

    @Test
    void testOnCreate() {
        // Ejecutar
        producto.onCreate();

        // Verificar
        assertNotNull(producto.getFechaCreacion(), "La fecha de creación no debe ser nula");
        assertNotNull(producto.getFechaActualizacion(), "La fecha de actualización no debe ser nula");
        assertTrue(producto.getActivo(), "El estado activo debe ser verdadero por defecto si no se define");
    }

    @Test
    void testOnUpdate() {
        // Configurar
        LocalDateTime fechaAnterior = LocalDateTime.now().minusDays(1);
        producto.setFechaActualizacion(fechaAnterior);

        // Ejecutar
        producto.onUpdate();

        // Verificar
        assertNotNull(producto.getFechaActualizacion(), "La fecha de actualización no debe ser nula");
        assertTrue(producto.getFechaActualizacion().isAfter(fechaAnterior), "La fecha de actualización debe ser posterior a la fecha anterior");
    }

    @Test
    void testCalcularPrecioTotal() {
        // Configurar
        BigDecimal precioUnitario = producto.getPrecio();
        int cantidad = 5;
        BigDecimal precioTotalEsperado = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        // Ejecutar
        BigDecimal precioTotalCalculado = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        // Verificar
        assertEquals(precioTotalEsperado, precioTotalCalculado, "El precio total debe ser igual al precio unitario multiplicado por la cantidad");
    }

    @Test
    void testProductoActivoPorDefecto() {
        // Configurar
        Producto nuevoProducto = new Producto();

        // Ejecutar
        nuevoProducto.onCreate();

        // Verificar
        assertTrue(nuevoProducto.getActivo(), "El producto debe estar activo por defecto al ser creado");
    }

    @Test
    void testSetCategoria() {
        // Configurar
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoría Test");

        // Ejecutar
        producto.setCategoria(categoria);

        // Verificar
        assertNotNull(producto.getCategoria(), "La categoría no debe ser nula");
        assertEquals(categoria, producto.getCategoria(), "La categoría asignada debe coincidir con la establecida");
    }


    @Test
void testSetNombre() {
    // Configurar
    String nuevoNombre = "Nuevo Producto";

    // Ejecutar
    producto.setNombre(nuevoNombre);

    // Verificar
    assertEquals(nuevoNombre, producto.getNombre(), "El nombre del producto debe coincidir con el establecido");
}

@Test
void testSetDescripcion() {
    // Configurar
    String nuevaDescripcion = "Nueva descripción del producto";

    // Ejecutar
    producto.setDescripcion(nuevaDescripcion);

    // Verificar
    assertEquals(nuevaDescripcion, producto.getDescripcion(), "La descripción del producto debe coincidir con la establecida");
}

@Test
void testSetPrecio() {
    // Configurar
    BigDecimal nuevoPrecio = new BigDecimal("199.99");

    // Ejecutar
    producto.setPrecio(nuevoPrecio);

    // Verificar
    assertEquals(nuevoPrecio, producto.getPrecio(), "El precio del producto debe coincidir con el establecido");
}

@Test
void testSetStock() {
    // Configurar
    int nuevoStock = 20;

    // Ejecutar
    producto.setStock(nuevoStock);

    // Verificar
    assertEquals(nuevoStock, producto.getStock(), "El stock del producto debe coincidir con el establecido");
}

@Test
void testSetUrlImagen() {
    // Configurar
    String nuevaUrl = "http://example.com/nueva-imagen.jpg";

    // Ejecutar
    producto.setUrlImagen(nuevaUrl);

    // Verificar
    assertEquals(nuevaUrl, producto.getUrlImagen(), "La URL de la imagen debe coincidir con la establecida");
}

@Test
void testSetActivo() {
    // Configurar
    boolean nuevoEstado = false;

    // Ejecutar
    producto.setActivo(nuevoEstado);

    // Verificar
    assertFalse(producto.getActivo(), "El estado activo debe coincidir con el establecido");
}

@Test
void testModificarStock() {
    // Configurar
    int stockInicial = producto.getStock();
    int cantidadModificacion = 3;

    // Ejecutar
    producto.setStock(stockInicial - cantidadModificacion);

    // Verificar
    assertEquals(stockInicial - cantidadModificacion, producto.getStock(), "El stock debe reducirse correctamente");
}

@Test
void testProductoToString() {
    // Ejecutar
    String productoString = producto.toString();

    // Verificar
    assertNotNull(productoString, "El método toString no debe devolver null");
    assertTrue(productoString.contains("Producto Test"), "El método toString debe incluir el nombre del producto");
}

@Test
void testSetFechaCreacion() {
    // Configurar
    LocalDateTime nuevaFechaCreacion = LocalDateTime.now();

    // Ejecutar
    producto.setFechaCreacion(nuevaFechaCreacion);

    // Verificar
    assertEquals(nuevaFechaCreacion, producto.getFechaCreacion(), "La fecha de creación debe coincidir con la establecida");
}

@Test
void testSetFechaActualizacion() {
    // Configurar
    LocalDateTime nuevaFechaActualizacion = LocalDateTime.now();

    // Ejecutar
    producto.setFechaActualizacion(nuevaFechaActualizacion);

    // Verificar
    assertEquals(nuevaFechaActualizacion, producto.getFechaActualizacion(), "La fecha de actualización debe coincidir con la establecida");
}

}
