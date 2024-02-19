package com.application.backend.document;

import com.application.backend.model.Product;
import com.application.backend.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "favouriteList")
public class FavouriteList {
    @Id
    private String id;

    @Field("email")
    private String email;

    @Field("contents")
    private List<Long> listProductId;


    public FavouriteList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getListProductId() {
        return listProductId;
    }

    public void setListProductId(List<Long> listProductId) {
        this.listProductId = listProductId;
    }

    public void addFavour(Long productId){
        if(listProductId == null){
            listProductId = new ArrayList<>();
        }
        listProductId.add(productId);
    }
}
