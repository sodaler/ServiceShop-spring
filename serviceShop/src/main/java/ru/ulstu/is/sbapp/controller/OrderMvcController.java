package ru.ulstu.is.sbapp.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.dto.OrderDto;
import ru.ulstu.is.sbapp.dto.RequestDto;
import ru.ulstu.is.sbapp.model.User;
import ru.ulstu.is.sbapp.model.UserRole;
import ru.ulstu.is.sbapp.service.OrderService;
import ru.ulstu.is.sbapp.service.RequestService;
import ru.ulstu.is.sbapp.service.RequestTypeService;
import ru.ulstu.is.sbapp.service.UserService;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/orders")
public class OrderMvcController {

    private final UserService userService;

    private final OrderService orderService;

    private final RequestService requestService;

    private final RequestTypeService requestTypeService;

    public OrderMvcController(UserService userService, OrderService orderService, RequestService requestService, RequestTypeService requestTypeService) {
        this.userService = userService;
        this.orderService = orderService;
        this.requestService = requestService;
        this.requestTypeService = requestTypeService;
    }

    @GetMapping
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String editOrder(@PathVariable(required = false) Long id, Model model) {
        if (id == null || id <= 0) {
            model.addAttribute("orderDto", new OrderDto());
        } else {
            model.addAttribute("orderId", id);
            model.addAttribute("orderDto", orderService.getOrder(id));
        }
        model.addAttribute("requests", requestService.getAllRequests());
        return "orders-edit";
    }

    @PostMapping(value = {"", "/{id}"})
    public String saveOrder(@PathVariable(required = false) Long id,
                            @ModelAttribute @Valid OrderDto orderDto,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("requests", requestService.getAllRequests());
            return "orders-edit";
        }
        orderDto.setCreationDate(new Date());

        double amount = 0;
        for (int i = 0; i < orderDto.getRequests().size(); i++) {
            String requestName = orderDto.getRequests().get(i);
            RequestDto request = requestService.getRequestByName(requestName);
            amount += request.getPrice();
        }

        orderDto.setAmount(amount);

        String login = userService.findLoggedInUserName();
        orderDto.setLogin(login);

        if (id == null || id <= 0) {
            orderService.addOrder(orderDto);
        } else {
            orderService.updateOrder(id, orderDto);
        }
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}
