package com.application.backend.dao;

import com.application.backend.model.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthProviderDao extends JpaRepository<AuthProvider, Long>{
    AuthProvider findByProviderName(String providerName);
}
