package com.tienda.tienda.repository;

import com.tienda.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    Optional<Producto> findByIdAndActivoTrue(Long id); 
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    List<Producto> findByActivoTrueAndCategoriaId(Long categoriaId);
    }
