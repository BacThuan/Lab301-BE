package com.application.backend.service;

import com.application.backend.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
//    Map<String, Object> getProducts(Integer page, Integer limit);
//
//
//    Map<String, Object> getSearching(String searching, Integer page, Integer limit);

    Map<String, Object> getFilter(String brand, String category, String gender, String searching, Integer page, Integer limit);

    Map<String, Object> getProduct(long id);

    Product addProduct(Product product);

    Product findById(long id);

    void deleteProduct(long id);

    List<Object> getEightHighestProduct();

    Map<String, Object> getPopupItem(long itemId);

    List<Object> getShopProduct(int page, String brand, String category, String color, Long gt, Long lt, String sort, String product);

    Map<String, Object> getProductDetails(long productId);

    List<Object> getRelated(long productId);

    Map<String, Object> findProductById(Long productId);
}
