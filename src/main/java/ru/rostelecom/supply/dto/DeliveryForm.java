package ru.rostelecom.supply.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import ru.rostelecom.supply.domain.DeliveryStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Форма приёмки поставки на склад. */
public class DeliveryForm {

    @NotNull
    private Long orderId;

    @NotNull(message = "Укажите дату поставки")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deliveryDate = LocalDate.now();

    @Size(max = 50)
    private String waybill;

    @Size(max = 150)
    private String carrier;

    @NotNull
    private DeliveryStatus status = DeliveryStatus.ACCEPTED;

    @Size(max = 500)
    private String comment;

    private List<Line> lines = new ArrayList<>();

    public static class Line {
        private Long orderItemId;
        private Integer quantity;

        public Line() {}

        public Line(Long orderItemId, Integer quantity) {
            this.orderItemId = orderItemId;
            this.quantity = quantity;
        }

        public Long getOrderItemId() { return orderItemId; }
        public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
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
    public List<Line> getLines() { return lines; }
    public void setLines(List<Line> lines) { this.lines = lines; }
}
