package ru.ulstu.is.sbapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.dto.RequestTypeDto;
import ru.ulstu.is.sbapp.exception.RequestTypeNotFoundException;
import ru.ulstu.is.sbapp.model.Request;
import ru.ulstu.is.sbapp.repository.RequestRepository;
import ru.ulstu.is.sbapp.service.RequestTypeService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/types")
public class RequestTypeMvcController {

    private final RequestTypeService typeService;
    private final RequestRepository requestRepository;

    public RequestTypeMvcController(RequestTypeService typeService, RequestRepository requestRepository) {
        this.typeService = typeService;
        this.requestRepository = requestRepository;
    }

    @GetMapping
    public String getRequestTypes(Model model) {
        model.addAttribute("types", typeService.getAllRequestTypes());
        return "types";
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String editRequestType(@PathVariable(required = false) Long id, Model model) {
        if (id == null || id <= 0) {
            model.addAttribute("typeDto", new RequestTypeDto());
        } else {
            model.addAttribute("typeId", id);
            model.addAttribute("typeDto", typeService.getRequestType(id));
        }
        return "type-edit";
    }

    @PostMapping(value = {"", "/{id}"})
    public String saveRequestType(@PathVariable(required = false) Long id,
                                  @ModelAttribute @Valid RequestTypeDto typeDto,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "type-edit";
        }
//        List<Request> requests = new ArrayList<>();
//        if(typeDto.getRequests()!=null){
//            requests = typeDto.getRequests().stream()
//                    .map(ordering -> requestRepository.findById(ordering.getId())
//                            .orElseThrow(()-> new RequestTypeNotFoundException(ordering.getId()))).toList();
//        }

        List<Request> requests = Collections.emptyList();
        if(typeDto.getRequests()!=null){
            requests = typeDto.getRequests().stream()
                    .map(request -> requestRepository.findById(request)
                            .orElseThrow(()-> new RequestTypeNotFoundException(request))).toList();
        }


        if (id == null || id <= 0) {
            typeService.addRequestType(typeDto.getName(), typeDto.getPrice(), requests);
        } else {
            typeService.updateRequestType(id, typeDto);
        }
        return "redirect:/types";
    }

    @PostMapping("/delete/{id}")
    public String deleteRequestType(@PathVariable Long id) {
        typeService.deleteRequestType(id);
        return "redirect:/types";
    }
}
