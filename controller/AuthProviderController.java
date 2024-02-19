package com.application.backend.controller;

import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.AuthProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthProviderController {
    @Autowired
    AuthProviderService authProviderService;

    @GetMapping("/read/auths/getAll")
    public ResponseEntity<Object> getAllRoles() {
        CheckRole.isRead();

        List<String> auths = authProviderService.getAuths();
        return Helper.res(auths, HttpStatus.OK);
    }


}
