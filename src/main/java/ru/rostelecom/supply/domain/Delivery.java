package ru.rostelecom.supply.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Поступление товара на склад по заказу. */
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String number;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate = LocalDate.now();

    @Column(length = 50)
    private String waybill;

    @Column(length = 150)
    private String carrier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryStatus status = DeliveryStatus.ACCEPTED;

    @Column(length = 500)
    private String comment;

    @Column(name = "received_by", length = 50)
    private String receivedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<DeliveryItem> items = new ArrayList<>();

    public void addItem(DeliveryItem item) {
        item.setDelivery(this);
        items.add(item);
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(DeliveryItem::getQuantity).sum();
    }

    public Long getId() { return id; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public PurchaseOrder getOrder() { return order; }
    public void setOrder(PurchaseOrder order) { this.order = order; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    public String getWaybill() { return waybill; }
    public void setWaybill(String waybill) { this.waybill = waybill; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public DeliveryStatus getStatus() { return status; }
    public void setStatus(DeliveryStatus status) { this.status = status; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getReceivedBy() { return receivedBy; }
    public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<DeliveryItem> getItems() { return items; }
}
