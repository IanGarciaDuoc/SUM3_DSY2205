package com.tienda.tienda.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tienda.tienda.dto.CarritoDTO.*;
import com.tienda.tienda.dto.MessageResponse;
import com.tienda.tienda.model.*;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.security.UserPrincipal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PedidoController pedidoController;

    private UserPrincipal userPrincipal;
    private Usuario usuario;
    private Producto producto;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        // Setup Usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("testuser");
        usuario.setCorreo("test@example.com");

        // Setup UserPrincipal
        userPrincipal = new UserPrincipal(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Setup Producto
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);

        // Setup Pedido
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuario(usuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setMontoTotal(new BigDecimal("100.00"));
    }

    @Test
    void whenAgregarAlCarrito_withValidProduct_thenSuccess() {
        // Arrange
        AgregarProductoRequest request = new AgregarProductoRequest();
        request.setProductoId(1L);
        request.setCantidad(2);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<?> response = pedidoController.agregarAlCarrito(userPrincipal, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Producto agregado al carrito", ((MessageResponse) response.getBody()).getMensaje());
    }

    @Test
    void whenAgregarAlCarrito_withInsufficientStock_thenReturnsBadRequest() {
        // Arrange
        AgregarProductoRequest request = new AgregarProductoRequest();
        request.setProductoId(1L);
        request.setCantidad(20); // Más que el stock disponible

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<?> response = pedidoController.agregarAlCarrito(userPrincipal, request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("No hay suficiente stock disponible", ((MessageResponse) response.getBody()).getMensaje());
    }

    @Test
    void whenVerCarrito_thenReturnsCarritoResponse() {
        // Act
        ResponseEntity<CarritoResponse> response = pedidoController.verCarrito(userPrincipal);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getItems());
    }

   
   

    @Test
    void whenCancelarPedido_withInvalidState_thenReturnsBadRequest() {
        // Arrange
        pedido.setEstado(EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        ResponseEntity<?> response = pedidoController.cancelarPedido(1L, userPrincipal);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals(
            "No se puede cancelar el pedido en su estado actual",
            ((MessageResponse) response.getBody()).getMensaje()
        );
    }

    @Test
    void whenGetMisPedidos_thenReturnsPedidosList() {
        // Arrange
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoRepository.findByUsuarioId(1L)).thenReturn(pedidos);

        // Act
        ResponseEntity<?> response = pedidoController.getMisPedidos(userPrincipal);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
    }

    @Test
    void whenActualizarEstadoPedido_withValidState_thenSuccess() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        ResponseEntity<?> response = pedidoController.actualizarEstadoPedido(1L, "CONFIRMADO");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(pedidoRepository).save(argThat(p -> 
            p.getEstado() == EstadoPedido.CONFIRMADO
        ));
    }
    @Test
    void whenAgregarAlCarrito_withNonExistentProduct_thenThrowsException() {
        // Arrange
        AgregarProductoRequest request = new AgregarProductoRequest();
        request.setProductoId(999L);
        request.setCantidad(1);

        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoController.agregarAlCarrito(userPrincipal, request);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    void whenVerCarrito_withEmptyCart_thenReturnsEmptyResponse() {
        // Act
        ResponseEntity<CarritoResponse> response = pedidoController.verCarrito(userPrincipal);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, response.getBody().getTotal());
    }

    @Test
    void whenCancelarPedido_withNonExistentPedido_thenThrowsRuntimeException() {
        // Arrange
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());
    
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoController.cancelarPedido(999L, userPrincipal);
        });
    
        // Verificar el mensaje de la excepción
        assertEquals("Pedido no encontrado", exception.getMessage());
    }
    

    @Test
    void whenActualizarEstadoPedido_withNonExistentPedido_thenReturnsNotFound() {
        // Arrange: Simular que el pedido no existe
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());
    
        // Act & Assert: Capturar la excepción lanzada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoController.actualizarEstadoPedido(999L, "CONFIRMADO");
        });
    
        // Verificar el mensaje de la excepción
        assertEquals("Pedido no encontrado", exception.getMessage());
    }
    

  

    @Test
    void whenGetMisPedidos_withNoPedidos_thenReturnsEmptyList() {
        // Arrange
        when(pedidoRepository.findByUsuarioId(1L)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> response = pedidoController.getMisPedidos(userPrincipal);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void whenActualizarMontoTotal_thenCalculatesCorrectly() {
        // Arrange
        DetallePedido detalle1 = new DetallePedido();
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(BigDecimal.valueOf(100.00));
        detalle1.setSubtotal(BigDecimal.valueOf(200.00)); // Asegurar que el subtotal esté configurado correctamente
    
        DetallePedido detalle2 = new DetallePedido();
        detalle2.setCantidad(1);
        detalle2.setPrecioUnitario(BigDecimal.valueOf(50.00));
        detalle2.setSubtotal(BigDecimal.valueOf(50.00)); // Asegurar que el subtotal esté configurado correctamente
    
        pedido.setDetalles(List.of(detalle1, detalle2));
    
        // Act
        pedido.calcularMontoTotal(); // Método que calcula el total basado en los subtotales
        BigDecimal montoTotal = pedido.getMontoTotal();
    
        // Assert
        assertEquals(new BigDecimal("250.0"), montoTotal); // Validar el total
    }
    @Test
void whenGetAllPedidos_thenReturnsPedidosList() {
    // Arrange
    Pedido pedido1 = new Pedido();
    pedido1.setId(1L);
    pedido1.setEstado(EstadoPedido.PENDIENTE);
    pedido1.setMontoTotal(BigDecimal.valueOf(100.0));
    pedido1.setUsuario(usuario);

    Pedido pedido2 = new Pedido();
    pedido2.setId(2L);
    pedido2.setEstado(EstadoPedido.CONFIRMADO);
    pedido2.setMontoTotal(BigDecimal.valueOf(200.0));
    pedido2.setUsuario(usuario);

    List<Pedido> pedidos = List.of(pedido1, pedido2);

    // Simular el repositorio devolviendo la lista de pedidos
    when(pedidoRepository.findAll()).thenReturn(pedidos);

    // Act
    ResponseEntity<List<Pedido>> response = pedidoController.getAllPedidos();

    // Assert
    assertEquals(200, response.getStatusCodeValue()); // Verificar el código de estado HTTP
    assertNotNull(response.getBody()); // Verificar que el cuerpo de la respuesta no sea nulo
    assertEquals(2, response.getBody().size()); // Verificar que se devuelven dos pedidos

    // Verificar los detalles del primer pedido
    Pedido pedidoResponse1 = response.getBody().get(0);
    assertEquals(1L, pedidoResponse1.getId());
    assertEquals(EstadoPedido.PENDIENTE, pedidoResponse1.getEstado());
    assertEquals(BigDecimal.valueOf(100.0), pedidoResponse1.getMontoTotal());

    // Verificar los detalles del segundo pedido
    Pedido pedidoResponse2 = response.getBody().get(1);
    assertEquals(2L, pedidoResponse2.getId());
    assertEquals(EstadoPedido.CONFIRMADO, pedidoResponse2.getEstado());
    assertEquals(BigDecimal.valueOf(200.0), pedidoResponse2.getMontoTotal());

    // Verificar que el repositorio fue llamado correctamente
    verify(pedidoRepository, times(1)).findAll();
}
@Test
void whenGetPedidoById_withValidId_thenReturnsPedido() {
    // Arrange
    Pedido pedido = new Pedido();
    pedido.setId(1L);
    pedido.setEstado(EstadoPedido.PENDIENTE);
    pedido.setMontoTotal(BigDecimal.valueOf(100.0));
    pedido.setUsuario(usuario);

    // Simular el repositorio devolviendo el pedido con ID 1
    when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

    // Act
    ResponseEntity<Pedido> response = (ResponseEntity<Pedido>) pedidoController.getPedidoById(1L, userPrincipal);

    // Assert
    assertEquals(200, response.getStatusCodeValue()); // Verificar el código de estado HTTP
    assertNotNull(response.getBody()); // Verificar que el cuerpo de la respuesta no sea nulo
    assertEquals(1L, response.getBody().getId()); // Verificar que el ID es correcto
    assertEquals(EstadoPedido.PENDIENTE, response.getBody().getEstado()); // Verificar el estado
    assertEquals(BigDecimal.valueOf(100.0), response.getBody().getMontoTotal()); // Verificar el monto total

    // Verificar que el repositorio fue llamado correctamente
    verify(pedidoRepository, times(1)).findById(1L);
}

@Test
void whenGetPedidoById_withInvalidId_thenReturnsNotFound() {
    // Arrange: Simular que el pedido con ID 999 no existe
    when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert: Capturar la excepción lanzada
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        pedidoController.getPedidoById(999L, userPrincipal);
    });

    // Verificar el mensaje de la excepción
    assertEquals("Pedido no encontrado", exception.getMessage());

    // Verificar que el repositorio fue llamado correctamente
    verify(pedidoRepository, times(1)).findById(999L);
}

}