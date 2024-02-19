package com.application.backend.controller;

import com.application.backend.document.DeliveryAddress;
import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DeliveryAddressController {
    @Autowired
    DeliveryAddressService deliveryAddressService;

    @PostMapping("/create/delivery-shop")
    public ResponseEntity<Object> createDeliveryShop (@RequestParam("city")  String city,
                                           @RequestParam("fastDeliveryCodeCity")  String fastDeliveryCodeCity,
                                           @RequestParam("district")  String district,
                                           @RequestParam("fastDeliveryCodeDistrict")  String fastDeliveryCodeDistrict,
                                           @RequestParam("ward")  String ward,
                                           @RequestParam("fastDeliveryCodeWard")  String fastDeliveryCodeWard,
                                           @RequestParam("address")  String address){

        CheckRole.isCreate();
        deliveryAddressService.createShop(city, fastDeliveryCodeCity, district, fastDeliveryCodeDistrict,
                                            ward, fastDeliveryCodeWard, address);
        return ResponseEntity.ok("Success!");
    }

    @PutMapping("/update/delivery-shop")
    public ResponseEntity<Object> updateDeliveryShop (@RequestParam("city")  String city,
                                                      @RequestParam("fastDeliveryCodeCity")  String fastDeliveryCodeCity,
                                                      @RequestParam("district")  String district,
                                                      @RequestParam("fastDeliveryCodeDistrict")  String fastDeliveryCodeDistrict,
                                                      @RequestParam("ward")  String ward,
                                                      @RequestParam("fastDeliveryCodeWard")  String fastDeliveryCodeWard,
                                                      @RequestParam("address")  String address,
                                                      @RequestParam("id")  String id){

        CheckRole.isUpdate();
        deliveryAddressService.updateShop(id, city, fastDeliveryCodeCity, district, fastDeliveryCodeDistrict,
                ward, fastDeliveryCodeWard, address);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/read/deliveries")
    public ResponseEntity<Object> getDeliveries(@RequestParam Integer page, @RequestParam  Integer limit,
                                              @RequestParam (required = false) String city,
                                              @RequestParam (required = false) String district,
                                              @RequestParam (required = false) String ward, @RequestParam (required = false) String searching) {
        CheckRole.isRead();

        Map<String, Object> deliveries = deliveryAddressService.getDeliveries(page, limit, city,district, ward, searching);

        return Helper.res(deliveries, HttpStatus.OK);
    }

    @GetMapping("/read/delivery")
    public ResponseEntity<Object> readDelivery(@RequestParam String id) {

        CheckRole.isRead();

        Map<String, Object> data =  deliveryAddressService.getDelivery(id);
        return ResponseEntity.ok(data);
    }


    @DeleteMapping("/delete/delivery")
    public ResponseEntity<Object> deleteDelivery(@RequestParam String id) {

        CheckRole.isDelete();

        deliveryAddressService.deleteDelivery(id);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/public/get-all-delivery-shop")
    public ResponseEntity<Object> getAllDelivery() {
        List<DeliveryAddress> data = deliveryAddressService.getAll();
        return Helper.res(data, HttpStatus.OK);

    }
}
