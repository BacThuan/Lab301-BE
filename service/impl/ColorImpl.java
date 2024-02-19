package com.application.backend.service.impl;

import com.application.backend.dao.ColorDao;
import com.application.backend.exception.CatchException;
import com.application.backend.model.Color;
import com.application.backend.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ColorImpl implements ColorService {
    @Autowired
    ColorDao colorDao;

    public Map<String, Object> getListData(List<Color> colors, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, String>> datas = new ArrayList<>();

        for(Color color : colors){
            Map<String, String> data = new HashMap<>();

            data.put("_id",String.valueOf(color.getId()));
            data.put("name", color.getName());
            data.put("code",color.getCode());
            data.put("create",color.getCreatedAt().toString());

            datas.add(data);
        }
        results.put("datas", datas);
        results.put("total", total);

        return results;
    }
    @Override
    public Map<String, Object> getColors(Integer page, Integer limit) {
        List<Color> colors = colorDao.findPageAndLimit(page * limit,limit);
        long total = colorDao.totalPageAndLimit();
        return getListData(colors,total);
    }

    @Override
    public Map<String, Object> getSearching(Integer page, Integer limit, String searching) {
        List<Color> colors = colorDao.findSearching(page * limit,limit,searching);
        long total = colorDao.totalSearching(searching);
        return getListData(colors,total);
    }

    @Override
    public void deleteColor(Long id) {
        colorDao.deleteById(id);
    }

    @Override
    public List<Map<String, String>> getAll() {
        List<Color> colors = colorDao.findAll();

        List<Map<String,String>> result = new ArrayList<>();
        for(Color color : colors){
            Map<String,String> attribute = new HashMap<>();
            attribute.put("name", color.getName());
            attribute.put("code", color.getCode());
            result.add(attribute);
        }
        return result;
    }

    @Override
    public void updateColor(long id, String name, String code) {
        Color color = colorDao.findByName(name);
        if(color != null && color.getId() != id) throw new CatchException("Color is existed!", HttpStatus.CONFLICT);

        Color colorUpdate = colorDao.findById(id);
        colorUpdate.setName(name);
        colorUpdate.setCode(code);
        colorDao.save(colorUpdate);
    }

    @Override
    public void createColor(String name, String code) {
        Color color = colorDao.findByName(name);
        if(color != null) throw new CatchException("Color is existed!", HttpStatus.CONFLICT);

        colorDao.save(new Color(name,code));
    }
}
