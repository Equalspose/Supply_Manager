package ru.rostelecom.supply.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.rostelecom.supply.domain.Product;
import ru.rostelecom.supply.domain.Stock;
import ru.rostelecom.supply.domain.Warehouse;
import ru.rostelecom.supply.dto.LowStockItem;
import ru.rostelecom.supply.repository.StockRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository repository;

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    public List<Stock> search(Long warehouseId, Long categoryId, String q) {
        return repository.search(warehouseId, categoryId, StringUtils.hasText(q) ? q.trim() : "");
    }

    public List<Stock> byWarehouse(Long warehouseId) {
        return repository.findByWarehouseIdOrderByProductNameAsc(warehouseId);
    }

    public List<LowStockItem> lowStock() {
        return repository.findLowStock().stream()
                .map(row -> new LowStockItem((Product) row[0], ((Number) row[1]).longValue()))
                .toList();
    }

    public BigDecimal totalValue() {
        return repository.totalStockValue();
    }

    /** Оприходование товара на склад. */
    @Transactional
    public void receive(Product product, Warehouse warehouse, int quantity) {
        Stock stock = repository.findByProductAndWarehouse(product, warehouse)
                .orElseGet(() -> new Stock(product, warehouse, 0));
        stock.add(quantity);
        repository.save(stock);
    }
}
