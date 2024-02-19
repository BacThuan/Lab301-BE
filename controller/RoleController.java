package com.application.backend.controller;

import com.application.backend.helper.CheckRole;
import com.application.backend.helper.Helper;
import com.application.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/read/roles/getAll")
    public ResponseEntity<Object> getAllRoles() {
        CheckRole.isRead();

        List<String> roles = roleService.getAll();


        return Helper.res(roles, HttpStatus.OK);
    }


    @GetMapping("/read/roles")
    public ResponseEntity<Object> getRoles(@RequestParam Integer page, @RequestParam  Integer limit,
                                           @RequestParam (required = false) String searching,
                                           @RequestParam (required = false) String create,@RequestParam (required = false) String read,
                                           @RequestParam (required = false) String update,@RequestParam (required = false) String delete) {
        CheckRole.isRead();


        Map<String, Object> roles = null;
         if(searching != null) {
             roles = roleService.getSearching(page, limit, searching);
        }

         else if(create != null){

             boolean isCreate = Boolean.parseBoolean(create);
             boolean isRead = Boolean.parseBoolean(read);
             boolean isUpdate= Boolean.parseBoolean(update);
             boolean isDelete = Boolean.parseBoolean(delete);

             roles = roleService.getSearching(page, limit, isCreate,isRead,isUpdate,isDelete);
         }

        else {
             roles = roleService.getRoles(page, limit);
        }


        return Helper.res(roles, HttpStatus.OK);
    }

    @GetMapping("/read/role")
    public ResponseEntity<Object> getRole(@RequestParam long idRole) {
        CheckRole.isRead();

        return Helper.res(roleService.getRole(idRole), HttpStatus.OK);
    }

    // only role with all 4 permission can do this
    @DeleteMapping("/delete/roles")
    public ResponseEntity<Object> deleteRole(@RequestParam Long idRole) {

        CheckRole.isRead();
        CheckRole.isUpdate();
        CheckRole.isCreate();
        CheckRole.isDelete();

        roleService.deleteRole(idRole);
        return ResponseEntity.ok("Success!");
    }

    @PutMapping("/update/role")
    public ResponseEntity<Object> updateRole(@RequestBody Map<String, Object> data) {

        CheckRole.isRead();
        CheckRole.isUpdate();
        CheckRole.isCreate();
        CheckRole.isDelete();

        long id = Long.parseLong((String) data.get("id"));
        String name = (String) data.get("name");
        boolean create = (Boolean) data.get("create");
        boolean read = (Boolean) data.get("read");
        boolean update = (Boolean) data.get("update");
        boolean delete = (Boolean) data.get("delete");

        roleService.updateRole(id,name,create,read,update,delete);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/create/role")
    public ResponseEntity<Object> createRole(@RequestBody Map<String, Object> data) {

        CheckRole.isRead();
        CheckRole.isUpdate();
        CheckRole.isCreate();
        CheckRole.isDelete();

        String name = (String) data.get("name");
        boolean create = data.get("create") != null ? (Boolean) data.get("create") : false;
        boolean read = data.get("read") != null ? (Boolean) data.get("read") : false;
        boolean update = data.get("update") != null ? (Boolean) data.get("update") : false;
        boolean delete = data.get("delete") != null ? (Boolean) data.get("delete") : false;

        roleService.createdRole(name,create,read,update,delete);
        return ResponseEntity.ok("Success!");
    }

}
