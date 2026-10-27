package ru.rostelecom.supply.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.rostelecom.supply.domain.Supplier;
import ru.rostelecom.supply.repository.SupplierRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SupplierService {

    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public List<Supplier> search(String q, Boolean active) {
        return repository.search(StringUtils.hasText(q) ? q.trim() : "", active);
    }

    public List<Supplier> findActive() {
        return repository.findByActiveTrueOrderByNameAsc();
    }

    public Supplier get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Поставщик", id));
    }

    @Transactional
    public Supplier save(Supplier supplier) {
        boolean duplicate = supplier.getId() == null
                ? repository.existsByInn(supplier.getInn())
                : repository.existsByInnAndIdNot(supplier.getInn(), supplier.getId());
        if (duplicate) {
            throw new BusinessException("Поставщик с ИНН " + supplier.getInn() + " уже зарегистрирован");
        }
        if (supplier.getId() != null) {
            supplier.setCreatedAt(get(supplier.getId()).getCreatedAt());
        }
        return repository.save(supplier);
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.delete(get(id));
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Нельзя удалить поставщика, по которому есть заказы. Сделайте его неактивным.");
        }
    }
}
