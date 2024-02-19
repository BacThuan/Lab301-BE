package com.application.backend.service.impl;

import com.application.backend.dao.ProductDao;
import com.application.backend.dao.ProductItemDao;
import com.application.backend.model.Product;
import com.application.backend.model.ProductImage;
import com.application.backend.model.ProductItem;
import com.application.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ProductImpl implements ProductService {
    @Autowired
    ProductDao productDao;
    @Autowired
    ProductItemDao productItemDao;

    public Map<String, Object> getListData(List<Product> products, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, String>> productData = new ArrayList<>();

        for(Product product : products){
            Map<String, String> data = new HashMap<>();

            data.put("_id",String.valueOf(product.getId()) );
            data.put("name", product.getName());
            data.put("gender", product.getGender());
            data.put("brand", product.getBrand().getName());
            data.put("category", product.getCategory().getName());
            data.put("create", product.getCreatedAt().toString());
            data.put("update", product.getUpdatedAt().toString());

            productData.add(data);
        }
        results.put("datas", productData);
        results.put("total", total);

        return results;
    }

    // delete duplicate 2 list
    public void deleteDuplicate(List<String> list1, List<String> list2){
        List<String> commonElements = new ArrayList<>(list1);
        commonElements.retainAll(list2);

        list2.removeAll(commonElements);
    }

    @Override
    public Map<String, Object> getFilter(String brand, String category, String gender, String searching, Integer page, Integer limit) {

        if(brand == null) brand = "";
        if(category == null) category = "";
        if(gender == null) gender = "";
        if(searching == null) searching = "";

        System.out.println(brand + " " + category + " " + " " + gender + " " +searching);

        List<Product> products = productDao.findFilter(brand,category,gender,searching,page * limit,limit);
        long total = productDao.totalFilter(brand,category,gender,searching);

        return getListData(products,total);
    }

    @Override
    public Map<String, Object> findProductById(Long productId) {
        Product product = productDao.findById((long)productId);

        if(product == null) return  getListData(new ArrayList<>(), 0);
        List<Product> products = new ArrayList<>(Arrays.asList(product));
        return getListData(products,1);
    }

    @Override
    public Map<String, Object> getProduct(long id) {
        Product product = productDao.findById(id);

        Map<String, Object> data = new HashMap<>();

        data.put("name", product.getName());
        data.put("gender", product.getGender());
        data.put("brand", product.getBrand().getName());
        data.put("category", product.getCategory().getName());
        data.put("short_desc", product.getShortDesc());
        data.put("long_desc", product.getLongDesc());

        return data;
    }

    @Override
    public Product findById(long id) {
        return productDao.findById(id);
    }

    @Override
    public Product addProduct(Product product) {
        return productDao.save(product);
    }

    @Override
    public void deleteProduct(long id) {
        productDao.deleteById(id);
    }

    // --------- client
    public Map<String, Object> getItemData(String data) {
        String[] item = data.split(",");
        Map<String, Object> temp = new HashMap<>();
        // name is: name + gender + color
        String name = item[1] + " "+ item[0] + " " + item[2];

        // url
        String primaryImage = item[3];

        // price
        String price = item[4];

        // item id
        String itemId = item[5];

        // product id
        String productId = item[6];

        temp.put("name", name);
        temp.put("img", primaryImage);
        temp.put("price", price);
        temp.put("itemId", itemId);
        temp.put("productId", productId);

        return temp;
    }
    @Override
    public List<Object> getEightHighestProduct() {
        List<String> data = productDao.getEightHighest();
        List<Object> result = new ArrayList<>();


        for(int i = 0; i < data.size(); ++i){
            result.add(getItemData(data.get(i)));
        }

        return result;
    }


    @Override
    public Map<String, Object> getPopupItem(long itemId) {
        ProductItem item = productItemDao.findById(itemId);
        Product product = item.getProduct();

        String name = product.getName();
        String color = item.getColor().getName();

        Map<String, Object> result = new HashMap<>();
        result.put("productId",product.getId());
        result.put("img", item.getPrimaryImage());
        result.put("name", name+ " " + color);
        result.put("brand", product.getBrand().getName());
        result.put("category",product.getCategory().getName());
        result.put("price",item.getPrice());
        result.put("size", item.getSize());
        result.put("gender", product.getGender());
        result.put("short_desc",product.getShortDesc());

        return result;
    }

    @Override
    public List<Object> getShopProduct(int page, String brand, String category, String color, Long gt, Long lt, String sort, String product) {


        // take only 9 item per page
        List<String> data;
        if(sort != null && sort.equals("a")){
            data = productDao.getShopProductSortA(page * 9, brand, category, color, gt, lt , product);
        }
        else if (sort != null && sort.equals("z")){
            data = productDao.getShopProductSortZ(page * 9, brand, category, color, gt, lt , product) ;
        }
        else if (sort != null && sort.equals("smallest")){
            data = productDao.getShopProductShortSmall(page * 9, brand, category, color, gt, lt , product);
        }
        else if (sort != null && sort.equals("highest")){
            data = productDao.getShopProductSortHigh(page * 9, brand, category, color, gt, lt , product);
        }
        else {
            data = productDao.getShopProduct(page * 9, brand, category, color, gt, lt , product);
        }
        List<Object> result = new ArrayList<>();

        for(int i = 0; i < data.size(); ++i){
            result.add(getItemData(data.get(i)));
        }

        return result;
    }

    @Override
    public Map<String, Object> getProductDetails(long productId) {
        Product product = productDao.findById(productId);

        Map<String, Object> result = new HashMap<>();
        result.put("name", product.getName());
        result.put("brand", product.getBrand().getName());
        result.put("category",product.getCategory().getName());

        result.put("gender", product.getGender());
        result.put("short_desc", product.getShortDesc());
        result.put("long_desc",product.getLongDesc());

        List<Object> items = new ArrayList<>();
        for(ProductItem tempItem : product.getProductItem()){
            ProductItem item = productItemDao.findById((long)tempItem.getId());
            Map<String, Object> temp = new HashMap<>();

            temp.put("id", item.getId());
            temp.put("size",item.getSize());
            temp.put("quantity", item.getQuantity());
            temp.put("img", item.getPrimaryImage());
            temp.put("price", item.getPrice());
            temp.put("color", item.getColor().getName());
            temp.put("colorCode", item.getColor().getCode());
            temp.put("state", item.getState().getStateName());

            List<String> images = new ArrayList<>();
            for(ProductImage image : item.getImages()){
                images.add(image.getUrl());
            }
            temp.put("images",images);

            items.add(temp);

        }

        result.put("items",items);
        return result;
    }

    @Override
    public List<Object> getRelated(long productId) {
        Product product = productDao.findById(productId);

        // get product with same category and brand
        long categoryId = product.getCategory().getId();
        long brandId = product.getBrand().getId();

        // get random only 5 for each type
        List<String> byCategory = productDao.getSameCategory(categoryId);
        List<String> byBrand = productDao.getSameBrand(brandId);

        // remove duplicate
        deleteDuplicate(byCategory,byBrand);

        List<Object> result = new ArrayList<>();

        for(int i = 0; i < byCategory.size(); ++i){
            result.add(getItemData(byCategory.get(i)));
        }
        for(int i = 0; i < byBrand.size(); ++i){
            result.add(getItemData(byBrand.get(i)));
        }

        return result;
    }
}
