package com.application.backend.controller;


import com.application.backend.document.Chat;
import com.application.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/private-message")
    public Object addChat(@Payload Map<String, String> data) {


        String email = data.get("email");
        String message = data.get("message");
        Integer type = Integer.parseInt(data.get("type"));
        chatService.addChat(type,email,message);
        simpMessagingTemplate.convertAndSendToUser(data.get("email"),
                "/private",
                data); // /user/{email}/message

        if(data.get("create") != null) getAllChat();


        if(type == 0 && message.equals("/end")) {
            chatService.deleteChat(email);
            getAllChat();
        }
        return data;
    }

    @MessageMapping("/get-private-message")
    public String getChat(@Payload Map<String, String> data) {
        Chat chat = chatService.findByEmail(data.get("email"));


        simpMessagingTemplate.convertAndSendToUser(data.get("email"),
                "/get-private",
                chat == null ? new ArrayList<>() : chat.getContents());
        return null;
    }

    @MessageMapping("/get-all-message")
    public List<String> getAllChat() {
        List<Chat> chats = chatService.findAll();
        List<String> list = new ArrayList<>();

        for(Chat chat : chats){
            list.add(chat.getEmailClient());
        }
        simpMessagingTemplate.convertAndSendToUser("admin","/get-all",list);
        return list;
    }




}
