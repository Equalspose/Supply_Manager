package ru.rostelecom.supply.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderItemForm {

    @NotNull(message = "Выберите товар")
    private Long productId;

    @NotNull(message = "Укажите количество")
    @Min(value = 1, message = "Количество должно быть больше нуля")
    private Integer quantity;

    @NotNull(message = "Укажите цену")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    private BigDecimal unitPrice;

    public OrderItemForm() {}

    public OrderItemForm(Long productId, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
