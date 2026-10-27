package ru.rostelecom.supply.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Форма создания / редактирования заказа поставщику. */
public class OrderForm {

    @NotNull(message = "Выберите поставщика")
    private Long supplierId;

    @NotNull(message = "Выберите склад")
    private Long warehouseId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedDate;

    private String comment;

    @Valid
    @NotEmpty(message = "Добавьте хотя бы одну позицию")
    private List<OrderItemForm> items = new ArrayList<>();

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public LocalDate getExpectedDate() { return expectedDate; }
    public void setExpectedDate(LocalDate expectedDate) { this.expectedDate = expectedDate; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public List<OrderItemForm> getItems() { return items; }
    public void setItems(List<OrderItemForm> items) { this.items = items; }
}
