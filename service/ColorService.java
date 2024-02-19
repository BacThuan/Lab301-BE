package com.application.backend.service;

import java.util.List;
import java.util.Map;

public interface ColorService {
    Map<String, Object> getColors(Integer page, Integer limit);

    Map<String, Object> getSearching(Integer page, Integer limit, String searching);

    void deleteColor(Long id);

    List<Map<String, String>> getAll();

    void updateColor(long id, String name, String code);

    void createColor(String name, String code);
}
