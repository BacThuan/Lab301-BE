package com.application.backend.service.impl;

import com.application.backend.dao.mongdb.DeliveryAddressDao;
import com.application.backend.document.DeliveryAddress;
import com.application.backend.exception.CatchException;
import com.application.backend.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeliveryAddressImpl implements DeliveryAddressService {

    @Autowired
    DeliveryAddressDao deliveryAddressDao;

    @Override
    public void createShop(String city, String fastDeliveryCodeCity, String district, String fastDeliveryCodeDistrict, String ward, String fastDeliveryCodeWard, String address) {
        DeliveryAddress deliveryAddress = new DeliveryAddress();

        deliveryAddress.setCity(city);
        deliveryAddress.setFastDeliveryCodeCity(fastDeliveryCodeCity);
        deliveryAddress.setDistrict(district);
        deliveryAddress.setFastDeliveryCodeDistrict(fastDeliveryCodeDistrict);
        deliveryAddress.setWard(ward);
        deliveryAddress.setFastDeliveryCodeWard(fastDeliveryCodeWard);
        deliveryAddress.setAddress(address);

        deliveryAddressDao.save(deliveryAddress);
    }

    @Override
    public void updateShop(String idDelivery, String city, String fastDeliveryCodeCity, String district, String fastDeliveryCodeDistrict, String ward, String fastDeliveryCodeWard, String address) {
        Optional<DeliveryAddress> delivery = deliveryAddressDao.findById(idDelivery);

        if(delivery.isPresent()){
            DeliveryAddress deliveryAddress = delivery.get();
            deliveryAddress.setCity(city);
            deliveryAddress.setFastDeliveryCodeCity(fastDeliveryCodeCity);
            deliveryAddress.setDistrict(district);
            deliveryAddress.setFastDeliveryCodeDistrict(fastDeliveryCodeDistrict);
            deliveryAddress.setWard(ward);
            deliveryAddress.setFastDeliveryCodeWard(fastDeliveryCodeWard);
            deliveryAddress.setAddress(address);

            deliveryAddressDao.save(deliveryAddress);
        }

        else {
            throw new CatchException("Not Found!", HttpStatus.NOT_FOUND);
        }

    }


    public Map<String, Object> getListData(List<DeliveryAddress> deliveryAddressList, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, String>> datas = new ArrayList<>();

        for(DeliveryAddress deliveryAddress : deliveryAddressList){
            Map<String, String> data = new HashMap<>();

            data.put("_id",deliveryAddress.getId());
            data.put("city", deliveryAddress.getCity());
            data.put("fastDeliveryCodeCity", deliveryAddress.getFastDeliveryCodeCity());
            data.put("district", deliveryAddress.getDistrict());
            data.put("fastDeliveryCodeDistrict", deliveryAddress.getFastDeliveryCodeDistrict());
            data.put("ward", deliveryAddress.getWard());
            data.put("fastDeliveryCodeWard", deliveryAddress.getFastDeliveryCodeWard());
            data.put("address", deliveryAddress.getAddress());

            datas.add(data);
        }
        results.put("datas", datas);
        results.put("total", total);

        return results;
    }

    @Override
    public Map<String, Object> getDeliveries(Integer page, Integer limit, String city, String district, String ward, String searching) {

        List<DeliveryAddress> list = deliveryAddressDao.getDeliveries(page * limit, limit, city, district, ward, searching);
        long total = deliveryAddressDao.total(city, district, ward,searching);
        return getListData(list, total);
    }

    @Override
    public Map<String, Object> getDelivery(String id) {
        Optional<DeliveryAddress> deliveryAddress = deliveryAddressDao.findById(id);

        if(deliveryAddress.isPresent()){
            DeliveryAddress address = deliveryAddress.get();
            Map<String, Object> data = new HashMap<>();

            Map<String, Object> city = new HashMap<>();
            city.put("nameCity", address.getCity());
            city.put("idCity", address.getFastDeliveryCodeCity());
            data.put("province", city);

            Map<String, Object> district = new HashMap<>();
            district.put("nameDistrict", address.getDistrict());
            district.put("idDistrict", address.getFastDeliveryCodeDistrict());
            data.put("district", district);

            Map<String, Object> ward = new HashMap<>();
            ward.put("nameWard", address.getWard());
            ward.put("idWard", address.getFastDeliveryCodeWard());
            data.put("ward", ward);

            data.put("address", address.getAddress());
            return data;

        }
        return null;
    }

    @Override
    public void deleteDelivery(String id) {
        deliveryAddressDao.deleteById(id);
    }

    @Override
    public List<DeliveryAddress> getAll() {

        return deliveryAddressDao.findAll();
    }
}
