package com.application.backend.service;

import java.util.List;

public interface ProductReviewService {
    List<Object> getReview(long productId, int page);

    void createComment(long productId, String email, Integer rating, String comment);
}
