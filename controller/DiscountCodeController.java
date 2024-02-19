package com.application.backend.controller;

import com.application.backend.document.DeliveryAddress;
import com.application.backend.document.DiscountCode;
import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class DiscountCodeController {
    @Autowired
    DiscountCodeService discountCodeService;

    @PostMapping("/create/event")
    public ResponseEntity<Object> createEvent (@RequestParam("event")  String event,
                                               @RequestParam("orderFrom") Long orderFrom,
                                               @RequestParam("percent") Integer percent,
                                               @RequestParam("numberCodes") Integer numberCodes,
                                               @RequestParam("start") Date start,
                                               @RequestParam("end") Date end,
                                               @RequestParam("isActive") Boolean isActive){

        CheckRole.isCreate();
        discountCodeService.createEvent(event,orderFrom,percent,numberCodes,start,end, isActive);
        return ResponseEntity.ok("Success!");
    }
    @PutMapping("/update/event")
    public ResponseEntity<Object> updateEvent (@RequestParam("event")  String event,
                                               @RequestParam("orderFrom") Long orderFrom,
                                               @RequestParam("percent") Integer percent,
                                               @RequestParam("numberCodes") Integer numberCodes,
                                               @RequestParam("start") Date start,
                                               @RequestParam("end") Date end,
                                               @RequestParam("isActive") Boolean isActive,
                                               @RequestParam("id")  String idEvent){

        CheckRole.isUpdate();
        discountCodeService.updateEvent(idEvent,event,orderFrom,percent,numberCodes,start,end, isActive);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/read/events")
    public ResponseEntity<Object> getEvents(@RequestParam Integer page, @RequestParam  Integer limit,
                                            @RequestParam (required = false) String searching,
                                            @RequestParam (required = false) String dayStart,
                                            @RequestParam (required = false) String dayEnd) {
        CheckRole.isRead();

        Date start = null;
        Date end = null;

        if(dayStart != null) start = Helper.getDate(dayStart);
        if(dayEnd != null) end = Helper.getDate(dayEnd);


        Map<String, Object> events = discountCodeService.getEvents(page, limit, searching, start, end);

        return Helper.res(events, HttpStatus.OK);
    }

    @GetMapping("/read/event")
    public ResponseEntity<Object> getEvent(@RequestParam String id) {
        CheckRole.isRead();
        Map<String, Object> data = discountCodeService.getEvent(id);
        return Helper.res(data, HttpStatus.OK);

    }

    @DeleteMapping("/delete/event")
    public ResponseEntity<Object> deleteEvent(@RequestParam String id) {

        CheckRole.isDelete();

        discountCodeService.deleteEvent(id);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/public/get-active-event")
    public ResponseEntity<Object> getActiveEvent() {
        List<DiscountCode> data = discountCodeService.getActiveEvent();
        return Helper.res(data, HttpStatus.OK);

    }
}
