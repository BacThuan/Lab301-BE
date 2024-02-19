package com.application.backend.controller;

import com.application.backend.file.ImageHandler;
import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.model.ProductImage;
import com.application.backend.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductItemController {
    private static final String UPLOAD_DIR = "classpath:images/";
    @Autowired
    ProductItemService productItemService;


    @GetMapping("/read/items")
    public ResponseEntity<Object> getItems(@RequestParam Integer page, @RequestParam  Integer limit,
                                           @RequestParam (required = false) String idProduct,
                                           @RequestParam (required = false) String searching,
                                           @RequestParam (required = false) Integer quantity,
                                           @RequestParam (required = false) Integer size,
                                           @RequestParam (required = false) String state,
                                           @RequestParam (required = false) Long lt, @RequestParam (required = false) Long gt,
                                           @RequestParam (required = false) String color,
                                           @RequestParam (required = false) Long itemId) {
        CheckRole.isRead();
        Long tempIdProduct = null;
        if(idProduct != null) tempIdProduct = Long.parseLong(idProduct);
        Map<String, Object> productItems =  productItemService.getFilter(tempIdProduct, searching, quantity,size,state,color,lt,gt, page, limit);;


        if(itemId != null ) productItems = productItemService.findById(itemId);
        return Helper.res(productItems, HttpStatus.OK);
    }

    @DeleteMapping("/delete/items")
    public ResponseEntity<Object> deleteUser(@RequestParam long id) throws MalformedURLException {

        CheckRole.isDelete();

        productItemService.deleteItem(id);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/read/item")
    public ResponseEntity<Object> getProduct(@RequestParam long id) {
        CheckRole.isRead();
        Map<String, Object> data = productItemService.getItem(id);
        return Helper.res(data, HttpStatus.OK);

    }

    @PostMapping("/create/product-item")
    public ResponseEntity<Object> createProductItem(@RequestParam("price") Long price,
                                                    @RequestParam("quantity") Integer quantity,
                                                    @RequestParam("size") Integer size,
                                                    @RequestParam("color") String color,
                                                    @RequestParam("state") String state,
                                                    @RequestParam("product") long productId,
                                                    @RequestParam("primaryImage") Integer primaryImage,
                                                    @RequestParam("images") List<MultipartFile> images) throws IOException {
        CheckRole.isCreate();


        List<ProductImage> listImages = new ArrayList<>();

        for(MultipartFile image : images){
            // Get the filename
            String fileName = ImageHandler.saveImage(image);

            ProductImage productImage = new ProductImage();
            productImage.setName(fileName);
            productImage.setUrl(System.getenv("API_SERVER")+"/images/" + fileName);
            listImages.add(productImage);
        }



        productItemService.addItem(price,quantity,size,color,state,productId,listImages,primaryImage);

        return ResponseEntity.ok("Success!");
    }

    @PutMapping("/update/product-item")
    public ResponseEntity<Object> updateProductItem(@RequestParam("price") Long price,
                                                    @RequestParam("quantity") Integer quantity,
                                                    @RequestParam("size") Integer size,
                                                    @RequestParam("color") String color,
                                                    @RequestParam("state") String state,
                                                    @RequestParam("id") long itemId,
                                                    @RequestParam("primaryImage") Integer primaryImage,
                                                    @RequestParam("deletePreImg") Boolean deletePreImg,
                                                    @RequestParam(value = "reusedImage", required = false)  List<String> reusedImages,
                                                    @RequestParam(value = "images", required = false)  List<MultipartFile> newImages) throws IOException {
        CheckRole.isUpdate();


        List<ProductImage> listImages = new ArrayList<>();

        if(newImages != null && newImages.size() > 0){
            for(MultipartFile image : newImages){
                // Get the filename
                String fileName = ImageHandler.saveImage(image);

                ProductImage productImage = new ProductImage();
                productImage.setName(fileName);
                productImage.setUrl(System.getenv("API_SERVER")+"/images/" + fileName);
                listImages.add(productImage);
            }
        }


        productItemService.updateItem(price,quantity,size,color,state,itemId,listImages, reusedImages,primaryImage,deletePreImg);

        return ResponseEntity.ok("Success!");
    }

}
