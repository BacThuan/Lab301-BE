package com.application.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.backend.model.User;

import java.util.Date;
import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long>{
     User findById(long id);

     User findByEmail(String email);

     @Query(value="SELECT * FROM User u " +
             "where u.email like %?4% or u.phone_number like %?4% and " +
             "(?3 IS NULL OR u.last_login < ?3) " +
             "LIMIT ?2 OFFSET ?1" ,nativeQuery = true)
     List<User> findPageAndLimit(Integer page, Integer limit, Date datePrevious, String searching);

     @Query(value="SELECT count(*) FROM User u " +
             "where (?1 IS NULL OR u.last_login < ?1) and " +
             "u.email like %?2% or u.phone_number like %?2% "
             ,nativeQuery = true)
     long totalPageAndLimit(Date datePrevious, String searching);

     void deleteByEmail(String email);
}
