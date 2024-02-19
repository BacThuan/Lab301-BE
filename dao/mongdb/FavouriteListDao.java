package com.application.backend.dao.mongdb;

import com.application.backend.document.FavouriteList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavouriteListDao extends MongoRepository<FavouriteList, String> {
    FavouriteList findByEmail(String email);
}
