package ru.rostelecom.supply.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rostelecom.supply.service.ProductService;
import ru.rostelecom.supply.service.StockService;
import ru.rostelecom.supply.service.WarehouseService;

@Controller
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;
    private final WarehouseService warehouseService;
    private final ProductService productService;

    public StockController(StockService stockService, WarehouseService warehouseService, ProductService productService) {
        this.stockService = stockService;
        this.warehouseService = warehouseService;
        this.productService = productService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long warehouseId,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) String q,
                       Model model) {
        model.addAttribute("stock", stockService.search(warehouseId, categoryId, q));
        model.addAttribute("warehouses", warehouseService.findAll());
        model.addAttribute("categories", productService.categories());
        model.addAttribute("warehouseId", warehouseId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("q", q);
        return "stock/list";
    }

    @GetMapping("/low")
    public String low(Model model) {
        model.addAttribute("items", stockService.lowStock());
        return "stock/low";
    }
}
