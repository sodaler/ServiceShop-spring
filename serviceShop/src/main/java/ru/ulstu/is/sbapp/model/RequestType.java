package ru.ulstu.is.sbapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "request_types")
public class RequestType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    @NotBlank
    @Size(min = 3, max = 64)
    private String name;

    @Column(nullable = false)
    private double price;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "type",fetch = FetchType.LAZY)
//    @JoinColumn(name = "requests")
    private List<Request> requests = new ArrayList<>();

    public RequestType() {
    }

    public RequestType(String requestName, double price, List<Request> requests) {
        this.name = requestName;
        this.price = price;
        this.requests = requests;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestType that = (RequestType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "RequestType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", requests='" +  requests.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("\n")) + '\'' +
                '}';
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
