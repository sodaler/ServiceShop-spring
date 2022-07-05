package ru.ulstu.is.sbapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.sbapp.dto.RequestTypeDto;
import ru.ulstu.is.sbapp.exception.RequestTypeNotFoundException;
import ru.ulstu.is.sbapp.exception.ResourceNotFoundException;
import ru.ulstu.is.sbapp.model.Request;
import ru.ulstu.is.sbapp.model.RequestType;
import ru.ulstu.is.sbapp.repository.RequestRepository;
import ru.ulstu.is.sbapp.repository.RequestTypeRepository;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RequestTypeService {

    @Resource
    private RequestTypeRepository requestTypeRepository;

    @Resource
    private RequestRepository requestRepository;

    @Resource
    private ValidatorUtil validatorUtil;

    @Transactional
    public RequestType addRequestType(String discountName, double price, List<Request> requests) {
        final RequestType discount = new RequestType(discountName, price, requests);
        validatorUtil.validate(discount);
        return requestTypeRepository.save(discount);
    }

//    @Transactional
//    public RequestTypeDto addRequestType(RequestTypeDto requestTypeDto) {
//        RequestType requestType = new RequestType();
//        mapDtoToEntity(requestTypeDto, requestType);
//        RequestType savedRequestType = requestTypeRepository.save(requestType);
//        return mapEntityToDto(savedRequestType);
//    }

    @Transactional
    public RequestTypeDto addRequestType(RequestType typeDto) {
        String requestTypeName = typeDto.getName();
        double percent = typeDto.getPrice();
        List<Request> requests = Collections.emptyList();
        if(typeDto.getRequests()!=null){
            requests = typeDto.getRequests().stream()
                    .map(ordering -> requestRepository.findById(ordering.getId())
                            .orElseThrow(()-> new RequestTypeNotFoundException(ordering.getId()))).toList();
        }
        return new RequestTypeDto(this.addRequestType(requestTypeName, percent, requests));
    }


    @Transactional(readOnly = true)
    public RequestTypeDto getRequestType(Long id) {
        return mapEntityToDto(requestTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RequestType with id=" + id + " not found")));
    }

    @Transactional(readOnly = true)
    public List<RequestTypeDto> getAllRequestTypes() {
        List<RequestTypeDto> requestTypeDtos = new ArrayList<>();
        List<RequestType> requestTypes = requestTypeRepository.findAll();
        requestTypes.forEach(requestType -> {
            RequestTypeDto requestTypeDto = mapEntityToDto(requestType);
            if (requestTypeDto.getName() != null && !Objects.equals(requestTypeDto.getName(), "")) {
                requestTypeDtos.add(requestTypeDto);
            }
        });
        return requestTypeDtos;
    }

    @Transactional
    public RequestTypeDto updateRequestType(Long id, RequestTypeDto requestTypeDto) {
        return requestTypeRepository.findById(id).map(requestType -> {
            mapDtoToEntity(requestTypeDto, requestType);
            return mapEntityToDto(requestTypeRepository.save(requestType));
        }).orElseThrow(() -> new ResourceNotFoundException("RequestType with id=" + id + " not found"));
    }

    @Transactional
    public String deleteRequestType(Long id) {
        return requestTypeRepository.findById(id).map(requestType -> {
            requestTypeRepository.deleteById(requestType.getId());
            return "RequestType with id=" + id + " successfully deleted";
        }).orElseThrow(() -> new ResourceNotFoundException("RequestType with id=" + id + " not found"));
    }

    private void mapDtoToEntity(RequestTypeDto requestTypeDto, RequestType requestType) {
        requestType.setName(requestTypeDto.getName());
        requestType.setPrice(requestTypeDto.getPrice());
    }

    private RequestTypeDto mapEntityToDto(RequestType requestType) {
        RequestTypeDto responseDto = new RequestTypeDto();
        responseDto.setId(requestType.getId());
        responseDto.setName(requestType.getName());
        responseDto.setPrice(requestType.getPrice());
        return responseDto;
    }
}
