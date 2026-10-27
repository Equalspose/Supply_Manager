package ru.rostelecom.supply.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rostelecom.supply.domain.Product;
import ru.rostelecom.supply.domain.Stock;
import ru.rostelecom.supply.service.BusinessException;
import ru.rostelecom.supply.service.ProductService;
import ru.rostelecom.supply.service.StockService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    private final StockService stockService;

    public ProductController(ProductService service, StockService stockService) {
        this.service = service;
        this.stockService = stockService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) Long categoryId,
                       Model model) {
        List<Product> products = service.search(q, categoryId);
        Map<Long, Integer> totals = stockService.search(null, categoryId, null).stream()
                .collect(Collectors.groupingBy(s -> s.getProduct().getId(), Collectors.summingInt(Stock::getQuantity)));
        model.addAttribute("products", products);
        model.addAttribute("totals", totals);
        model.addAttribute("categories", service.categories());
        model.addAttribute("q", q);
        model.addAttribute("categoryId", categoryId);
        return "products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", service.categories());
        return "products/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.get(id));
        model.addAttribute("categories", service.categories());
        return "products/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("product") Product product, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categories", service.categories());
            return "products/form";
        }
        try {
            Product saved = service.save(product);
            ra.addFlashAttribute("success", "Товар «" + saved.getName() + "» сохранён");
            return "redirect:/products";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", service.categories());
            return "products/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Товар удалён");
        } catch (BusinessException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products";
    }
}
