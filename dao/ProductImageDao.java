package com.application.backend.dao;

import com.application.backend.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageDao extends JpaRepository<ProductImage, Long> {

    ProductImage findById(long id);
}
