package com.tienda.tienda.dto;

import com.tienda.tienda.model.Categoria;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductoDTOTest {

    @Test
    void testConstructor() {
        CarritoDTO carrito = new CarritoDTO();
        assertNotNull(carrito);
    }


    @Test
    void testCrearProductoRequest_SettersAndGetters() {
        // Crear una instancia de CrearProductoRequest
        ProductoDTO.CrearProductoRequest request = new ProductoDTO.CrearProductoRequest();

        // Configurar valores
        request.setNombre("Producto A");
        request.setDescripcion("Descripción del producto A");
        request.setPrecio(BigDecimal.valueOf(150.50));
        request.setStock(10);
        request.setUrlImagen("http://example.com/imagen.jpg");

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");
        request.setCategoria(categoria);

        // Verificar valores configurados
        assertEquals("Producto A", request.getNombre());
        assertEquals("Descripción del producto A", request.getDescripcion());
        assertEquals(BigDecimal.valueOf(150.50), request.getPrecio());
        assertEquals(10, request.getStock());
        assertEquals("http://example.com/imagen.jpg", request.getUrlImagen());
        assertNotNull(request.getCategoria());
        assertEquals("Electrónica", request.getCategoria().getNombre());
    }

    @Test
    void testProductoResponse_SettersAndGetters() {
        // Crear una instancia de ProductoResponse
        ProductoDTO.ProductoResponse response = new ProductoDTO.ProductoResponse();

        // Configurar valores
        response.setId(1L);
        response.setNombre("Producto B");
        response.setDescripcion("Descripción del producto B");
        response.setPrecio(BigDecimal.valueOf(200.00));
        response.setStock(5);
        response.setUrlImagen("http://example.com/imagen2.jpg");
        response.setActivo(true);

        // Verificar valores configurados
        assertEquals(1L, response.getId());
        assertEquals("Producto B", response.getNombre());
        assertEquals("Descripción del producto B", response.getDescripcion());
        assertEquals(BigDecimal.valueOf(200.00), response.getPrecio());
        assertEquals(5, response.getStock());
        assertEquals("http://example.com/imagen2.jpg", response.getUrlImagen());
        assertTrue(response.getActivo());
    }

    @Test
    void testCrearProductoRequest_InvalidValues() {
        // Crear una instancia de CrearProductoRequest con valores inválidos
        ProductoDTO.CrearProductoRequest request = new ProductoDTO.CrearProductoRequest();

        // Validar valores no configurados
        assertNull(request.getNombre());
        assertNull(request.getDescripcion());
        assertNull(request.getPrecio());
        assertNull(request.getStock());
        assertNull(request.getUrlImagen());
        assertNull(request.getCategoria());
    }

    @Test
    void testProductoResponse_EqualsAndHashCode() {
        // Crear dos instancias de ProductoResponse con los mismos valores
        ProductoDTO.ProductoResponse response1 = new ProductoDTO.ProductoResponse();
        response1.setId(1L);
        response1.setNombre("Producto C");
        response1.setDescripcion("Descripción del producto C");
        response1.setPrecio(BigDecimal.valueOf(300.00));
        response1.setStock(20);
        response1.setUrlImagen("http://example.com/imagen3.jpg");
        response1.setActivo(true);

        ProductoDTO.ProductoResponse response2 = new ProductoDTO.ProductoResponse();
        response2.setId(1L);
        response2.setNombre("Producto C");
        response2.setDescripcion("Descripción del producto C");
        response2.setPrecio(BigDecimal.valueOf(300.00));
        response2.setStock(20);
        response2.setUrlImagen("http://example.com/imagen3.jpg");
        response2.setActivo(true);

        // Verificar que las instancias sean iguales y tengan el mismo hashCode
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testCrearProductoRequest_NullValues() {
        ProductoDTO.CrearProductoRequest request = new ProductoDTO.CrearProductoRequest();
    
        // Verificar que los campos no configurados sean null
        assertNull(request.getNombre());
        assertNull(request.getDescripcion());
        assertNull(request.getPrecio());
        assertNull(request.getStock());
        assertNull(request.getUrlImagen());
        assertNull(request.getCategoria());
    }
  

@Test
void testCrearProductoRequest_LargeValues() {
    ProductoDTO.CrearProductoRequest request = new ProductoDTO.CrearProductoRequest();

    request.setNombre("Producto con un nombre extremadamente largo".repeat(10));
    request.setDescripcion("Descripción extremadamente larga".repeat(20));
    request.setPrecio(BigDecimal.valueOf(Double.MAX_VALUE));
    request.setStock(Integer.MAX_VALUE);
    request.setUrlImagen("http://example.com/imagen-muy-larga.jpg");

    // Verificar valores configurados
    assertNotNull(request.getNombre());
    assertTrue(request.getNombre().length() > 100);
    assertNotNull(request.getDescripcion());
    assertTrue(request.getDescripcion().length() > 500);
    assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), request.getPrecio());
    assertEquals(Integer.MAX_VALUE, request.getStock());
    assertEquals("http://example.com/imagen-muy-larga.jpg", request.getUrlImagen());
}
@Test
void testProductoResponse_NotEquals() {
    ProductoDTO.ProductoResponse response1 = new ProductoDTO.ProductoResponse();
    response1.setId(1L);
    response1.setNombre("Producto E");

    ProductoDTO.ProductoResponse response2 = new ProductoDTO.ProductoResponse();
    response2.setId(2L);
    response2.setNombre("Producto F");

    // Verificar que no sean iguales
    assertNotEquals(response1, response2);

    // Verificar que los hashCodes sean diferentes
    assertNotEquals(response1.hashCode(), response2.hashCode());
}
@Test
void testCrearProductoRequest_Validation() {
    ProductoDTO.CrearProductoRequest request = new ProductoDTO.CrearProductoRequest();

    // No configurar valores para verificar violaciones
    assertNull(request.getNombre());
    assertNull(request.getPrecio());
    assertNull(request.getStock());
}
@Test
void testProductoResponse_PartialValues() {
    ProductoDTO.ProductoResponse response = new ProductoDTO.ProductoResponse();
    response.setId(1L);
    response.setNombre("Producto Parcial");

    // Verificar valores configurados y nulos
    assertEquals(1L, response.getId());
    assertEquals("Producto Parcial", response.getNombre());
    assertNull(response.getDescripcion());
    assertNull(response.getPrecio());
    assertNull(response.getStock());
    assertNull(response.getUrlImagen());
    assertNull(response.getActivo());
}

}
