package com.application.backend.service;


import java.util.List;
import java.util.Map;

public interface OrderService {
    void addOrder( String orderEmail, String orderName, String orderPhone, String orderAddress, String orderMethod, Long orderTotal, String emailAccount, String discountCodeId);

    List<Object> userGetOrder(String email);

    Map<String, Object> getOrderDetails(long orderId);

    Map<String, Object> getOrders(int page, int limit, String deliveryState, String paymentState, String searching);

    void deleteOrder(long idOrder);

    List<Object> getItemInfo(List<Long> listIdItems);

    void updateOrder(long idOrder, Map<String, Object> info);

    void createOrder(Map<String, Object> info, List<Map<String, Object>> itemQuantity, long total);

    long countOrder(Integer year, Integer month);

    long totalEarning(Integer year, Integer month);

    List<Object> getHomeData(Integer year, Integer month);

    void successPayOrder(Long orderId);

    List<Long> getCharts(String type, Integer year);


    Map<String, Object> addGuestOrder(String orderEmail, String orderName, String orderPhone, String orderAddress, String orderMethod, long orderTotal, List<Map<String, Object>> dataOrder);

    Map<String, Object> findById(Long orders);
}
