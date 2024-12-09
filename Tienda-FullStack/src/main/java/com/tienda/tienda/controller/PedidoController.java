package com.tienda.tienda.controller;


import com.tienda.tienda.dto.CarritoDTO.*;
import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.MessageResponse;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.HashMap;


@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    
    private final Map<Long, List<CarritoItemResponse>> carritos = new ConcurrentHashMap<>();
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;


    // Obtener todos los pedidos (para admin)
    @GetMapping("/admin")
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    // Obtener pedidos del usuario autenticado
    @GetMapping("/mis-pedidos")
public ResponseEntity<?> getMisPedidos(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    List<Pedido> pedidos = pedidoRepository.findByUsuarioId(userPrincipal.getId());
    
    if (pedidos.isEmpty()) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    return ResponseEntity.ok(pedidos.stream()
        .map(pedido -> {
            Map<String, Object> pedidoMap = new HashMap<>();
            pedidoMap.put("id", pedido.getId());
            pedidoMap.put("estado", pedido.getEstado());
            pedidoMap.put("montoTotal", pedido.getMontoTotal());
            pedidoMap.put("fechaCreacion", pedido.getFechaCreacion());
            pedidoMap.put("direccionEnvio", pedido.getDireccionEnvio());
            pedidoMap.put("cantidadItems", pedido.getDetalles().size());
            return pedidoMap;
        })
        .collect(Collectors.toList()));

    }
    // Obtener detalle de un pedido específico
    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Verificar que el pedido pertenece al usuario o es admin
        if (!pedido.getUsuario().getId().equals(userPrincipal.getId()) 
            && !userPrincipal.getAuthorities().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body(
                new MessageResponse("No tienes permiso para ver este pedido")
            );
        }

        return ResponseEntity.ok(pedido);
    }

    // Actualizar estado del pedido (para admin)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(
            @PathVariable Long id,
            @RequestBody String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        try {
            pedido.setEstado(EstadoPedido.valueOf(nuevoEstado.toUpperCase()));
            pedidoRepository.save(pedido);
            return ResponseEntity.ok(
                new MessageResponse("Estado del pedido actualizado correctamente")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new MessageResponse("Estado no válido")
            );
        }
    }

    // Cancelar pedido (solo si está en estado PENDIENTE o CONFIRMADO)
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getId().equals(userPrincipal.getId())) {
            return ResponseEntity.status(403).body(
                new MessageResponse("No tienes permiso para cancelar este pedido")
            );
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE && 
            pedido.getEstado() != EstadoPedido.CONFIRMADO) {
            return ResponseEntity.badRequest().body(
                new MessageResponse("No se puede cancelar el pedido en su estado actual")
            );
        }

        // Devolver stock
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(
            new MessageResponse("Pedido cancelado correctamente")
        );
    }

    @PostMapping("/carrito/agregar")
    public ResponseEntity<?> agregarAlCarrito(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AgregarProductoRequest request) {
        
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                
        if (producto.getStock() < request.getCantidad()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("No hay suficiente stock disponible"));
        }

        List<CarritoItemResponse> carrito = carritos.computeIfAbsent(
            userPrincipal.getId(), k -> new ArrayList<>());

        CarritoItemResponse item = new CarritoItemResponse();
        item.setProductoId(producto.getId());
        item.setNombreProducto(producto.getNombre());
        item.setCantidad(request.getCantidad());
        item.setPrecioUnitario(producto.getPrecio());
        item.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(request.getCantidad())));

        carrito.add(item);

        return ResponseEntity.ok(new MessageResponse("Producto agregado al carrito"));
    }

    @GetMapping("/carrito")
    public ResponseEntity<CarritoResponse> verCarrito(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<CarritoItemResponse> items = carritos.getOrDefault(userPrincipal.getId(), new ArrayList<>());
        
        CarritoResponse response = new CarritoResponse();
        response.setItems(items);
        response.setCantidadItems(items.size());
        response.setTotal(items.stream()
                .map(CarritoItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/finalizar")
public ResponseEntity<?> finalizarCompra(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody FinalizarCompraRequest request) {

    Usuario usuario = usuarioRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    List<CarritoItemResponse> items = carritos.get(userPrincipal.getId());
    if (items == null || items.isEmpty()) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse("El carrito está vacío"));
    }

    if (request.getDireccionEnvio() == null || request.getDireccionEnvio().trim().isEmpty()) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse("La dirección de envío es requerida"));
    }

    try {
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setEstado(EstadoPedido.CONFIRMADO);
        pedido.setDetalles(new ArrayList<>());

        for (CarritoItemResponse item : items) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("No hay suficiente stock de " + producto.getNombre()));
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            pedido.getDetalles().add(detalle);

            // Actualizar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        pedido.calcularMontoTotal();
        pedidoRepository.save(pedido);
        
        // Limpiar carrito
        carritos.remove(userPrincipal.getId());

        return ResponseEntity.ok(new MessageResponse("Compra realizada con éxito"));
    } catch (Exception e) {
        return ResponseEntity.internalServerError()
                .body(new MessageResponse("Error al procesar la compra: " + e.getMessage()));
    }
    }

    @DeleteMapping("/carrito/limpiar")
    public ResponseEntity<?> limpiarCarrito(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        carritos.remove(userPrincipal.getId());
        return ResponseEntity.ok(new MessageResponse("Carrito limpiado con éxito"));
    }


    @GetMapping("/historial")
    public ResponseEntity<?> getHistorialPedidos(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Usuario usuario = usuarioRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        List<Pedido> pedidos = pedidoRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
        
        // Si no hay pedidos, devolver lista vacía
        if (pedidos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/historial/{id}")
    public ResponseEntity<?> getPedidoDetalle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Verificar que el pedido pertenece al usuario
        if (!pedido.getUsuario().getId().equals(userPrincipal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("No tienes permiso para ver este pedido"));
        }

        return ResponseEntity.ok(pedido);
    }
    
}