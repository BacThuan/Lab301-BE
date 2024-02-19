package com.application.backend.service.impl;

import com.application.backend.dao.CategoryDao;
import com.application.backend.dao.RoleDao;
import com.application.backend.exception.CatchException;
import com.application.backend.model.Brand;
import com.application.backend.model.Category;
import com.application.backend.model.Role;
import com.application.backend.service.CategoryService;
import com.application.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryImpl implements CategoryService {
    @Autowired
    CategoryDao categoryDao;
    public Map<String, Object> getListData(List<Category> categories, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, String>> datas = new ArrayList<>();

        for(Category category : categories){
            Map<String, String> data = new HashMap<>();

            data.put("_id",String.valueOf(category.getId()));
            data.put("name", category.getName());
            data.put("create",category.getCreatedAt().toString());

            datas.add(data);
        }
        results.put("datas", datas);
        results.put("total", total);

        return results;
    }

    @Override
    public Map<String, Object> getCategories(Integer page, Integer limit) {
        List<Category> categories = categoryDao.findPageAndLimit(page * limit,limit);
        long total = categoryDao.totalPageAndLimit();
        return getListData(categories,total);
    }

    @Override
    public Map<String, Object> getSearching(Integer page, Integer limit, String searching) {
        List<Category> categories = categoryDao.findSearching(page * limit,limit,searching);
        long total = categoryDao.totalSearching(searching);
        return getListData(categories,total);
    }


    @Override
    public void deleteCategory(Long id) {
        categoryDao.deleteById(id);
    }

    @Override
    public List<String> getAll() {
        List<Category> categories = categoryDao.findAll();

        List<String> result = new ArrayList<>();
        for(Category category : categories){
            result.add(category.getName());
        }
        return result;
    }

    @Override
    public void updateCategory(long id, String name) {
        Category category = categoryDao.findByName(name);

        if(category != null && category.getId() != id) {
            throw new CatchException("Category is existed!", HttpStatus.CONFLICT);
        }
        Category categoryUpdate = categoryDao.findById(id);
        categoryUpdate.setName(name);
        categoryDao.save(categoryUpdate);

    }

    @Override
    public void createCategory(String name) {
        Category category = categoryDao.findByName(name);

        if(category != null) throw new CatchException("Category is existed!", HttpStatus.CONFLICT);
        categoryDao.save(new Category(name));
    }
}
