package com.application.backend.document;

import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

@Document(collection = "chatsessions")
public class Chat {
    @Id
    private String id;

    @Field("email")
    private String emailClient;

    @Field("contents")
    private List<Map<String, Object>> contents;


    public Chat() {
    }

    public Chat(String emailClient, List<Map<String, Object>> contents) {
        this.emailClient = emailClient;
        this.contents = contents;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public List<Map<String, Object>> getContents() {
        return contents;
    }

    public void setContents(List<Map<String, Object>> contents) {
        this.contents = contents;
    }


    public void addChat(Integer type, String content){
        // type 0 is server side, 1 is client side
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("message", content);

        if(contents == null){
            contents = new ArrayList<>();
        }
        contents.add(data);
    }
}
