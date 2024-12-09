package com.tienda.tienda.repository;

import com.tienda.tienda.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByActivoTrue();
    Optional<Categoria> findByIdAndActivoTrue(Long id);
    List<Categoria> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    boolean existsByNombre(String nombre);
}