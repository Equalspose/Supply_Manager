package ru.rostelecom.supply.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rostelecom.supply.domain.Warehouse;
import ru.rostelecom.supply.service.BusinessException;
import ru.rostelecom.supply.service.StockService;
import ru.rostelecom.supply.service.WarehouseService;

@Controller
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService service;
    private final StockService stockService;

    public WarehouseController(WarehouseService service, StockService stockService) {
        this.service = service;
        this.stockService = stockService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("warehouses", service.findAll());
        model.addAttribute("occupancy", service.occupancy());
        return "warehouses/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("warehouse", service.get(id));
        model.addAttribute("stock", stockService.byWarehouse(id));
        model.addAttribute("totalQuantity", service.totalQuantity(id));
        return "warehouses/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("warehouse", new Warehouse());
        return "warehouses/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("warehouse", service.get(id));
        return "warehouses/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("warehouse") Warehouse warehouse, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "warehouses/form";
        }
        try {
            Warehouse saved = service.save(warehouse);
            ra.addFlashAttribute("success", "Склад «" + saved.getName() + "» сохранён");
            return "redirect:/warehouses/" + saved.getId();
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "warehouses/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Склад удалён");
            return "redirect:/warehouses";
        } catch (BusinessException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouses/" + id;
        }
    }
}
