package com.application.backend.dao;

import com.application.backend.model.Role;
import com.application.backend.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateDao extends JpaRepository<State, Long>{

    State findById(long id);
    State findByStateName(String stateName);

    @Query(value="SELECT * FROM State LIMIT ?2 OFFSET ?1" ,nativeQuery = true)
    List<State> findPageAndLimit(Integer page, Integer limit);

    @Query(value="SELECT count(*) FROM State" ,nativeQuery = true)
    long totalPageAndLimit();

    @Query(value="SELECT * FROM State s WHERE s.state_name LIKE %?3% LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<State> findSearching(Integer page, Integer limit, String searching);

    @Query(value="SELECT count(*) FROM State s WHERE s.state_name LIKE %?1%", nativeQuery = true)
    long totalSearching(String searching);

    @Query(value="SELECT * FROM State WHERE State_Name like ?1%" ,nativeQuery = true)
    List<State> findStateProduct(String type);
    @Query(value="SELECT s.state_name FROM State s WHERE s.state_name like ?1%" ,nativeQuery = true)
    List<String> getState(String state);
}
