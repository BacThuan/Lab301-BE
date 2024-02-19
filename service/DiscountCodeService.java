package com.application.backend.service;

import com.application.backend.document.DiscountCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DiscountCodeService {
    void createEvent(String event, Long orderFrom, Integer percent, Integer numberCodes, Date start, Date end, Boolean isActive);

    void updateEvent(String idEvent, String event, Long orderFrom, Integer percent, Integer numberCodes, Date start, Date end, Boolean isActive);

    Map<String, Object> getEvents(Integer page, Integer limit, String searching, Date start, Date end);

    Map<String, Object> getEvent(String id);

    void deleteEvent(String id);

    List<DiscountCode> getActiveEvent();
}
