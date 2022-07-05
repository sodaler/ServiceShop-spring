package ru.ulstu.is.sbapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.dto.RequestDto;
import ru.ulstu.is.sbapp.service.OrderService;
import ru.ulstu.is.sbapp.service.RequestService;
import ru.ulstu.is.sbapp.service.RequestTypeService;

import javax.validation.Valid;

@Controller
@RequestMapping("/requests")
public class RequestMvcController {

    private final RequestService requestService;

    private final RequestTypeService requestTypeService;

    private final OrderService orderService;

    public RequestMvcController(RequestService requestService, RequestTypeService requestTypeService, OrderService orderService) {
        this.requestService = requestService;
        this.requestTypeService = requestTypeService;
        this.orderService = orderService;
    }

    @GetMapping(value = {"/admin"})
    public String getRequests(Model model) {
        model.addAttribute("requests", requestService.getAllRequests());
        return "requests-admin";
    }

    @GetMapping(value = {"/admin/edit", "/admin/edit/{id}"})
    public String editRequest(@PathVariable(required = false) Long id, Model model) {
        if (id == null || id <= 0) {
            model.addAttribute("requestDto", new RequestDto());
        } else {
            model.addAttribute("requestId", id);
            model.addAttribute("requestDto", requestService.getRequest(id));
        }
        model.addAttribute("requestTypes", requestTypeService.getAllRequestTypes());
        return "request-edit-admin";
    }

    @PostMapping(value = {"/admin", "/admin/{id}"})
    public String saveRequest(@PathVariable(required = false) Long id,
                              @ModelAttribute @Valid RequestDto requestDto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("requestTypes", requestTypeService.getAllRequestTypes());
            model.addAttribute("requests", orderService.getAllOrders());
            return "request-edit-admin";
        }
        if (id == null || id <= 0) {
            requestService.addRequest(requestDto);
        } else {
            requestService.updateRequest(id, requestDto);
        }
        return "redirect:/requests/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return "redirect:/requests/admin";
    }
}
