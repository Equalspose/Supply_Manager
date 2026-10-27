package ru.rostelecom.supply.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.rostelecom.supply.domain.Category;
import ru.rostelecom.supply.domain.Product;
import ru.rostelecom.supply.repository.CategoryRepository;
import ru.rostelecom.supply.repository.ProductRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> search(String q, Long categoryId) {
        return repository.search(StringUtils.hasText(q) ? q.trim() : "", categoryId);
    }

    public List<Product> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    public List<Category> categories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public Product get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Товар", id));
    }

    @Transactional
    public Product save(Product product) {
        product.setSku(product.getSku().trim().toUpperCase());
        boolean duplicate = product.getId() == null
                ? repository.existsBySku(product.getSku())
                : repository.existsBySkuAndIdNot(product.getSku(), product.getId());
        if (duplicate) {
            throw new BusinessException("Товар с артикулом " + product.getSku() + " уже существует");
        }
        return repository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.delete(get(id));
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Нельзя удалить товар, который используется в заказах");
        }
    }
}
