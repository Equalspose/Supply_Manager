package ru.rostelecom.supply;

import org.junit.jupiter.api.Test;
import ru.rostelecom.supply.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderTest {

    private Product product(String price) {
        Product p = new Product();
        p.setPrice(new BigDecimal(price));
        return p;
    }

    @Test
    void totalIsSumOfItems() {
        PurchaseOrder order = new PurchaseOrder();
        order.addItem(new OrderItem(product("100"), 3, new BigDecimal("100.00")));
        order.addItem(new OrderItem(product("2.5"), 10, new BigDecimal("2.50")));
        order.recalculateTotal();
        assertEquals(new BigDecimal("325.00"), order.getTotalAmount());
    }

    @Test
    void progressReflectsReceivedQuantity() {
        PurchaseOrder order = new PurchaseOrder();
        OrderItem a = new OrderItem(product("1"), 10, BigDecimal.ONE);
        OrderItem b = new OrderItem(product("1"), 10, BigDecimal.ONE);
        order.addItem(a);
        order.addItem(b);
        a.setReceivedQty(10);
        b.setReceivedQty(5);
        assertEquals(75, order.getProgress());
        assertEquals(5, b.getRemaining());
    }

    @Test
    void overdueOnlyForActiveOrders() {
        PurchaseOrder order = new PurchaseOrder();
        order.setExpectedDate(LocalDate.now().minusDays(1));
        order.setStatus(OrderStatus.SENT);
        assertTrue(order.isOverdue());
        order.setStatus(OrderStatus.DELIVERED);
        assertFalse(order.isOverdue());
    }

    @Test
    void statusRules() {
        assertTrue(OrderStatus.DRAFT.isEditable());
        assertFalse(OrderStatus.APPROVED.isEditable());
        assertTrue(OrderStatus.SENT.canReceive());
        assertTrue(OrderStatus.PARTIALLY_DELIVERED.canReceive());
        assertFalse(OrderStatus.DRAFT.canReceive());
    }
}
