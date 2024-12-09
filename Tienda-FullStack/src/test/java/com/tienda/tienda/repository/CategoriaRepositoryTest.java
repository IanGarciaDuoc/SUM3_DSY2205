package com.tienda.tienda.repository;

import com.tienda.tienda.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoriaActiva1;
    private Categoria categoriaActiva2;
    private Categoria categoriaInactiva;
    
    @BeforeEach
    void setUp() {
        categoriaRepository.deleteAll();

        categoriaActiva1 = new Categoria();
        categoriaActiva1.setNombre("Electronica");
        categoriaActiva1.setActivo(true);
        categoriaRepository.save(categoriaActiva1);

        categoriaActiva2 = new Categoria();
        categoriaActiva2.setNombre("Ropa");
        categoriaActiva2.setActivo(true);
        categoriaRepository.save(categoriaActiva2);

        categoriaInactiva = new Categoria();
        categoriaInactiva.setNombre("Hogar");
        categoriaInactiva.setActivo(false);
        categoriaRepository.save(categoriaInactiva);
    }

    @Test
    void testFindByActivoTrue() {
        List<Categoria> categoriasActivas = categoriaRepository.findByActivoTrue();
        assertEquals(2, categoriasActivas.size(), "Debe retornar sólo las categorías activas.");
        
    }

    @Test
    void testFindByIdAndActivoTrue() {
        // Debe encontrar la categoría activa por ID
        Optional<Categoria> encontrada = categoriaRepository.findByIdAndActivoTrue(categoriaActiva1.getId());
        assertTrue(encontrada.isPresent(), "Debe encontrar la categoría activa.");
        assertEquals(categoriaActiva1.getId(), encontrada.get().getId(), "El ID debe coincidir.");

        // Intentar encontrar la inactiva por ID no debe retornar nada
        Optional<Categoria> inactivaEncontrada = categoriaRepository.findByIdAndActivoTrue(categoriaInactiva.getId());
        assertFalse(inactivaEncontrada.isPresent(), "No debe encontrar categorías inactivas.");
    }

    @Test
    void testFindByNombreContainingIgnoreCaseAndActivoTrue() {
        // Buscar "electr" ignorando mayúsculas
        List<Categoria> resultado = categoriaRepository.findByNombreContainingIgnoreCaseAndActivoTrue("ELEC");
        assertEquals(1, resultado.size(), "Debe encontrar una categoría que contenga 'ELEC' ignorando mayúsculas.");
        assertEquals("Electronica", resultado.get(0).getNombre(), "El nombre debe ser 'Electronica'.");
    }

    @Test
    void testExistsByNombre() {
        assertTrue(categoriaRepository.existsByNombre("Electronica"), "Debe existir la categoría 'Electronica'.");
        assertFalse(categoriaRepository.existsByNombre("NoExiste"), "No debe existir esta categoría.");
    }
}
