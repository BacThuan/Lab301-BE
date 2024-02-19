package com.application.backend.controller;

import com.application.backend.exception.CatchException;
import com.application.backend.helper.Helper;
import com.application.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    @Autowired
    CartService cartService;

    @PostMapping("/users/carts/add")
    public ResponseEntity<Object> addToCart (@RequestParam("itemId") Long itemId,
                                             @RequestParam("email")  String email,
                                             @RequestParam("count")  Integer count){

        cartService.addToCart(itemId, email, count);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/users/carts")
    public ResponseEntity<Object> userGetCart (@RequestParam("email")  String email){

       List<Object> data =  cartService.userGetCart(email);
       return Helper.res(data, HttpStatus.OK);
    }

    @PutMapping("/users/carts/update")
    public ResponseEntity<Object> updateCart (@RequestParam("cartId") Long cartId,
                                             @RequestParam("count")  Integer count){

        if(count == null) count = 1;
        cartService.updateCart(cartId, count);
        return ResponseEntity.ok("Success!");
    }

    @DeleteMapping("/users/carts/delete")
    public ResponseEntity<Object> updateCart (@RequestParam("cartId") Long cartId){

        cartService.deleteCart(cartId);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/users/carts/count")
    public ResponseEntity<Object> userCartsCount (@RequestParam("email")  String email){

        Integer cartsCount =  cartService.countCart(email);
        return Helper.res(cartsCount, HttpStatus.OK);
    }

    // not sign up user
    @PostMapping("/public/cart")
    public ResponseEntity<Object> getCartForGuest (@RequestBody List<Map<String, Object>> listData){

        List<Object> data =  cartService.guestGetCart(listData);
        return Helper.res(data, HttpStatus.OK);
    }

    @PostMapping("/public/checkItems")
    public ResponseEntity<Object> checkIfItemsEmpty (@RequestBody List<Map<String, Object>> listData){

        cartService.checkItems(listData);

        return Helper.res("Ok", HttpStatus.OK);
    }
}
