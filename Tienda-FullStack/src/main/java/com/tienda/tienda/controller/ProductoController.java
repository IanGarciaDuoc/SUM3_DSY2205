package com.tienda.tienda.controller;

import com.tienda.tienda.dto.MessageResponse;
import com.tienda.tienda.model.Producto;
import com.tienda.tienda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos los productos activos
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findByActivoTrue();
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoRepository.findByIdAndActivoTrue(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto) {
        try {
            System.out.println("Recibiendo producto: " + producto); // Para debug
            
            // Validaciones básicas
            if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body(new MessageResponse("El precio debe ser mayor a 0"));
            }
            
            if (producto.getStock() == null || producto.getStock() < 0) {
                return ResponseEntity.badRequest().body(new MessageResponse("El stock debe ser mayor o igual a 0"));
            }

            producto.setActivo(true);
            Producto nuevoProducto = productoRepository.save(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error al crear el producto: " + e.getMessage()));
        }
    }
    
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody Producto productoDetails) {
        
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoDetails.getNombre());
                    producto.setDescripcion(productoDetails.getDescripcion());
                    producto.setPrecio(productoDetails.getPrecio());
                    producto.setStock(productoDetails.getStock());
                    producto.setUrlImagen(productoDetails.getUrlImagen());
                    producto.setCategoria(productoDetails.getCategoria());
                    
                    productoRepository.save(producto);
                    return ResponseEntity.ok(new MessageResponse("Producto actualizado con éxito"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un producto (soft delete) (solo admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setActivo(false);
                    productoRepository.save(producto);
                    return ResponseEntity.ok(new MessageResponse("Producto eliminado con éxito"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}/stock")
public ResponseEntity<?> getStock(@PathVariable Long id) {
    return productoRepository.findByIdAndActivoTrue(id)
        .map(producto -> ResponseEntity.ok(Map.of(
            "id", producto.getId(),
            "nombre", producto.getNombre(),
            "stock", producto.getStock()
        )))
        .orElse(ResponseEntity.notFound().build());
}
@GetMapping("/search")
public List<Producto> searchProductos(@RequestParam String query) {
    return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(query);
}

@GetMapping("/categoria/{categoriaId}")
public List<Producto> getProductosPorCategoria(@PathVariable Long categoriaId) {
    return productoRepository.findByActivoTrueAndCategoriaId(categoriaId);
}
}