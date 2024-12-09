package com.tienda.tienda.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tienda.tienda.dto.MessageResponse;
import com.tienda.tienda.model.Producto;
import com.tienda.tienda.model.Categoria;
import com.tienda.tienda.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoría Test");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción de prueba");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);
        producto.setUrlImagen("http://ejemplo.com/imagen.jpg");
        producto.setActivo(true);
        producto.setCategoria(categoria);
    }

    @Test
    void whenGetAllProductos_thenReturnsProductosList() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByActivoTrue()).thenReturn(productos);

        // Act
        List<Producto> result = productoController.getAllProductos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Producto Test", result.get(0).getNombre());
    }

    @Test
    void whenGetProductoById_withValidId_thenReturnsProducto() {
        // Arrange
        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<Producto> response = productoController.getProductoById(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Producto Test", response.getBody().getNombre());
    }

    @Test
    void whenGetProductoById_withInvalidId_thenReturnsNotFound() {
        // Arrange
        when(productoRepository.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Producto> response = productoController.getProductoById(99L);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void whenCreateProducto_withValidData_thenSuccess() {
        // Arrange
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Producto);
    }

    @Test
    void whenCreateProducto_withNegativePrice_thenReturnsBadRequest() {
        // Arrange
        producto.setPrecio(new BigDecimal("-100.00"));

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("El precio debe ser mayor a 0", ((MessageResponse)response.getBody()).getMensaje());
    }

    @Test
    void whenCreateProducto_withNegativeStock_thenReturnsBadRequest() {
        // Arrange
        producto.setStock(-10);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("El stock debe ser mayor o igual a 0", ((MessageResponse)response.getBody()).getMensaje());
    }

    @Test
    void whenUpdateProducto_withValidData_thenSuccess() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(1L, producto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void whenUpdateProducto_withInvalidId_thenReturnsNotFound() {
        // Arrange
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(99L, producto);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void whenDeleteProducto_withValidId_thenSuccess() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.eliminarProducto(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(productoRepository).save(argThat(p -> !p.getActivo()));
    }

    @Test
    void whenGetStock_withValidId_thenReturnsStockInfo() {
        // Arrange
        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<?> response = productoController.getStock(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) response.getBody();
        assertNotNull(result);
        assertEquals(10, result.get("stock"));
    }

    @Test
    void whenSearchProductos_thenReturnsFilteredList() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue("Test"))
            .thenReturn(productos);

        // Act
        List<Producto> result = productoController.searchProductos("Test");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Producto Test", result.get(0).getNombre());
    }

    @Test
    void whenGetProductosPorCategoria_thenReturnsFilteredList() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByActivoTrueAndCategoriaId(1L))
            .thenReturn(productos);

        // Act
        List<Producto> result = productoController.getProductosPorCategoria(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCategoria().getId());
    }
}