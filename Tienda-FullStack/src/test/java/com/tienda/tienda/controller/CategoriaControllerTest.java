package com.tienda.tienda.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tienda.tienda.model.Categoria;
import com.tienda.tienda.model.Producto;
import com.tienda.tienda.repository.CategoriaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaController categoriaController;

    private Categoria categoria1;
    private Categoria categoria2;
    private List<Categoria> categorias;

    @BeforeEach
    void setUp() {
        // Primera categoría
        categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNombre("Electrónicos");
        categoria1.setDescripcion("Productos electrónicos");
        categoria1.setActivo(true);
        categoria1.setFechaCreacion(LocalDateTime.now());
        categoria1.setProductos(new ArrayList<>());

        // Segunda categoría
        categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNombre("Ropa");
        categoria2.setDescripcion("Productos de vestir");
        categoria2.setActivo(true);
        categoria2.setFechaCreacion(LocalDateTime.now());
        categoria2.setProductos(new ArrayList<>());

        // Lista de categorías
        categorias = Arrays.asList(categoria1, categoria2);
    }

    @Test
    void whenGetAllCategorias_thenReturnsCategoriasList() {
        // Arrange
        when(categoriaRepository.findByActivoTrue()).thenReturn(categorias);

        // Act
        List<Categoria> result = categoriaController.getAllCategorias();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electrónicos", result.get(0).getNombre());
        assertEquals("Ropa", result.get(1).getNombre());
        verify(categoriaRepository).findByActivoTrue();
    }

    @Test
    void whenGetAllCategorias_withNoActiveCategories_thenReturnsEmptyList() {
        // Arrange
        when(categoriaRepository.findByActivoTrue()).thenReturn(new ArrayList<>());

        // Act
        List<Categoria> result = categoriaController.getAllCategorias();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository).findByActivoTrue();
    }

    @Test
    void verifyCategoriaProductosRelationship() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        
        // Act
        categoria1.addProducto(producto);

        // Assert
        assertTrue(categoria1.getProductos().contains(producto));
        assertEquals(categoria1, producto.getCategoria());
    }

    @Test
    void verifyRemoveProductoFromCategoria() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        categoria1.addProducto(producto);

        // Act
        categoria1.removeProducto(producto);

        // Assert
        assertFalse(categoria1.getProductos().contains(producto));
        assertNull(producto.getCategoria());
    }

   
}