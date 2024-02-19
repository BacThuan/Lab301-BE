package com.application.backend.service.impl;

import com.application.backend.dao.RoleDao;
import com.application.backend.exception.CatchException;
import com.application.backend.model.Role;
import com.application.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    public int getBoolean(boolean check){
        if(check) return 1;
        return 0;
    }

    public Map<String, Object> getListData(List<Role> roles, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, Object>> rolesData = new ArrayList<>();

        for(Role role : roles){
            Map<String, Object> data = new HashMap<>();

            data.put("_id",String.valueOf(role.getId()) );
            data.put("roleName", role.getRoleName());
            data.put("create", role.isCREATE());
            data.put("read", role.isREAD());
            data.put("update", role.isUPDATE());
            data.put("delete", role.isDELETE());

            rolesData.add(data);
        }
        results.put("datas", rolesData);
        results.put("total", total);

        return results;
    }
    @Override
    public Map<String, Object> getRoles(Integer page, Integer limit) {
        List<Role> roles = roleDao.findPageAndLimit(page * limit, limit);
        long total = roleDao.totalPageAndLimit();
        return getListData(roles,total);
    }

    @Override
    public Map<String, Object> getSearching(Integer page, Integer limit, String searching) {
        List<Role> roles = roleDao.findSearching(page * limit, limit, searching);
        long total = roleDao.totalSearching(searching);
        return getListData(roles,total);
    }

    @Override
    public void deleteRole(Long roleId) {
        roleDao.deleteById(roleId);
    }

    @Override
    public List<String> getAll() {
        List<Role> roles = roleDao.findAll();

        List<String> result = new ArrayList<>();
        for(Role role : roles){
            result.add(role.getRoleName());
        }
        return result;
    }

    @Override
    public Map<String, Object> getRole(long idRole) {
        Role role = roleDao.findById(idRole);

            Map<String, Object> data = new HashMap<>();

            data.put("_id",String.valueOf(role.getId()) );
            data.put("roleName", role.getRoleName());
            data.put("create", role.isCREATE());
            data.put("read", role.isREAD());
            data.put("update", role.isUPDATE());
            data.put("delete", role.isDELETE());


        return data;
    }

    @Override
    public void updateRole(long id, String name, boolean create, boolean read, boolean update, boolean delete) {
        Role roleExisted = roleDao.findByRoleName(name);
        if(roleExisted != null && roleExisted.getId() != id)
            throw new CatchException("This role is existed!", HttpStatus.CONFLICT);

        Role role = roleDao.findById(id);

        role.setRoleName(name);
        role.setCREATE(create);
        role.setUPDATE(update);
        role.setREAD(read);
        role.setDELETE(delete);

        roleDao.save(role);
    }

    @Override
    public void createdRole(String name, boolean create, boolean read, boolean update, boolean delete) {
        Role role = roleDao.findByRoleName(name);
        if(role != null) throw new CatchException("This role is existed!", HttpStatus.CONFLICT);

        roleDao.save(new Role(name,create,read,update,delete));
    }

    @Override
    public Map<String, Object> getSearching(Integer page, Integer limit, boolean create, boolean read, boolean update, boolean delete) {

        List<Role> roles = roleDao.findPermission(page * limit, limit,create,read,update,delete);
        long total = roleDao.totalSearchPermission(create,read,update,delete);
        return getListData(roles,total);
    }
}
