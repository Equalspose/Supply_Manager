package ru.rostelecom.supply.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rostelecom.supply.domain.Warehouse;
import ru.rostelecom.supply.repository.StockRepository;
import ru.rostelecom.supply.repository.WarehouseRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WarehouseService {

    private final WarehouseRepository repository;
    private final StockRepository stockRepository;

    public WarehouseService(WarehouseRepository repository, StockRepository stockRepository) {
        this.repository = repository;
        this.stockRepository = stockRepository;
    }

    public List<Warehouse> findAll() {
        return repository.findAllByOrderByIdAsc();
    }

    public Warehouse get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Склад", id));
    }

    /** Заполненность складов в процентах (по количеству единиц). */
    public Map<Long, Integer> occupancy() {
        Map<Long, Integer> result = new LinkedHashMap<>();
        for (Warehouse w : findAll()) {
            long total = stockRepository.totalQuantityByWarehouse(w.getId());
            result.put(w.getId(), (int) Math.min(100, total * 100 / Math.max(1, w.getCapacity())));
        }
        return result;
    }

    public long totalQuantity(Long warehouseId) {
        return stockRepository.totalQuantityByWarehouse(warehouseId);
    }

    @Transactional
    public Warehouse save(Warehouse warehouse) {
        warehouse.setCode(warehouse.getCode().trim().toUpperCase());
        boolean duplicate = warehouse.getId() == null
                ? repository.existsByCode(warehouse.getCode())
                : repository.existsByCodeAndIdNot(warehouse.getCode(), warehouse.getId());
        if (duplicate) {
            throw new BusinessException("Склад с кодом " + warehouse.getCode() + " уже существует");
        }
        return repository.save(warehouse);
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.delete(get(id));
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Нельзя удалить склад, на который оформлены заказы");
        }
    }
}
