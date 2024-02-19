package com.application.backend.service.impl;

import com.application.backend.dao.mongdb.DiscountCodeDao;
import com.application.backend.document.DiscountCode;
import com.application.backend.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiscountCodeImpl implements DiscountCodeService {

    @Autowired
    DiscountCodeDao discountCodeDao;

    @Override
    public void createEvent(String event, Long orderFrom, Integer percent, Integer numberCodes, Date start, Date end, Boolean isActive) {
        DiscountCode code = new DiscountCode();

        code.setName(event);
        code.setOrderFrom(orderFrom);
        code.setDiscountPercent(percent);

        code.setDayStart(start);
        code.setDayEnd(end);
        code.setActive(isActive);

        code.setUnusedCodes(numberCodes);

        discountCodeDao.save(code);
    }

    @Override
    public void updateEvent(String idEvent, String event, Long orderFrom, Integer percent, Integer numberCodes, Date start, Date end, Boolean isActive) {
        Optional<DiscountCode> theEvent = discountCodeDao.findById(idEvent);

        if(theEvent.isPresent()){
            DiscountCode code = theEvent.get();

            code.setName(event);
            code.setOrderFrom(orderFrom);
            code.setDiscountPercent(percent);

            code.setDayStart(start);
            code.setDayEnd(end);
            code.setActive(isActive);

            code.setUnusedCodes(numberCodes);

            discountCodeDao.save(code);


        }
    }

    public void getListData(Map<String, Object> data, DiscountCode discountCode){

        data.put("_id",discountCode.getId());
        data.put("event", discountCode.getName());
        data.put("orderFrom", discountCode.getOrderFrom());
        data.put("percent", discountCode.getDiscountPercent());
        data.put("numberCodes", discountCode.getUnusedCodes());
        data.put("start", discountCode.getDayStart());
        data.put("end", discountCode.getDayEnd());
        data.put("isActive", discountCode.getActive());

    }

    @Override
    public Map<String, Object> getEvents(Integer page, Integer limit, String searching, Date start, Date end) {

        List<DiscountCode> list = discountCodeDao.getEvent(page * limit, limit, searching,start, end);
        long total = discountCodeDao.total(searching, start, end);

        Map<String, Object> results = new HashMap<>();

        List<Map<String, Object>> datas = new ArrayList<>();

        for(DiscountCode discountCode : list){
            Map<String, Object> data = new HashMap<>();

            getListData(data,discountCode);

            datas.add(data);
        }
        results.put("datas", datas);
        results.put("total", total);

        return results;
    }

    @Override
    public Map<String, Object> getEvent(String id) {
        Optional<DiscountCode> discountCode = discountCodeDao.findById(id);

        if(discountCode.isPresent()) {
            Map<String, Object> results = new HashMap<>();
            getListData(results, discountCode.get());
            return results;
        };
        return null;
    }

    @Override
    public void deleteEvent(String id) {
        discountCodeDao.deleteById(id);
    }

    @Override
    public List<DiscountCode> getActiveEvent() {

        return discountCodeDao.findActiveEvent(new Date());
    }
}
