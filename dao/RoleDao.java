package com.application.backend.dao;

import com.application.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role, Long>{

    Role findByRoleName(String roleName);

    Role findById(long roleId);
    @Query(value="SELECT * FROM Role LIMIT ?2 OFFSET ?1" ,nativeQuery = true)
    List<Role> findPageAndLimit(Integer page, Integer limit);

    @Query(value="SELECT count(*) FROM Role" ,nativeQuery = true)
    long totalPageAndLimit();

    @Query(value="SELECT * FROM Role r WHERE r.role_name LIKE %?3% LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Role> findSearching(Integer page, Integer limit, String searching);

    @Query(value="SELECT count(*) FROM Role r WHERE r.role_name LIKE %?1%", nativeQuery = true)
    long totalSearching(String searching);

    @Query(value="SELECT * FROM Role r WHERE r.can_create = ?3 and " +
            "r.can_read = ?4 and "+
            "r.can_update = ?5 and "+
            "r.can_delete = ?6 "+
            "LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Role> findPermission(Integer page, Integer limit, boolean create, boolean read, boolean update, boolean delete);

    @Query(value="SELECT count(*) FROM Role r WHERE r.can_create = ?1 and " +
            "r.can_read = ?2 and "+
            "r.can_update = ?3 and "+
            "r.can_delete = ?4 ", nativeQuery = true)
    long totalSearchPermission(boolean create, boolean read, boolean update, boolean delete);


}
