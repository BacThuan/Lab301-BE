package com.application.backend.controller;

import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class StateController {
    @Autowired
    StateService stateService;
    @GetMapping("/read/states-user/getAll")
    public ResponseEntity<Object> getStateUser() {
        CheckRole.isRead();

        List<String> states = stateService.getStateUser();


        return Helper.res(states, HttpStatus.OK);
    }

    @GetMapping("/read/states-product/getAll")
    public ResponseEntity<Object> getStateProduct() {
        CheckRole.isRead();

        List<String> states = stateService.getStateProduct();


        return Helper.res(states, HttpStatus.OK);
    }

    @GetMapping("/read/states-payment/getAll")
    public ResponseEntity<Object> getStatePayment() {
        CheckRole.isRead();

        List<String> states = stateService.getStatePayment();


        return Helper.res(states, HttpStatus.OK);
    }

    @GetMapping("/read/states-order/getAll")
    public ResponseEntity<Object> getStateOrder() {
        CheckRole.isRead();

        List<String> states = stateService.getStateOrder();


        return Helper.res(states, HttpStatus.OK);
    }


    @GetMapping("/read/states")
    public ResponseEntity<Object> getStates(@RequestParam Integer page, @RequestParam  Integer limit,
                                           @RequestParam (required = false) String searching) {
        CheckRole.isRead();

        Map<String, Object> roles = null;
        if(searching != null) {
            roles = stateService.getSearching(page, limit, searching);
        }

        else {
            roles = stateService.getStates(page, limit);
        }


        return Helper.res(roles, HttpStatus.OK);
    }

    @DeleteMapping("/delete/states")
    public ResponseEntity<Object> deleteState(@RequestParam Long roleId) {

        CheckRole.isDelete();

        stateService.deleteState(roleId);
        return ResponseEntity.ok("Success!");
    }
}
