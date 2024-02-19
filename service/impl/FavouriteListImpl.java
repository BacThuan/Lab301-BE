package com.application.backend.service.impl;

import com.application.backend.dao.ProductDao;
import com.application.backend.dao.mongdb.FavouriteListDao;
import com.application.backend.document.FavouriteList;
import com.application.backend.model.Product;
import com.application.backend.service.FavouriteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavouriteListImpl implements FavouriteListService {
    @Autowired
    FavouriteListDao favouriteListDao;

    @Autowired
    ProductDao productDao;
    @Override
    public void addLike(String email, Long idProduct) {
        FavouriteList list = favouriteListDao.findByEmail(email);

        if(list == null){
            list = new FavouriteList();
            list.setEmail(email);
        }

        list.addFavour(idProduct);
        favouriteListDao.save(list);
    }

    @Override
    public void removeLike(String email, Long idProduct) {
        FavouriteList list = favouriteListDao.findByEmail(email);

        List<Long> listProducts = list.getListProductId();

        listProducts.remove(idProduct);
        if(listProducts.size() == 0) favouriteListDao.deleteById(list.getId());
        else favouriteListDao.save(list);
    }

    @Override
    public List<Object> getListLike(String email) {
        FavouriteList list = favouriteListDao.findByEmail(email);

        if(list == null) return new ArrayList<>();

        else {
            List<Long> listProducts = list.getListProductId();
            List<Object> results = new ArrayList<>();

            for(Long id : listProducts){
                Product product = productDao.findById((long)id);

                Map<String, Object> data = new HashMap<>();
                if(product != null){
                    data.put("id",product.getId());
                    data.put("name", product.getName());
                    data.put("gender", product.getGender());
                    data.put("brand", product.getBrand().getName());
                    data.put("category", product.getCategory().getName());
                    data.put("numberItems", product.getProductItem().size());
                }
                else {
                    data.put("id",null);
                    data.put("name", null);
                    data.put("gender", null);
                    data.put("brand", null);
                    data.put("category", null);
                    data.put("numberItems", null);
                }

                results.add(data);

            }

            return results;
        }

    }

    @Override
    public List<Long> getListLikeId(String email) {
        FavouriteList list = favouriteListDao.findByEmail(email);
        if(list == null) return new ArrayList<>();
        return list.getListProductId();
    }
}
