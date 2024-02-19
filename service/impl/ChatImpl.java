package com.application.backend.service.impl;

import com.application.backend.dao.mongdb.ChatDao;
import com.application.backend.document.Chat;
import com.application.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatImpl implements ChatService {
    @Autowired
    ChatDao chatDao;

    @Override
    public void addChat(int type, String email, String content) {
        Chat chat = chatDao.findByEmailClient(email);

        if(type == 0){
            chat.addChat(0, content);
            chatDao.save(chat);
        }

        else {
            // if first time client chat
            if(chat == null){
                chat = new Chat();
                chat.setEmailClient(email);
            }
            chat.addChat(1, content);
            chatDao.save(chat);
        }
    }


    @Override
    public void deleteChat(String email) {
        Chat chat = chatDao.findByEmailClient(email);
        chatDao.deleteById(chat.getId());
    }


    @Override
    public Chat findByEmail(String email) {
        return chatDao.findByEmailClient(email);
    }

    @Override
    public List<Chat> findAll() {

        return chatDao.findAll();
    }
}
