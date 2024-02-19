package com.application.backend.service.impl;

import com.application.backend.dao.CartDao;
import com.application.backend.dao.ProductItemDao;
import com.application.backend.dao.StateDao;
import com.application.backend.dao.UserDao;
import com.application.backend.exception.CatchException;
import com.application.backend.model.Cart;
import com.application.backend.model.ProductItem;
import com.application.backend.model.State;
import com.application.backend.model.User;
import com.application.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartImpl implements CartService {

    @Autowired
    CartDao cartDao;
    @Autowired
    UserDao userDao;

    @Autowired
    ProductItemDao productItemDao;
    @Autowired
    StateDao stateDao;

    @Override
    public void addToCart(long itemId, String email, int count) {
        User user = userDao.findByEmail(email);
        ProductItem productItem = productItemDao.findById(itemId);

        Cart exsitedCart = cartDao.findExistedCart(user.getId(),productItem.getId());

        if(exsitedCart != null){
            exsitedCart.setQuantity(count + exsitedCart.getQuantity());
            cartDao.save(exsitedCart);
        }

        else {
            Cart cart = new Cart();
            cart.setQuantity(count);
            cart.setUser(user);
            cart.setProductItem(productItem);
            Cart result = cartDao.save(cart);


            user.addCarts(result);
            productItem.addCarts(cart);

            userDao.save(user);
            productItemDao.save(productItem);
        }

    }


    @Override
    public List<Object> userGetCart(String email) {
        User user = userDao.findByEmail(email);

        List<Object> result = new ArrayList<>();
        List<Cart> carts = cartDao.getByUser(user.getId());

        for(Cart cart : carts){
            Map<String, Object> data = new HashMap<>();
            ProductItem item = productItemDao.findById((long)cart.getProductItem().getId()) ;

            data.put("cartId", cart.getId());
            data.put("img",item.getPrimaryImage());
            data.put("name",item.getProduct().getName());
            data.put("size",item.getSize());
            data.put("gender",item.getProduct().getGender());
            data.put("color",item.getColor().getName());
            data.put("price",item.getPrice());
            data.put("quantity", cart.getQuantity());
            data.put("itemsLeft",item.getQuantity());

            result.add(data);

        }
        return result;
    }

    @Override
    public void updateCart(long cartId, int count) {
        Cart exsitedCart = cartDao.findById(cartId);
        exsitedCart.setQuantity(count);
        cartDao.save(exsitedCart);
    }

    @Override
    public void deleteCart(long cartId) {
        cartDao.deleteById(cartId);
    }

    @Override
    public Integer countCart(String email) {
        User user = userDao.findByEmail(email);

        Integer numbers = cartDao.countCart(user.getId());
        return numbers;
    }

    // --------user not sign up

    @Override
    public List<Object> guestGetCart(List<Map<String, Object>> listData) {

        List<Object> result = new ArrayList<>();

        for(Map<String, Object> info : listData){
            Map<String, Object> data = new HashMap<>();

            String id = String.valueOf(info.get("id"));
            ProductItem item = productItemDao.findById( Long.parseLong(id)) ;

            data.put("itemId", item.getId());
            data.put("img",item.getPrimaryImage());
            data.put("name",item.getProduct().getName());
            data.put("size",item.getSize());
            data.put("gender",item.getProduct().getGender());
            data.put("color",item.getColor().getName());
            data.put("price",item.getPrice());
            data.put("quantity", info.get("quantity"));
            data.put("itemsLeft",item.getQuantity());

            result.add(data);

        }
        return result;
    }

    @Override
    public void checkItems(List<Map<String, Object>> listData) {
        for(Map<String, Object> info : listData){

            String id = String.valueOf(info.get("id"));
            ProductItem item = productItemDao.findById(Long.parseLong(id)) ;

            Integer quantity = item.getQuantity();
            Integer cartValue =  Integer.parseInt(String.valueOf(info.get("quantity")));
            if(quantity < cartValue) throw new CatchException("No more item", HttpStatus.BAD_REQUEST);

        }
    }
}
