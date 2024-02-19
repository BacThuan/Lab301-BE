package com.application.backend.document;


import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

@Document(collection = "discountCode")
public class DiscountCode {
    @Id
    private String id;
    @Field("name")
    private String name;

    @Field("discountPercent")
    private Integer discountPercent;

    @Field("orderFrom")
    private Long orderFrom;

    @Field("unusedCodes")
    private Integer unusedCodes;

    @Field("dayStart")
    private Date dayStart;

    @Field("dayEnd")
    private Date dayEnd;

    @Field("isActive")
    private Boolean isActive;

    public DiscountCode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Long getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(Long orderFrom) {
        this.orderFrom = orderFrom;
    }


    public Date getDayStart() {
        return dayStart;
    }

    public void setDayStart(Date dayStart) {
        this.dayStart = dayStart;
    }

    public Date getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(Date dayEnd) {
        this.dayEnd = dayEnd;
    }

    public Integer getUnusedCodes() {
        return unusedCodes;
    }

    public void setUnusedCodes(Integer unusedCodes) {
        this.unusedCodes = unusedCodes;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}
