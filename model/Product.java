package com.application.backend.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name="name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name="short_desc", columnDefinition = "LONGTEXT")
//    @Column(name="short_desc") // test
    @Lob
    private String shortDesc;

    @Column(name="long_desc", columnDefinition = "LONGTEXT")
//    @Column(name="long_desc") //test
    @Lob
    private String longDesc;



    @OneToMany(mappedBy="product",fetch = FetchType.EAGER,
            cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
    private List<ProductItem> ProductItem;

    @OneToMany(mappedBy="product",fetch = FetchType.EAGER,
            cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
    private List<ProductReview> reviews;


    //mapping
    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name="brand_id")
    private Brand brand;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name="category_id")
    private Category category;

    public Product() {
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

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }



    public List<ProductItem> getProductItem() {
        return ProductItem;
    }

    public void setProductItem(List<ProductItem> ProductItem) {
        this.ProductItem = ProductItem;
    }

    public List<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReview> reviews) {
        this.reviews = reviews;
    }



    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public void addItem(ProductItem Item) {
        if(ProductItem == null) {
            ProductItem = new ArrayList<>();
        }
        ProductItem.add(Item);
    }

    public void addReview(ProductReview review) {
        if(reviews == null) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }

}
