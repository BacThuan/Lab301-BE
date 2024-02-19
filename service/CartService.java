package com.application.backend.service;

import java.util.List;
import java.util.Map;

public interface CartService {

    void addToCart(long itemId, String email, int count);

    List<Object> userGetCart(String email);

    void updateCart(long cartId, int count);

    void deleteCart(long cartId);

    Integer countCart(String email);

    List<Object> guestGetCart(List<Map<String, Object>> listData);

    void checkItems(List<Map<String, Object>> listData);
}
