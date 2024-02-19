package com.application.backend.service.impl;

import com.application.backend.dao.AuthProviderDao;
import com.application.backend.model.AuthProvider;
import com.application.backend.service.AuthProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthProviderImpl implements AuthProviderService {
    @Autowired
    AuthProviderDao authProviderDao;

    @Override
    public List<String> getAuths() {
        List<AuthProvider> auths = authProviderDao.findAll();

        List<String> result = new ArrayList<>();
        for(AuthProvider auth : auths){
            result.add(auth.getProviderName());
        }
        return result;
    }
}
