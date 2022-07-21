package ru.ulstu.is.sbapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    @NotBlank
    @Size(min = 3, max = 64)
    private String name;

    @Column(unique = false, length = 64)
    @NotBlank
    @Size(min = 3, max = 1000)
    private String description;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "request_type_id")
    private RequestType type;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Order> orders;

    @Version
    private Integer version;

//    public Request(String name, String description, double price, RequestType type, List<Order> orders) {
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.type = type;
//        this.orders = orders;
//    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(id, request.id) && Objects.equals(name, request.name) && Objects.equals(price, request.price) && Objects.equals(description, request.description) && Objects.equals(type, request.type) && Objects.equals(orders, request.orders);
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, price);
    }  //была цена

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", type=" + type +
                ", orders=" + orders.stream()
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
