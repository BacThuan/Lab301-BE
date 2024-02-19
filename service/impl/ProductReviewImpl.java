package com.application.backend.service.impl;

import com.application.backend.dao.ProductDao;
import com.application.backend.dao.ProductReviewDao;
import com.application.backend.dao.UserDao;
import com.application.backend.model.Product;
import com.application.backend.model.ProductReview;
import com.application.backend.model.User;
import com.application.backend.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductReviewImpl implements ProductReviewService {

    @Autowired
    ProductReviewDao productReviewDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    UserDao userDao;

    @Override
    public List<Object> getReview(long productId, int page) {

        // take only 5 review per page
        int number = (page - 1) * 5;
        List<ProductReview> data = productReviewDao.getReview(productId, number);

        List<Object> result = new ArrayList<>();

        for(ProductReview review : data){
            Map<String, Object> temp = new HashMap<>();
            temp.put("email", review.getUser().getEmail());
            temp.put("comment", review.getContent());
            temp.put("rating", review.getRate());
            temp.put("day", review.getUpdatedAt());

            result.add(temp);
        }

        return result;
    }

    @Override
    public void createComment(long productId, String email, Integer rating, String comment) {
        Product product = productDao.findById(productId);

        User user = userDao.findByEmail(email);

        ProductReview review = new ProductReview();

        review.setRate(rating);
        review.setContent(comment);
        review.setProduct(product);
        review.setUser(user);

        ProductReview result = productReviewDao.save(review);

        user.addReview(result);
        product.addReview(result);

        userDao.save(user);
        productDao.save(product);
    }
}
