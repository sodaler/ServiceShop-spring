package ru.ulstu.is.sbapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.sbapp.dto.OrderDetailDto;
import ru.ulstu.is.sbapp.exception.ResourceNotFoundException;
import ru.ulstu.is.sbapp.model.OrderDetail;
import ru.ulstu.is.sbapp.repository.OrderDetailsRepository;
import ru.ulstu.is.sbapp.repository.OrderRepository;
import ru.ulstu.is.sbapp.repository.UserRepository;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderDetailsService {

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderDetailsRepository orderDetailsRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private ValidatorUtil validatorUtil;

    @Transactional
    public OrderDetailDto addOrderDetail(OrderDetailDto orderDetailsDto) {
        OrderDetail orderDetail = new OrderDetail();
        mapDtoToEntity(orderDetailsDto, orderDetail);
        validatorUtil.validate(orderDetail);
        OrderDetail savedOrderDetails = orderDetailsRepository.save(orderDetail);
        return mapEntityToDto(savedOrderDetails);
    }

    @Transactional(readOnly = true)
    public OrderDetailDto getOrderDetail(Long id) {
        return mapEntityToDto(orderDetailsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderDetails with id=" + id + " not found")));
    }

    @Transactional(readOnly = true)
    public List<OrderDetailDto> getAllOrderDetails() {
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        List<OrderDetail> orderDetails = orderDetailsRepository.findAll();
        orderDetails.forEach(orderDetail -> {
            OrderDetailDto orderDetailsDto = mapEntityToDto(orderDetail);
            orderDetailDtos.add(orderDetailsDto);
        });
        return orderDetailDtos;
    }

    @Transactional
    public OrderDetailDto updateOrderDetails(Long id, OrderDetailDto orderDetailDto) {
        return orderDetailsRepository.findById(id).map(orderDetail -> {
            mapDtoToEntity(orderDetailDto, orderDetail);
            return mapEntityToDto(orderDetailsRepository.save(orderDetail));
        }).orElseThrow(() -> new ResourceNotFoundException("OrderDetail with id=" + id + " not found"));
    }

    @Transactional
    public String deleteOrderDetails(Long id) {
        return orderDetailsRepository.findById(id).map(orderDetail -> {
            orderDetailsRepository.deleteById(orderDetail.getId());
            return "OrderDetail with id=" + id + " successfully deleted";
        }).orElseThrow(() -> new ResourceNotFoundException("OrderDetail with id=" + id + " not found"));
    }

    @Transactional
    public void deleteAllOrderDetails() {
        orderDetailsRepository.deleteAll();
    }

    private void mapDtoToEntity(OrderDetailDto orderDetailDto, OrderDetail orderDetail) {
        orderDetail.setQuanity(orderDetailDto.getRequestQuantity());
        Optional<OrderDetail> orderDetailsFind = orderDetailsRepository.findById(orderDetailDto.getId());
        orderDetail.setOrder(orderDetailsFind.get().getOrder());
        orderDetail.setRequest(orderDetailsFind.get().getRequest());
    }

    private OrderDetailDto mapEntityToDto(OrderDetail orderDetail) {
        OrderDetailDto responseDto = new OrderDetailDto();
        responseDto.setId(orderDetail.getId());
        responseDto.setRequestId(orderDetail.getRequest().getId());
        responseDto.setRequestName(orderDetail.getRequest().getName());
        responseDto.setRequestQuantity(orderDetail.getQuanity());
//        responseDto.setRequestPrice(orderDetail.getRequest().getPrice());
        responseDto.setRequestAmount(responseDto.getRequestPrice() * responseDto.getRequestQuantity());
        return responseDto;
    }

//    public List<OrderDetail> findByNameContaining(String cityName) {
//        return orderDetailsRepository.findByNameContaining(cityName).stream()
//                .map(OrderDetailDto::new)
//                .collect(Collectors.toList());
//    }
}
