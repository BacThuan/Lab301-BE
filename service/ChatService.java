package com.application.backend.service;

import com.application.backend.document.Chat;

import java.util.List;
import java.util.Map;

public interface ChatService {

    void deleteChat(String email);


    Chat findByEmail(String email);


    void addChat(int type, String email, String content);

    List<Chat> findAll();
}
