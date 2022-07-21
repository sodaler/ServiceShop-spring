package ru.ulstu.is.sbapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.sbapp.dto.OrderDto;
import ru.ulstu.is.sbapp.exception.ResourceNotFoundException;
import ru.ulstu.is.sbapp.model.Order;
import ru.ulstu.is.sbapp.repository.OrderRepository;
import ru.ulstu.is.sbapp.repository.UserRepository;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private ValidatorUtil validatorUtil;

    @Transactional
    public OrderDto addOrder(OrderDto orderDto) {
        Order order = new Order();
        mapDtoToEntity(orderDto, order);
        validatorUtil.validate(order);
        Order savedOrder = orderRepository.save(order);
        return mapEntityToDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderDto getOrder(Long id) {
        return mapEntityToDto(orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order with id=" + id + " not found")));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> {
            OrderDto orderDto = mapEntityToDto(order);
            orderDtos.add(orderDto);
        });
        return orderDtos;
    }

    @Transactional
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        return orderRepository.findById(id).map(order -> {
            mapDtoToEntity(orderDto, order);
            return mapEntityToDto(orderRepository.save(order));
        }).orElseThrow(() -> new ResourceNotFoundException("Order with id=" + id + " not found"));
    }

    @Transactional
    public String deleteOrder(Long id) {
        return orderRepository.findById(id).map(order -> {
            orderRepository.deleteById(order.getId());
            return "Order with id=" + id + " successfully deleted";
        }).orElseThrow(() -> new ResourceNotFoundException("Order with id=" + id + " not found"));
    }

    @Transactional
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

    private void mapDtoToEntity(OrderDto orderDto, Order order) {
        order.setUserAddress(orderDto.getUserAddress());
        order.setUserPhone(orderDto.getUserPhone());
        order.setAmount(orderDto.getAmount());
        order.setCreationDate(orderDto.getCreationDate());
        order.setUser(userRepository.findOneByLoginIgnoreCase(orderDto.getLogin()));
    }

    private OrderDto mapEntityToDto(Order order) {
        OrderDto responseDto = new OrderDto();
        responseDto.setId(order.getId());
        responseDto.setLogin(order.getUser().getLogin());
        responseDto.setUserAddress(order.getUserAddress());
        responseDto.setUserPhone(order.getUserPhone());
        responseDto.setCreationDate(order.getCreationDate());
        responseDto.setAmount(order.getAmount());
        return responseDto;
    }
}
