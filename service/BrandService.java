package com.application.backend.service;

import java.util.List;
import java.util.Map;

public interface BrandService {
    Map<String, Object> getBrands(Integer page, Integer limit);

    Map<String, Object> getSearching(Integer page, Integer limit, String searching);

    void deleteBrand(Long id);

    List<String> getAll();

    void updateBrand(long id, String name);

    void createBrand(String name);
}
