package ru.rostelecom.supply.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rostelecom.supply.domain.Supplier;
import ru.rostelecom.supply.repository.PurchaseOrderRepository;
import ru.rostelecom.supply.service.BusinessException;
import ru.rostelecom.supply.service.OrderService;
import ru.rostelecom.supply.service.SupplierService;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService service;
    private final OrderService orderService;
    private final PurchaseOrderRepository orderRepository;

    public SupplierController(SupplierService service, OrderService orderService, PurchaseOrderRepository orderRepository) {
        this.service = service;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) Boolean active,
                       Model model) {
        model.addAttribute("suppliers", service.search(q, active));
        model.addAttribute("q", q);
        model.addAttribute("active", active);
        return "suppliers/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", service.get(id));
        model.addAttribute("orders", orderService.bySupplier(id));
        model.addAttribute("totalAmount", orderRepository.sumAmountBySupplier(id));
        return "suppliers/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "suppliers/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", service.get(id));
        return "suppliers/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("supplier") Supplier supplier, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "suppliers/form";
        }
        try {
            Supplier saved = service.save(supplier);
            ra.addFlashAttribute("success", "Поставщик «" + saved.getName() + "» сохранён");
            return "redirect:/suppliers/" + saved.getId();
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "suppliers/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Поставщик удалён");
            return "redirect:/suppliers";
        } catch (BusinessException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/suppliers/" + id;
        }
    }
}
