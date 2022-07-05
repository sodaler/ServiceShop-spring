package ru.ulstu.is.sbapp.dto;

import ru.ulstu.is.sbapp.model.Request;
import ru.ulstu.is.sbapp.model.RequestType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestTypeDto {

    private Long id;

    private String name;

    private double price;

    private List<Long> requests;

    private List<String> requestsName;

    public RequestTypeDto(RequestType requestType) {
        this.id = requestType.getId();
        this.name = String.format("%s", requestType.getName());
        this.requests = new ArrayList<>();
        this.requestsName = new ArrayList<>();
        for (Request request : requestType.getRequests()) {
            this.requestsName.add(String.format("%s", request.getName()));
        }
        List<Request> requestList = requestType.getRequests();
        if(requestList != null) {
            for (Request request : requestList) {
                this.requests.add(request.getId());
            }
        }
        else {
            this.requests = Collections.emptyList();
        }
    }

    public RequestTypeDto() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Long> getRequests() {
        return requests;
    }

    public void setRequests(List<Long> requests) {
        this.requests = requests;
    }

    public List<String> getRequestsName() {
        return requestsName;
    }

    public void setRequestsName(List<String> requestsName) {
        this.requestsName = requestsName;
    }
}
