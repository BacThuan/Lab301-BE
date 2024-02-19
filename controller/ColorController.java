package com.application.backend.controller;

import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ColorController {
    @Autowired
    ColorService colorService;

    @GetMapping("/read/colors/getAll")
    public ResponseEntity<Object> getAllColors() {
        CheckRole.isRead();

        List<Map<String, String>> colors = colorService.getAll();


        return Helper.res(colors, HttpStatus.OK);
    }

    @GetMapping("/read/colors")
    public ResponseEntity<Object> getColors(@RequestParam Integer page, @RequestParam  Integer limit,
                                           @RequestParam (required = false) String searching) {
        CheckRole.isRead();


        Map<String, Object> colors = null;
        if(searching != null) {
            colors = colorService.getSearching(page, limit, searching);
        }


        else {
            colors = colorService.getColors(page, limit);
        }


        return Helper.res(colors, HttpStatus.OK);
    }

    @DeleteMapping("/delete/colors")
    public ResponseEntity<Object> deleteColor(@RequestParam Long id) {

        CheckRole.isDelete();

        colorService.deleteColor(id);
        return ResponseEntity.ok("Success!");
    }

    @PutMapping("/update/color")
    public ResponseEntity<Object> updateColor(@RequestBody Map<String, Object> data) {

        CheckRole.isUpdate();
        long id = Long.parseLong((String) data.get("_id"));
        String name = (String) data.get("name");
        String code = (String) data.get("code");
        colorService.updateColor(id,name,code);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/create/color")
    public ResponseEntity<Object> createColor(@RequestBody Map<String, Object> data) {

        CheckRole.isCreate();
        String name = (String) data.get("name");
        String code = (String) data.get("code");
        colorService.createColor(name,code);
        return ResponseEntity.ok("Success!");
    }
    // ----------------- client
    @GetMapping("/public/colors/getAll")
    public ResponseEntity<Object> getClientCategories() {
        return Helper.res(colorService.getAll(), HttpStatus.OK);
    }
}
