package ru.rostelecom.supply.domain;

import jakarta.persistence.*;

/** Остаток товара на конкретном складе. */
@Entity
@Table(name = "stock", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "warehouse_id"}))
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(nullable = false)
    private int quantity;

    public Stock() {}

    public Stock(Product product, Warehouse warehouse, int quantity) {
        this.product = product;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void add(int qty) { this.quantity += qty; }
}
