package com.application.backend.service;

import com.application.backend.model.ProductImage;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface ProductItemService {


    Map<String, Object> getFilter(Long idProduct, String searching, Integer quantity, Integer size, String state, String color, Long lt, Long gt, Integer page, Integer limit);

    void addItem(Long price, Integer quantity, Integer size, String color, String state, long productId, List<ProductImage> listImages, Integer primaryImage);

    void deleteItem(long id) throws MalformedURLException;

    Map<String, Object> getItem(long id);

    void updateItem(Long price, Integer quantity, Integer size, String color, String state, long itemId, List<ProductImage> listImages, List<String> reusedImages, Integer primaryImage, Boolean deletePreImg) throws MalformedURLException;

    Map<String, Object> findById(Long itemId);
}
