package ru.rostelecom.supply.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_item")
public class DeliveryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @Column(nullable = false)
    private int quantity;

    public DeliveryItem() {}

    public DeliveryItem(OrderItem orderItem, int quantity) {
        this.orderItem = orderItem;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Delivery getDelivery() { return delivery; }
    public void setDelivery(Delivery delivery) { this.delivery = delivery; }
    public OrderItem getOrderItem() { return orderItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
