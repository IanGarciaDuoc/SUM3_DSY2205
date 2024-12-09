package com.tienda.tienda.model;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;


@Data
@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_detalle_pedido")
    @SequenceGenerator(name = "seq_detalle_pedido", sequenceName = "seq_detalle_pedido", allocationSize = 1)
    @Column(name = "detalle_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
    }
}