package com.application.backend.dao;

import com.application.backend.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductReviewDao extends JpaRepository<ProductReview, Long> {
    @Query(value="select * from product_review where product_id = ?1 order by updated_at desc limit 5 offset ?2" ,nativeQuery = true)
    List<ProductReview> getReview(long productId, int number);
}
