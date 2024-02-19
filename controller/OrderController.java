package com.application.backend.controller;

import com.application.backend.configuration.VNPayConfig;
import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.model.Order;
import com.application.backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/read/orders")
    public ResponseEntity<Object> getOrders(@RequestParam  Integer page,@RequestParam  Integer limit,
                                           @RequestParam (value = "deliveryState", required = false) String deliveryState ,
                                           @RequestParam (value = "paymentState", required = false) String paymentState,
                                            @RequestParam (value = "searching", required = false) String searching,
                                            @RequestParam (value = "orderId", required = false) Long idOrder ){
        CheckRole.isRead();

        System.out.println(deliveryState);
        System.out.println(paymentState);
        System.out.println(searching);

        Map<String, Object> orders = orderService.getOrders(page,limit,deliveryState,paymentState, searching);

        if(idOrder != null ) orders = orderService.findById(idOrder);
        return Helper.res(orders, HttpStatus.OK);
    }

    @DeleteMapping("/delete/orders")
    public ResponseEntity<Object> updateCart (@RequestParam("idOrder") Long idOrder){
        CheckRole.isDelete();
        orderService.deleteOrder(idOrder);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/read/order")
    public ResponseEntity<Object> getOrderDetails( @RequestParam (value = "id", required = false) Long idOrder) {
        CheckRole.isRead();

        Map<String, Object> orders = orderService.getOrderDetails(idOrder);

        return Helper.res(orders, HttpStatus.OK);
    }

    @PostMapping("/read/order/getOrderItem")
    public ResponseEntity<Object> userOrder (@RequestBody List<Long> listIdItems) {
        CheckRole.isRead();
        List<Object> data = orderService.getItemInfo(listIdItems);
        return Helper.res(data, HttpStatus.OK);
    }

    @PutMapping("/update/orders")
    public ResponseEntity<Object> updateOrder (@RequestBody Map<String,Object> data) {
        CheckRole.isUpdate();
        Map<String,Object> info = (Map<String,Object>) data.get("info");
        long idOrder = Long.parseLong(String.valueOf(data.get("idOrder")));
        orderService.updateOrder(idOrder,info);
        return Helper.res("Ok", HttpStatus.OK);
    }

    @PostMapping("/create/orders")
    public ResponseEntity<Object> createOrder (@RequestBody Map<String,Object> data) {
        CheckRole.isUpdate();
        Map<String,Object> info = (Map<String,Object>) data.get("info");
        List<Map<String,Object>> itemQuantity =  (List<Map<String,Object>>) data.get("itemQuantity");
        long total = Long.parseLong(String.valueOf(data.get("total"))) ;

        orderService.createOrder(info,itemQuantity,total);
        return Helper.res("Ok", HttpStatus.OK);
    }

    @GetMapping("/read/orders/count")
    public ResponseEntity<Object> countOrder(@RequestParam (value = "month", required = false) Integer month,
                                             @RequestParam (value = "year", required = false) Integer year) {
        CheckRole.isRead();

        Long totalOrders = orderService.countOrder(year,month);

        return Helper.res(totalOrders, HttpStatus.OK);
    }

    @GetMapping("/read/orders/total")
    public ResponseEntity<Object> totalOrder(@RequestParam (value = "month", required = false) Integer month,
                                             @RequestParam (value = "year", required = false) Integer year) {
        CheckRole.isRead();

       long totalEarning = orderService.totalEarning(year,month);

        return Helper.res(totalEarning, HttpStatus.OK);
    }

    @GetMapping("/read/home")
    public ResponseEntity<Object> homeOrder(@RequestParam (value = "month", required = false) Integer month,
                                             @RequestParam (value = "year", required = false) Integer year) {
        CheckRole.isRead();
        List<Object> data = orderService.getHomeData(year,month);

        return Helper.res(data, HttpStatus.OK);
    }

    @GetMapping("/read/home/charts")
    public ResponseEntity<Object> getCharts(@RequestParam (value = "type", required = false) String type,
                                            @RequestParam (value = "year", required = false) Integer year) {
        CheckRole.isRead();


        List<Long> data = orderService.getCharts(type, year);

        return Helper.res(data, HttpStatus.OK);
    }


    // ----------------client
    @PostMapping("/users/orders")
    public ResponseEntity<Object> userOrder (@RequestParam("orderEmail") String orderEmail,
                                             @RequestParam("orderName") String orderName,
                                             @RequestParam("orderPhone") String orderPhone,
                                             @RequestParam("orderAddress") String orderAddress,
                                             @RequestParam("orderMethod")  String orderMethod,
                                             @RequestParam("orderTotal")  Long orderTotal,
                                             @RequestParam("emailAccount")  String emailAccount,
                                             @RequestParam(value = "discountCode",required = false)  String discountCodeId){


        orderService.addOrder(orderEmail,orderName,orderPhone,orderAddress,
                orderMethod,orderTotal,emailAccount, discountCodeId);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/users/orders")
    public ResponseEntity<Object> userGetOrder (@RequestParam("email")  String email){

        List<Object> data =  orderService.userGetOrder(email);
        return Helper.res(data, HttpStatus.OK);
    }

    @GetMapping("/users/orders/detail")
    public ResponseEntity<Object> userGetOrderDetail (@RequestParam("orderId")  Long orderId){

        Map<String, Object> data =  orderService.getOrderDetails(orderId);
        return Helper.res(data, HttpStatus.OK);
    }

    @GetMapping("/public/users/orders/pay")
    public ResponseEntity<Object> userPayOrderSuccess (@RequestParam("orderId")  Long orderId){

        orderService.successPayOrder(orderId);
        return Helper.res("Success", HttpStatus.OK);
    }

    @GetMapping("/public/users/orders/createVNPay")
    public ResponseEntity<Object> userCreateVNPay (@RequestParam("amount")  Long amount,
                                                   @RequestParam(value = "orderId", required = false)  Long orderId,
                                                   HttpServletRequest req) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        String urlData;
        if(orderId == null) urlData = "";
        else urlData = "?orderId=" + orderId;

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_BankCode", "NCB");

        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl+ urlData);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return Helper.res(paymentUrl, HttpStatus.OK);
    }

    // guest - client not in system
    @PostMapping("/public/users/orders")
    public ResponseEntity<Object> guestOrder (@RequestBody Map<String,Object> data){

        String orderEmail = (String) data.get("email");
        String orderName = (String) data.get("name");
        String orderPhone = (String) data.get("phone");

        String orderAddress = (String) data.get("address");

        String orderMethod = (String) data.get("method");
        long orderTotal = Long.parseLong(String.valueOf(data.get("total")));
        List<Map<String,Object>> dataOrder =  (List<Map<String,Object>>) data.get("listItem");
        Map<String, Object> order =
                orderService.addGuestOrder(orderEmail,orderName,orderPhone,orderAddress,
                        orderMethod,orderTotal,dataOrder);
        return Helper.res(order, HttpStatus.OK);
    }

    @DeleteMapping("/public/users/delete-order")
    public ResponseEntity<Object> deleteGuestOrder (@RequestParam("idOrder") Long idOrder){

        orderService.deleteOrder(idOrder);
        return ResponseEntity.ok("Success!");
    }

}
