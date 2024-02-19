package com.application.backend.service.impl;

import com.application.backend.dao.BrandDao;
import com.application.backend.exception.CatchException;
import com.application.backend.model.Brand;
import com.application.backend.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrandImpl implements BrandService {
    @Autowired
    BrandDao brandDao;


    public Map<String, Object> getListData(List<Brand> brands, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, String>> datas = new ArrayList<>();

        for(Brand brand : brands){
            Map<String, String> data = new HashMap<>();

            data.put("_id",String.valueOf(brand.getId()) );
            data.put("name", brand.getName());
            data.put("create", brand.getCreatedAt().toString());

            datas.add(data);
        }
        results.put("datas", datas);
        results.put("total", total);

        return results;
    }

    @Override
    public Map<String, Object> getBrands(Integer page, Integer limit) {
        List<Brand> brands = brandDao.findPageAndLimit(page * limit,limit);
        long total = brandDao.totalPageAndLimit();
        return getListData(brands,total);
    }

    @Override
    public Map<String, Object> getSearching(Integer page, Integer limit, String searching) {
        List<Brand> brands = brandDao.findSearching(page * limit,limit,searching);
        long total = brandDao.totalSearching(searching);
        return getListData(brands,total);
    }
    @Override
    public void deleteBrand(Long id) {
        brandDao.deleteById(id);
    }


    @Override
    public List<String> getAll() {
        List<Brand> brands = brandDao.findAll();

        List<String> result = new ArrayList<>();
        for(Brand brand : brands){
            result.add(brand.getName());
        }
        return result;
    }

    @Override
    public void updateBrand(long id, String name) {
        Brand brand = brandDao.findByName(name);
        if(brand != null && brand.getId() != id) throw new CatchException("Brand is existed!", HttpStatus.CONFLICT);

        Brand brandUpdate = brandDao.findById(id);
        brandUpdate.setName(name);
        brandDao.save(brandUpdate);
    }

    @Override
    public void createBrand(String name) {
        Brand brand = brandDao.findByName(name);
        if(brand != null) throw new CatchException("Brand is existed!", HttpStatus.CONFLICT);
        brandDao.save(new Brand(name));
    }
}
