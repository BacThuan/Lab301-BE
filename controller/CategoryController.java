package com.application.backend.controller;

import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.CategoryService;
import com.application.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/read/categories/getAll")
    public ResponseEntity<Object> getAllCategories() {
        CheckRole.isRead();

        List<String> categories = categoryService.getAll();


        return Helper.res(categories, HttpStatus.OK);
    }


    @GetMapping("/read/categories")
    public ResponseEntity<Object> getCategories(@RequestParam Integer page, @RequestParam  Integer limit,
                                           @RequestParam (required = false) String searching) {
        CheckRole.isRead();


        Map<String, Object> categories = null;
         if(searching != null) {
             categories = categoryService.getSearching(page, limit, searching);
        }

        else {
             categories = categoryService.getCategories(page, limit);
        }


        return Helper.res(categories, HttpStatus.OK);
    }

    @DeleteMapping("/delete/categories")
    public ResponseEntity<Object> deleteCategory(@RequestParam Long id) {

        CheckRole.isDelete();

        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Success!");
    }

    @PutMapping("/update/category")
    public ResponseEntity<Object> updateBrand(@RequestBody Map<String, Object> data) {

        CheckRole.isUpdate();
        long id = Long.parseLong((String) data.get("_id"));
        String name = (String) data.get("name");

        categoryService.updateCategory(id,name);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/create/category")
    public ResponseEntity<Object> createBrand(@RequestBody Map<String, Object> data) {
        String name = (String) data.get("name");
        CheckRole.isCreate();
        categoryService.createCategory(name);
        return ResponseEntity.ok("Success!");
    }


    // ----------------- client
    @GetMapping("/public/categories/getAll")
    public ResponseEntity<Object> getClientCategories() {
        return Helper.res(categoryService.getAll(), HttpStatus.OK);
    }
}
