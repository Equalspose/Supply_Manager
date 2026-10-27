package ru.rostelecom.supply.dto;

import ru.rostelecom.supply.domain.Product;

public record LowStockItem(Product product, long total) {

    public long getShortage() {
        return Math.max(0, product.getMinStock() - total);
    }

    public int getPercent() {
        return product.getMinStock() == 0 ? 100 : (int) Math.min(100, total * 100 / product.getMinStock());
    }
}
