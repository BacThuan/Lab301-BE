package com.application.backend.controller;

import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.BrandService;
import com.application.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BrandController {
    @Autowired
    BrandService brandService;
    @GetMapping("/read/brands/getAll")
    public ResponseEntity<Object> getAllBrands() {
        CheckRole.isRead();

        List<String> brands = brandService.getAll();
        return Helper.res(brands, HttpStatus.OK);
    }


    @GetMapping("/read/brands")
    public ResponseEntity<Object> getBrands(@RequestParam Integer page, @RequestParam  Integer limit,
                                           @RequestParam (required = false) String searching) {
        CheckRole.isRead();


        Map<String, Object> brand = null;
         if(searching != null) {
             brand = brandService.getSearching(page, limit, searching);
        }


        else {
             brand = brandService.getBrands(page, limit);
        }


        return Helper.res(brand, HttpStatus.OK);
    }



    @DeleteMapping("/delete/brands")
    public ResponseEntity<Object> deleteBrand(@RequestParam Long id) {

        CheckRole.isDelete();

        brandService.deleteBrand(id);
        return ResponseEntity.ok("Success!");
    }

    @PutMapping("/update/brand")
    public ResponseEntity<Object> updateBrand(@RequestBody Map<String, Object> data) {

        CheckRole.isUpdate();
        long id = Long.parseLong((String) data.get("_id"));
        String name = (String) data.get("name");

        brandService.updateBrand(id,name);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/create/brand")
    public ResponseEntity<Object> createBrand(@RequestBody Map<String, Object> data) {
        String name = (String) data.get("name");
        CheckRole.isCreate();
        brandService.createBrand(name);
        return ResponseEntity.ok("Success!");
    }


    // --------------------- client
    @GetMapping("/public/brands/getAll")
    public ResponseEntity<Object> getClientBrands() {
        return Helper.res(brandService.getAll(), HttpStatus.OK);
    }

}
