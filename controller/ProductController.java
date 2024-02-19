package com.application.backend.controller;

import com.application.backend.dao.*;
import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.model.*;
import com.application.backend.service.ProductService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;




    @GetMapping("/read/products")
    public ResponseEntity<Object> getProducts(@RequestParam Integer page, @RequestParam  Integer limit,
                                              @RequestParam (required = false) String searching,
                                              @RequestParam (required = false) String brand,
                                              @RequestParam (required = false) String category,
                                              @RequestParam (required = false) String gender,
                                              @RequestParam (required = false) Long productId) {
        CheckRole.isRead();

        Map<String, Object> products = productService.getFilter(brand,category,gender, searching,page,limit);

        if(productId != null ) products = productService.findProductById(productId);
        return Helper.res(products, HttpStatus.OK);
    }

    @GetMapping("/read/product")
    public ResponseEntity<Object> getProduct(@RequestParam long id) {
        CheckRole.isRead();
        Map<String, Object> data = productService.getProduct(id);
        return Helper.res(data, HttpStatus.OK);

    }


    @PostMapping("/create/product")
    public ResponseEntity<Object> createProduct(@RequestBody Map<String,Object> info) throws MessagingException {
        CheckRole.isCreate();

        String name = (String)  info.get("name");
        String short_desc = (String) info.get("short_desc");
        String long_desc = (String)  info.get("long_desc");
        String gender = (String)  info.get("gender");

        String theBrand = (String) info.get("brand");
        String theCategory = (String)  info.get("category");

        Brand brand = brandDao.findByName(theBrand);
        Category category = categoryDao.findByName(theCategory);

        Product product = new Product();

        product.setName(name);
        product.setShortDesc(short_desc);
        product.setLongDesc(long_desc);
        product.setGender(gender);

        product.setBrand(brand);
        product.setCategory(category);

        productService.addProduct(product);

        return ResponseEntity.ok("Success!");
    }

    @PutMapping("/update/product")
    public ResponseEntity<Object> updateProduct(@RequestBody Map<String,Object>  info) {

        CheckRole.isUpdate();

        long id = Long.parseLong((String) info.get("id"));
        String name = (String) info.get("name");
        String short_desc = (String) info.get("short_desc");
        String long_desc = (String)  info.get("long_desc");
        String gender = (String)  info.get("gender");

        String theBrand = (String) info.get("brand");
        String theCategory = (String)  info.get("category");

        Brand brand = brandDao.findByName(theBrand);
        Category category = categoryDao.findByName(theCategory);

        Product product = productService.findById(id);

        product.setName(name);
        product.setShortDesc(short_desc);
        product.setLongDesc(long_desc);
        product.setGender(gender);

        product.setBrand(brand);
        product.setCategory(category);

        productService.addProduct(product);

        return ResponseEntity.ok("Success!");
    }


    @DeleteMapping("/delete/products")
    public ResponseEntity<Object> deleteUser(@RequestParam long id) {

        CheckRole.isDelete();

        productService.deleteProduct(id);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/public/homepage-products")
    public ResponseEntity<Object> getHomePageProduct() {
        // get only 8 highest items in shop
        List<Object> data = productService.getEightHighestProduct();
        return Helper.res(data, HttpStatus.OK);

    }

    @GetMapping("/public/product")
    public ResponseEntity<Object> getPopupProduct(@RequestParam Long itemId) {

        Map<String, Object> data = productService.getPopupItem(itemId);
        return Helper.res(data, HttpStatus.OK);

    }

    @GetMapping("/public/products")
    public ResponseEntity<Object> getShopProduct(@RequestParam Integer page,
                                                 @RequestParam (required = false) String sort,
                                                 @RequestParam (required = false) String brand,
                                                 @RequestParam (required = false) String category,
                                                 @RequestParam (required = false) String color,
                                                 @RequestParam (required = false) String price,
                                                 @RequestParam (required = false) String product) {


        Long lt = null;
        Long gt = null;

        if(price != null && !price.equals("")){

            if(price.contains("<")) {
                String[] limit = price.split("<");
                lt = Long.parseLong(limit[1].replace(".","").trim());
            }
            else if(price.contains("-")) {
                String[] limit = price.split("-");
                lt = Long.parseLong(limit[1].replace(".","").trim());
                gt = Long.parseLong(limit[0].replace(".","").trim());
            }
            else {
                String[] limit = price.split(">");
                gt = Long.parseLong(limit[1].replace(".","").trim());
            }
        }

        List<Object> data = productService.getShopProduct(page, brand, category, color, gt, lt, sort, product);
        return Helper.res(data, HttpStatus.OK);

    }

    @GetMapping("/public/products/details")
    public ResponseEntity<Object> getProductDetail(@RequestParam Long productId) {

        Map<String, Object> data = productService.getProductDetails(productId);
        return Helper.res(data, HttpStatus.OK);

    }

    @GetMapping("/public/products/related")
    public ResponseEntity<Object> getProductRelated(@RequestParam Long productId) {

        List<Object> data = productService.getRelated(productId);
        return Helper.res(data, HttpStatus.OK);

    }
}
