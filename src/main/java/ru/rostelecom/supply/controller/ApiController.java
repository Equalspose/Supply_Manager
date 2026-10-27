package ru.rostelecom.supply.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rostelecom.supply.domain.Product;
import ru.rostelecom.supply.domain.Supplier;
import ru.rostelecom.supply.dto.DashboardStats;
import ru.rostelecom.supply.dto.LowStockItem;
import ru.rostelecom.supply.service.DashboardService;
import ru.rostelecom.supply.service.ProductService;
import ru.rostelecom.supply.service.StockService;
import ru.rostelecom.supply.service.SupplierService;

import java.util.List;

/** REST API (JSON) для интеграции с другими системами. */
@RestController
@RequestMapping("/api")
public class ApiController {

    private final DashboardService dashboardService;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final StockService stockService;

    public ApiController(DashboardService dashboardService, ProductService productService,
                         SupplierService supplierService, StockService stockService) {
        this.dashboardService = dashboardService;
        this.productService = productService;
        this.supplierService = supplierService;
        this.stockService = stockService;
    }

    @GetMapping("/dashboard")
    public DashboardStats dashboard() {
        return dashboardService.stats();
    }

    @GetMapping("/products")
    public List<Product> products() {
        return productService.findAll();
    }

    @GetMapping("/suppliers")
    public List<Supplier> suppliers() {
        return supplierService.findActive();
    }

    @GetMapping("/stock/low")
    public List<LowStockItem> lowStock() {
        return stockService.lowStock();
    }
}
