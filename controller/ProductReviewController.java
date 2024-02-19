package com.application.backend.controller;


import com.application.backend.helper.Helper;
import com.application.backend.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ProductReviewController {
    @Autowired
    ProductReviewService productReviewService;

    @GetMapping("/public/products/review")
    public ResponseEntity<Object> getProduct(@RequestParam("productId") Long productId,
                                             @RequestParam("page") Integer page) {

        List<Object> data = productReviewService.getReview(productId, page);
        return Helper.res(data, HttpStatus.OK);

    }

    @PostMapping("/users/comment")
    public ResponseEntity<Object> userComment (@RequestParam("productId") Long productId,
                                             @RequestParam("email")  String email,
                                             @RequestParam("rating")  Integer rating,
                                             @RequestParam("comment")  String comment){

        productReviewService.createComment(productId, email, rating, comment);
        return ResponseEntity.ok("Success!");
    }
}
