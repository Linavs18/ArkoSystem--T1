package co.edu.sena.arkosystem.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Detalle_orden")
public class Detalle_orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden_compra orden;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Inventario producto;

    private int cantidad;

    @Column(name = "precio_unitario")
    private double precioUnitario;

    @Column(name = "subtotal", insertable = false, updatable = false)
    private double subtotal;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orden_compra getOrden() {
        return orden;
    }

    public void setOrden(Orden_compra orden) {
        this.orden = orden;
    }

    public Inventario getProducto() {
        return producto;
    }

    public void setProducto(Inventario producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
