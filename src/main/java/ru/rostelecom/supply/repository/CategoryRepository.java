package ru.rostelecom.supply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rostelecom.supply.domain.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByNameAsc();
}
