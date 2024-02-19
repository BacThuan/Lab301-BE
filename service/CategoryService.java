package com.application.backend.service;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Map<String, Object> getCategories(Integer page, Integer limit);

    Map<String, Object> getSearching(Integer page, Integer limit, String searching);

    void deleteCategory(Long id);

    List<String> getAll();


    void updateCategory(long id, String name);

    void createCategory(String name);
}
