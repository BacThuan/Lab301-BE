package com.application.backend.service;

import com.application.backend.document.DeliveryAddress;

import java.util.List;
import java.util.Map;

public interface DeliveryAddressService {
    void createShop(String city, String fastDeliveryCodeCity, String district, String fastDeliveryCodeDistrict, String ward, String fastDeliveryCodeWard, String address);

    void updateShop(String idDelivery, String city, String fastDeliveryCodeCity, String district, String fastDeliveryCodeDistrict, String ward, String fastDeliveryCodeWard, String address);

    Map<String, Object> getDeliveries(Integer page, Integer limit, String city, String district, String ward, String searching);

    Map<String, Object> getDelivery(String id);

    void deleteDelivery(String id);

    List<DeliveryAddress> getAll();
}
