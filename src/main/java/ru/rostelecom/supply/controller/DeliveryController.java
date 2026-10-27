package ru.rostelecom.supply.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rostelecom.supply.domain.Delivery;
import ru.rostelecom.supply.domain.DeliveryStatus;
import ru.rostelecom.supply.domain.PurchaseOrder;
import ru.rostelecom.supply.dto.DeliveryForm;
import ru.rostelecom.supply.service.BusinessException;
import ru.rostelecom.supply.service.DeliveryService;
import ru.rostelecom.supply.service.OrderService;

@Controller
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final OrderService orderService;

    public DeliveryController(DeliveryService deliveryService, OrderService orderService) {
        this.deliveryService = deliveryService;
        this.orderService = orderService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        model.addAttribute("deliveries", deliveryService.search(q,
                PageRequest.of(Math.max(0, page), 15, Sort.by("deliveryDate").descending().and(Sort.by("id").descending()))));
        model.addAttribute("awaiting", orderService.awaitingDelivery());
        model.addAttribute("q", q);
        return "deliveries/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("delivery", deliveryService.get(id));
        return "deliveries/view";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long orderId, Model model, RedirectAttributes ra) {
        if (orderId == null) {
            model.addAttribute("awaiting", orderService.awaitingDelivery());
            return "deliveries/select-order";
        }
        PurchaseOrder order = orderService.get(orderId);
        if (!order.getStatus().canReceive()) {
            ra.addFlashAttribute("error", "Заказ " + order.getNumber() + " не ожидает поставки");
            return "redirect:/orders/" + orderId;
        }
        return showForm(deliveryService.prepareForm(order), order, model);
    }

    @PostMapping
    public String receive(@Valid @ModelAttribute("form") DeliveryForm form, BindingResult result,
                          Model model, Authentication auth, RedirectAttributes ra) {
        PurchaseOrder order = orderService.get(form.getOrderId());
        if (result.hasErrors()) {
            return showForm(form, order, model);
        }
        try {
            Delivery delivery = deliveryService.receive(form, auth.getName());
            ra.addFlashAttribute("success", "Поставка " + delivery.getNumber() + " принята, остатки на складе обновлены");
            return "redirect:/deliveries/" + delivery.getId();
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return showForm(form, order, model);
        }
    }

    private String showForm(DeliveryForm form, PurchaseOrder order, Model model) {
        model.addAttribute("form", form);
        model.addAttribute("order", order);
        model.addAttribute("statuses", DeliveryStatus.values());
        return "deliveries/form";
    }
}
