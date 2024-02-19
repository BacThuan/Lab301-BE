package com.application.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="product_item")
public class ProductItem {
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
    @Column(name = "size")
    private int size;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private long price;

    @Column(name = "primary_image")
    private String primaryImage;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "color_id")
    private Color color;


    @OneToMany(mappedBy="ProductItem",fetch = FetchType.EAGER,
            cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
    private List<ProductImage> images;

    @OneToMany(mappedBy="ProductItem",fetch = FetchType.EAGER,
            cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
    private List<Cart> carts;

    @OneToMany(mappedBy="ProductItem",fetch = FetchType.EAGER,
            cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
    private List<OrderDetail> orderDetails;

    public ProductItem() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public void addImage(ProductImage image) {
        if(images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
    }

    public void addCarts(Cart cart) {
        if(carts == null) {
            carts = new ArrayList<>();
        }
        carts.add(cart);
    }

    public void addOrderDetails(OrderDetail orderDetail) {
        if(orderDetails == null) {
            orderDetails = new ArrayList<>();
        }
        orderDetails.add(orderDetail);
    }
}
