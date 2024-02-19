package com.application.backend.service.impl;

import com.application.backend.dao.*;
import com.application.backend.dao.mongdb.DiscountCodeDao;
import com.application.backend.document.DiscountCode;
import com.application.backend.email.EmailHandler;
import com.application.backend.exception.CatchException;
import com.application.backend.model.*;
import com.application.backend.service.OrderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderImpl implements OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    StateDao stateDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CartDao cartDao;

    @Autowired
    OrderDetailDao orderDetailDao;

    @Autowired
    ProductItemDao productItemDao;

    @Autowired
    DiscountCodeDao discountCodeDao;


    @Autowired
    private EmailHandler emailHandler;


    public void autoMinusStock (ProductItem productItem, int count){
        int newQuantity = productItem.getQuantity() - count;
        productItem.setQuantity(newQuantity);

        // check state
        if(newQuantity == 0){
            State state = stateDao.findByStateName("PRODUCT_OUT_OF_STOCK");
            productItem.setState(state);
        }

    }

    // get simple user information of order
    public void getInformation (Map<String, Object> data, Order order){
        User user = order.getUser();
        data.put("idUser", user != null ? user.getId() : "Guest");
        data.put("name", order.getName());
        data.put("email", order.getEmail());
        data.put("phone", order.getPhone());
        data.put("address", order.getAddress());
        data.put("total", order.getTotal());
        data.put("method",order.getMethod());
        data.put("orderDay", order.getUpdatedAt());
        data.put("delivery",order.getDeliveryState() != null ? order.getDeliveryState().getStateName() : "Buy At Store");
        data.put("payment", order.getPaymentState().getStateName());
    }

    // get simple order item information
    public void getItemInformation( Map<String, Object> temp, OrderDetail orderDetail ){

        ProductItem productItem = productItemDao.findById((long)orderDetail.getProductItem().getId());

        temp.put("idItem", orderDetail.getProductItem().getId());
        temp.put("img", orderDetail.getProductItem().getPrimaryImage());
        temp.put("name", orderDetail.getProductItem().getProduct().getName());
        temp.put("color", orderDetail.getProductItem().getColor().getName());
        temp.put("size", orderDetail.getProductItem().getSize());
        temp.put("gender", orderDetail.getProductItem().getProduct().getGender());
        temp.put("quantity",orderDetail.getQuantity());
        temp.put("price", orderDetail.getProductItem().getPrice());
        temp.put("leftOver",productItem.getQuantity());

    }

    private Map<String, Object> getListData(List<Order> orders, long total) {
        Map<String, Object> results = new HashMap<>();

        List<Object> ordersData = new ArrayList<>();

        for(Order order : orders){
            Map<String, Object> data = new HashMap<>();

            data.put("_id", order.getId());
            getInformation(data, order);


            ordersData.add(data);
        }
        results.put("datas", ordersData);
        results.put("total", total);

        return results;
    }



    @Override
    public Map<String, Object> getOrders(int page, int limit, String deliveryState, String paymentState, String searching) {
        if(deliveryState == null) deliveryState = "";
        if(paymentState == null) paymentState = "";
        if(searching == null) searching = "";


        List<Order> orders = orderDao.findPageAndLimit(page * limit,limit, deliveryState,paymentState,searching);
        long total = orderDao.totalPageAndLimit(deliveryState,paymentState,searching);
        return getListData(orders,total);
    }

    @Override
    public Map<String, Object> findById(Long idOrder) {
        Order order = orderDao.findById((long)idOrder);

        if(order == null) return  getListData(new ArrayList<>(), 0);
        List<Order> orders = new ArrayList<>(Arrays.asList(order));
        return getListData(orders,1);
    }

    public void restoreQuantityOfDeleteOrder(OrderDetail item ){
        int quantity = item.getQuantity();

        ProductItem productItem = productItemDao.findById((long)item.getProductItem().getId());
        productItem.setQuantity(productItem.getQuantity() + quantity);

        productItemDao.save(productItem);

    }
    @Override
    public void deleteOrder(long idOrder) {

        List<OrderDetail> items = orderDetailDao.findByOrder(idOrder);

        for(OrderDetail item : items){

            if(item != null){
                restoreQuantityOfDeleteOrder(item);
                orderDetailDao.deleteById(item.getId());
            }

        }
        orderDao.deleteById(idOrder);
    }

    @Override
    public List<Object> getItemInfo(List<Long> listIdItems) {
        List<Object> list = new ArrayList<>();

        for(long id : listIdItems){
            Map<String, Object> temp = new HashMap<>();

            ProductItem productItem = productItemDao.findById(id);
            if(productItem.getQuantity() == 0) throw new CatchException("No more product", HttpStatus.BAD_REQUEST);

            temp.put("idItem", productItem.getId());
            temp.put("img", productItem.getPrimaryImage());
            temp.put("name", productItem.getProduct().getName());
            temp.put("color", productItem.getColor().getName());
            temp.put("size", productItem.getSize());
            temp.put("gender", productItem.getProduct().getGender());
            temp.put("quantity",1);
            temp.put("price", productItem.getPrice());
            temp.put("leftOver",productItem.getQuantity() - 1);

            list.add(temp);

        }
        return list;
    }

    public void checkAdminQuantity (List<Map<String, Object>> itemQuantity){
        List<ProductItem> items = new ArrayList<>();

        for(Map<String, Object> data : itemQuantity){
            ProductItem item = productItemDao.findById(Long.parseLong(String.valueOf(data.get("id"))) );

            // not enough throw err
            if(item.getQuantity() < Integer.parseInt(String.valueOf(data.get("quantity")))) {
                throw new CatchException("Not enough product. Order failed!.", HttpStatus.BAD_REQUEST);
            }

            // if enough then minus
            else {
                autoMinusStock(item,Integer.parseInt(String.valueOf(data.get("quantity"))));
                items.add(item);
            }
        }

        // if all pass, save them
        productItemDao.saveAll(items);
    }
    @Override
    public void updateOrder(long idOrder, Map<String, Object> info) {

        Order order = orderDao.findById(idOrder);

        State payment = stateDao.findByStateName((String) info.get("payment"));
        State delivery = stateDao.findByStateName((String) info.get("delivery"));

        order.setEmail((String) info.get("email"));
        order.setName((String) info.get("name"));
        order.setPhone((String) info.get("phone"));
        order.setMethod((String) info.get("method"));

        order.setPaymentState(payment);
        order.setDeliveryState(delivery);

        orderDao.save(order);

    }

    @Override
    public void createOrder(Map<String, Object> info, List<Map<String, Object>> itemQuantity, long total) {
        checkAdminQuantity(itemQuantity);

        State payment = stateDao.findByStateName("PAYMENT_PAID");
        State delivery = stateDao.findByStateName("ORDER_DELIVERY_SUCCESS");
        Order order = new Order();

        order.setEmail((String) info.get("email"));
        order.setName((String) info.get("name"));
        order.setPhone((String) info.get("phone"));
        order.setAddress("Buy At Store");
        order.setTotal(total);
        order.setMethod("Buy At Store");

        order.setPaymentState(payment);
        order.setDeliveryState(delivery);

        User user = userDao.findByEmail((String) info.get("email"));

        if(user != null) order.setUser(user);
        else order.setUser(null);

        Order savedOrder = orderDao.save(order);

        // add new items
        for(Map<String, Object> itemData : itemQuantity){
            OrderDetail orderDetail = new OrderDetail();

            ProductItem item = productItemDao.findById(Long.parseLong(String.valueOf(itemData.get("id"))) );

            orderDetail.setQuantity(Integer.parseInt(String.valueOf(itemData.get("quantity"))) );
            orderDetail.setOrder(savedOrder);
            orderDetail.setProductItem(item);

            orderDetailDao.save(orderDetail);
        }

    }

    @Override
    public long countOrder(Integer year, Integer month) {
        return orderDao.countMonthData(year, month);
    }

    @Override
    public long totalEarning(Integer year, Integer month) {
        return orderDao.countEarningData(year, month);
    }

    @Override
    public List<Object> getHomeData(Integer year, Integer month) {
        List<Order> orders  = orderDao.nearestEightOrder(year, month);

        List<Object> ordersData = new ArrayList<>();

        for(Order order : orders){
            Map<String, Object> data = new HashMap<>();

            data.put("_id", order.getId());
            getInformation(data, order);


            ordersData.add(data);
        }
        return ordersData;
    }

    @Override
    public List<Long> getCharts(String type, Integer year) {

        if(type.equals("sales")) {
            return orderDao.totalSales(year);
        }

        else {
            return orderDao.totalOrders(year);
        }
    }

    // --- client

    // check if order quantity is greater than database quantity
    public void checkQuantity (List<Cart> carts){
         List<ProductItem> items = new ArrayList<>();

        for(Cart cart : carts){
            ProductItem item = productItemDao.findById((long)cart.getProductItem().getId());

            // not enough throw err
            if(item.getQuantity() < cart.getQuantity()) {
                throw new CatchException("Not enough product. Order failed!.", HttpStatus.BAD_REQUEST);
            }

            // if enough then minus
            else {
                autoMinusStock(item,cart.getQuantity());
                items.add(item);
            }
        }

        // if all pass, save them
        productItemDao.saveAll(items);
    }

    @Override
    public void addOrder(String orderEmail, String orderName, String orderPhone, String orderAddress,
                         String orderMethod, Long orderTotal, String emailAccount, String discountCodeId) {


        Optional<DiscountCode> codeEvent = discountCodeDao.findById(discountCodeId);
        if(codeEvent.isPresent()){
            DiscountCode discountCode = codeEvent.get();

            if(discountCode.getUnusedCodes() == 0) throw new CatchException("No more discount codes!", HttpStatus.BAD_REQUEST);
            else{
                discountCode.setUnusedCodes(discountCode.getUnusedCodes() - 1);
                discountCodeDao.save(discountCode);
            }
        }

        User user = userDao.findByEmail(emailAccount);
        List<Cart> carts = cartDao.getByUser(user.getId());
        checkQuantity(carts);

        State unpaid = stateDao.findByStateName("PAYMENT_UNPAID");
        State notApproved = stateDao.findByStateName("ORDER_NOT_APPROVED");

        // create order
        Order order = new Order();
        order.setEmail(orderEmail);
        order.setAddress(orderAddress);


        order.setName(orderName);
        order.setPhone(orderPhone);
        order.setTotal(orderTotal);
        order.setMethod(orderMethod);

        order.setPaymentState(unpaid);
        order.setDeliveryState(notApproved);
        order.setUser(user);

        Order savedOrder = orderDao.save(order);


        List<OrderDetail> orderDetails = new ArrayList<>();
        // create order detail
        for(Cart cart : carts){
            OrderDetail orderDetail = new OrderDetail();

            ProductItem item = productItemDao.findById((long) cart.getProductItem().getId());

            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setOrder(savedOrder);
            orderDetail.setProductItem(item);

            orderDetails.add(orderDetailDao.save(orderDetail));

            // delete cart
            cartDao.deleteById(cart.getId());
        }

        // send email order
        // create a thread to send email so that it will not block the process
        new Thread(new Runnable() {
            public void run() {
                try {
                    emailHandler.sendOrderEmail(orderDetails, savedOrder);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public List<Object> userGetOrder(String email) {
        User user = userDao.findByEmail(email);
        List<Order> orders = orderDao.findByUser(user.getId());

        List<Object> result = new ArrayList<>();
        for(Order order : orders){
            Map<String, Object> data = new HashMap<>();

            data.put("idOrder", order.getId());
            getInformation(data, order);

            result.add(data);
        }
        return result;
    }

    @Override
    public Map<String, Object> getOrderDetails(long orderId) {
        Order order = orderDao.findById(orderId);

        Map<String, Object> data = new HashMap<>();

        User user = order.getUser();
        data.put("idUser", user != null ? user.getId() : "Guest");
        data.put("name", order.getName());
        data.put("email", order.getEmail());
        data.put("phone", order.getPhone());
        data.put("address", order.getAddress());

        data.put("total", order.getTotal());
        data.put("method",order.getMethod());
        data.put("orderDay", order.getUpdatedAt());
        data.put("delivery",order.getDeliveryState().getStateName());
        data.put("payment", order.getPaymentState().getStateName());

        List<OrderDetail> orderDetails = orderDetailDao.findByOrder(order.getId());

        List<Object> orderData = new ArrayList<>();
        for(OrderDetail orderDetail : orderDetails){

            Map<String, Object> temp = new HashMap<>();

            getItemInformation(temp,orderDetail);
            orderData.add(temp);
        }

        data.put("orderData", orderData);
        return data;
    }

    @Override
    public void successPayOrder(Long orderId) {
        Order order = orderDao.findById((long)orderId);

        State state = stateDao.findByStateName("PAYMENT_PAID");

        order.setPaymentState(state);

        orderDao.save(order);
    }

    // guest ---------

    @Override
    public Map<String, Object> addGuestOrder(String orderEmail, String orderName, String orderPhone, String orderAddress,
                                             String orderMethod, long orderTotal, List<Map<String, Object>> dataOrder) {
        checkAdminQuantity(dataOrder);

        State unpaid = stateDao.findByStateName("PAYMENT_PAID");
        State notApproved = stateDao.findByStateName("ORDER_NOT_APPROVED");

        // create order
        Order order = new Order();
        order.setEmail(orderEmail);
        order.setAddress(orderAddress);

        order.setName(orderName);
        order.setPhone(orderPhone);
        order.setTotal(orderTotal);
        order.setMethod(orderMethod);

        order.setPaymentState(unpaid);
        order.setDeliveryState(notApproved);
        order.setUser(null);

        Order savedOrder = orderDao.save(order);


        List<OrderDetail> orderDetails = new ArrayList<>();
        // create order detail
        for(Map<String, Object> data : dataOrder){
            OrderDetail orderDetail = new OrderDetail();

            String id = String.valueOf(data.get("id"));
            ProductItem item = productItemDao.findById( Long.parseLong(id)) ;

            String count = String.valueOf(data.get("quantity"));
            orderDetail.setQuantity(Integer.parseInt(count));
            orderDetail.setOrder(savedOrder);
            orderDetail.setProductItem(item);

            orderDetails.add(orderDetailDao.save(orderDetail));

        }

        // send email order
        // create a thread to send email so that it will not block the process
        new Thread(new Runnable() {
            public void run() {
                try {
                    emailHandler.sendOrderEmail(orderDetails, savedOrder);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Map<String, Object> data = new HashMap<>();
        data.put("idOrder", savedOrder.getId());
        getInformation (data, savedOrder);
        return data;
    }
}
