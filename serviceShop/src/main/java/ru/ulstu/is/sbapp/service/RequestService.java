package ru.ulstu.is.sbapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.sbapp.dto.RequestDto;
import ru.ulstu.is.sbapp.exception.OrderNotFoundException;
import ru.ulstu.is.sbapp.exception.ResourceNotFoundException;
import ru.ulstu.is.sbapp.model.Order;
import ru.ulstu.is.sbapp.model.Request;
import ru.ulstu.is.sbapp.model.RequestType;
import ru.ulstu.is.sbapp.repository.OrderRepository;
import ru.ulstu.is.sbapp.repository.RequestRepository;
import ru.ulstu.is.sbapp.repository.RequestTypeRepository;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class RequestService {

    @Resource
    private RequestRepository requestRepository;

    @Resource
    private RequestTypeRepository requestTypeRepository;

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private ValidatorUtil validatorUtil;

    @Transactional
    public RequestDto addRequest(RequestDto requestDto) {
        Request request = new Request();
        mapDtoToEntity(requestDto, request);
        validatorUtil.validate(request);
        Request savedRequest = requestRepository.save(request);
        return mapEntityToDto(savedRequest);
    }

//    @Transactional
//    public Request addRequest(String name, String description, double price, RequestType types, List<Order> orders) {
//        final Request request = new Request(name, description, price, types, orders);
//        validatorUtil.validate(request);
//        return requestRepository.save(request);
//    }
//
//    @Transactional
//    public RequestDto addRequest(RequestDto discountDto) {
//        String discountName = discountDto.getName();
//        String description = discountDto.getDescription();
//        double price = discountDto.getPrice();
//        String type = discountDto.getType();
//        List<Order> orderings = Collections.emptyList();
//        if(discountDto.getOrders()!=null){
//            orderings = discountDto.getOrders().stream()
//                    .map(ordering -> orderRepository.findById(ordering.getId())
//                            .orElseThrow(()-> new OrderNotFoundException(ordering.getId()))).toList();
//        }
//        return new RequestDto(addRequest(discountName, description, price, type, orderings));
//    }

    @Transactional(readOnly = true)
    public RequestDto getRequest(Long id) {
        return mapEntityToDto(requestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Request with id=" + id + " not found")));
    }

    @Transactional(readOnly = true)
    public RequestDto getRequestByName(String name) {
        return mapEntityToDto(requestRepository.findRequestByName(name));
    }

    @Transactional(readOnly = true)
    public List<RequestDto> getAllRequests() {
        List<RequestDto> requestDtos = new ArrayList<>();
        List<Request> requests = requestRepository.findAll();
        requests.forEach(request -> {
            RequestDto requestDto = mapEntityToDto(request);
            if (requestDto.getName() != null && !Objects.equals(requestDto.getName(), "")) {
                requestDtos.add(requestDto);
            }
        });
        return requestDtos;
    }

    @Transactional
    public RequestDto updateRequest(Long id, RequestDto requestDto) {
        return requestRepository.findById(id).map(request -> {
            mapDtoToEntity(requestDto, request);
            return mapEntityToDto(requestRepository.save(request));
        }).orElseThrow(() -> new ResourceNotFoundException("Request with id=" + id + " not found"));
    }

    @Transactional
    public String deleteRequest(Long id) {
        return requestRepository.findById(id).map(request -> {
            requestRepository.deleteById(request.getId());
            return "Request with id=" + id + " successfully deleted";
        }).orElseThrow(() -> new ResourceNotFoundException("Request with id=" + id + " not found"));
    }

    @Transactional
    public void deleteAllRequests() {
        requestRepository.deleteAll();
    }

    private void mapDtoToEntity(RequestDto requestDto, Request request) {
        request.setName(requestDto.getName());
        request.setDescription(requestDto.getDescription());
//        RequestType price = requestTypeRepository.findRequestTypeByPrice(requestDto.getType());
//        request.setPrice(requestDto.getPrice());
        RequestType price = requestTypeRepository.findRequestTypeByName(requestDto.getType());
        RequestType type = requestTypeRepository.findRequestTypeByName(requestDto.getType());
        List<Order> orderings = Collections.emptyList();
        if(requestDto.getOrders()!=null){
            orderings = request.getOrders().stream()
                    .map(ordering -> orderRepository.findById(ordering.getId())
                            .orElseThrow(()-> new OrderNotFoundException(ordering.getId()))).toList();
        }
//        List<Order> order = orderRepository.findAll();
        request.setOrders(orderings);
        request.setPrice(price.getPrice());
        request.setType(type);
    }

    private RequestDto mapEntityToDto(Request request) {
        RequestDto responseDto = new RequestDto();
        responseDto.setId(request.getId());
        responseDto.setName(request.getName());
        responseDto.setDescription(request.getDescription());
        responseDto.setPrice(request.getType().getPrice());
        responseDto.setType(request.getType().getName());
        responseDto.setOrders(request.getOrders());
        return responseDto;
    }
}
