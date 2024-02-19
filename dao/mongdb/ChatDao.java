package com.application.backend.dao.mongdb;

import com.application.backend.document.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatDao extends MongoRepository<Chat, String>{

    Chat findByEmailClient(String email);
}
