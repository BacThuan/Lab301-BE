package com.application.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="user_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    @Column(name="Email")
    private String email;


    @Column(name="Address")
    private String address;


    @Column(name="User_Name")
    private String name;

    @Column(name="Phone")
    private String phone;

    @Column(name = "total")
    private long total;

    @Column(name="method")
    private String method;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "payment_state_id")
    private State paymentState;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "delivery_state_id")
    private State deliveryState;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy="order",fetch = FetchType.EAGER,
            cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
    private List<OrderDetail> orderDetails;


    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public State getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(State paymentState) {
        this.paymentState = paymentState;
    }

    public State getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(State deliveryState) {
        this.deliveryState = deliveryState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addOrderDetails(OrderDetail orderDetail) {
        if(orderDetails == null) {
            orderDetails = new ArrayList<>();
        }
        orderDetails.add(orderDetail);
    }
}
