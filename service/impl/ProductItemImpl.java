package com.application.backend.service.impl;

import com.application.backend.dao.*;
import com.application.backend.file.ImageHandler;
import com.application.backend.model.*;
import com.application.backend.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;

@Service
public class ProductItemImpl implements ProductItemService {

    @Autowired
    ProductItemDao productItemDao;

    @Autowired
    StateDao stateDao;

    @Autowired
    ColorDao colorDao;

    @Autowired
    ProductImageDao productImageDao;

    @Autowired
    ProductDao productDao;

    public Map<String, Object> getListData(List<ProductItem> productItems, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, String>> productItemData = new ArrayList<>();

        for(ProductItem item : productItems){
            Map<String, String> data = new HashMap<>();

            data.put("_id",String.valueOf(item.getId()) );
            data.put("product_id",String.valueOf(item.getProduct().getId()) );
            data.put("name", item.getProduct().getName());
            data.put("price",String.valueOf(item.getPrice()));
            data.put("quantity", String.valueOf(item.getQuantity()));
            data.put("size", String.valueOf(item.getSize()));
            data.put("color", item.getColor().getName());
            data.put("state", item.getState().getStateName());
            data.put("image", item.getPrimaryImage());
            productItemData.add(data);
        }
        results.put("datas", productItemData);
        results.put("total", total);

        return results;
    }


    @Override
    public Map<String, Object> getFilter(Long idProduct, String searching, Integer quantity, Integer size, String state, String color, Long lt, Long gt, Integer page, Integer limit) {
        if(searching == null) searching = "";
        if(state == null) state = "";
        if(color == null) color = "";

        List<ProductItem> items = productItemDao.getFilter(idProduct,searching,quantity,size ,state, color,lt,gt,page * limit, limit);
        long total = productItemDao.totalFilter(idProduct,searching,quantity,size,state,color,lt,gt);

        return getListData(items,total);


    }

    @Override
    public Map<String, Object> findById(Long itemId) {
        ProductItem item = productItemDao.findById((long) itemId);
        List<ProductItem> items = new ArrayList<>(Arrays.asList(item));
        return getListData(items,1);
    }

    @Override
    public void deleteItem(long id) throws MalformedURLException {
        ProductItem item = productItemDao.findById(id);
        List<ProductImage> images = item.getImages();

        for(ProductImage image : images){
            ImageHandler.deleteImage(image.getName());
        }
        productItemDao.deleteById(id);
    }

    @Override
    public Map<String, Object> getItem(long id) {
        ProductItem item = productItemDao.findById(id);
        Map<String, Object> results = new HashMap<>();

        results.put("product_id", item.getProduct().getId());
        results.put("price", item.getPrice());
        results.put("quantity", item.getQuantity());
        results.put("size", item.getSize());
        results.put("color", item.getColor().getName());
        results.put("state",item.getState().getStateName());


        List<String> urls = new ArrayList<>();

        for(ProductImage image : item.getImages()){

            urls.add(image.getUrl());
        }
        results.put("images", urls);
        return results;
    }

    @Override
    public void addItem(Long price, Integer quantity, Integer size, String color, String state, long productId,
                        List<ProductImage> listImages, Integer primaryImage) {
        State theState = stateDao.findByStateName(state);
        Color theColor = colorDao.findByName(color);
        Product product = productDao.findById(productId);

        ProductImage primary = listImages.get(primaryImage);


        // save image first
        List<ProductImage> savedImages = new ArrayList<>();
        for(ProductImage image : listImages){
            savedImages.add(productImageDao.save(image));
        }

        ProductItem productItem = new ProductItem();

        productItem.setPrice(price);
        productItem.setQuantity(quantity);
        productItem.setSize(size);
        productItem.setPrimaryImage(primary.getUrl());


        productItem.setColor(theColor);
        productItem.setState(theState);
        productItem.setProduct(product);

        for(ProductImage image : savedImages){
            productItem.addImage(image);
        }

        ProductItem result = productItemDao.save(productItem);

        for(ProductImage image : savedImages){
            image.setProductItem(result);
        }

        product.addItem(result);
        productDao.save(product);
        productImageDao.saveAll(savedImages);

    }

    @Override
    public void updateItem(Long price, Integer quantity, Integer size, String color, String state, long itemId,
                           List<ProductImage> listImages, List<String> reusedImages, Integer primaryImage, Boolean deletePreImg) throws MalformedURLException {

        State theState = stateDao.findByStateName(state);
        Color theColor = colorDao.findByName(color);
        ProductItem item = productItemDao.findById(itemId);

        // save image first
        List<ProductImage> savedImages = new ArrayList<>();
        if(listImages.size() > 0){
            for(ProductImage image : listImages){
                savedImages.add(productImageDao.save(image));
            }
        }


        // find reused image
        List<ProductImage> itemImages = item.getImages();

        for(ProductImage image: itemImages){
            // if reused list not contain the url
            if(!reusedImages.contains(image.getUrl())){
                itemImages.remove(image);

                // if user want to delete unselected images, delete in database and server
                if(deletePreImg) {
                    productImageDao.deleteById(image.getId());
                    ImageHandler.deleteImage(image.getName());
                }
            }
        }

        // put to new list image
        savedImages.addAll(itemImages);

        item.setPrice(price);
        item.setQuantity(quantity);
        item.setSize(size);
        if(primaryImage != -1) item.setPrimaryImage(savedImages.get(primaryImage).getUrl());
        item.setColor(theColor);
        item.setState(theState);

        item.setImages(new ArrayList<>());
        for(ProductImage image : savedImages){
            item.addImage(image);
            image.setProductItem(item);
        }

        productImageDao.saveAll(savedImages);
        productItemDao.save(item);

    }
}
