package com.application.backend.controller;

import com.application.backend.document.FavouriteList;
import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.FavouriteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class FavouriteListController {
    @Autowired
    FavouriteListService favouriteListService;

    @PostMapping("/user/like")
    public ResponseEntity<Object> like (@RequestParam("email")  String email,
                                        @RequestParam("idProduct") Long idProduct){

        favouriteListService.addLike(email, idProduct);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/user/unlike")
    public ResponseEntity<Object> unlike (@RequestParam("email")  String email,
                                          @RequestParam("idProduct") Long idProduct){

        favouriteListService.removeLike(email, idProduct);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/user/get-likes-data")
    public ResponseEntity<Object> getLikeData(@RequestParam("email")  String email) {
        List<Object> data = favouriteListService.getListLike(email);

        return Helper.res(data, HttpStatus.OK);
    }

    @GetMapping("/user/get-list-likes")
    public ResponseEntity<Object> getListLikesId(@RequestParam("email")  String email) {
        List<Long> data = favouriteListService.getListLikeId(email);

        return Helper.res(data, HttpStatus.OK);
    }

}
