package com.application.backend.dao;

import com.application.backend.model.Brand;
import com.application.backend.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorDao extends JpaRepository<Color, Long> {
    Color findById(long id);

    Color findByName(String name);

    @Query(value="SELECT * FROM Color LIMIT ?2 OFFSET ?1" ,nativeQuery = true)
    List<Color> findPageAndLimit(Integer page, Integer limit);

    @Query(value="SELECT count(*) FROM Color" ,nativeQuery = true)
    long totalPageAndLimit();

    @Query(value="SELECT * FROM Color c WHERE c.name LIKE %?3% " +
            "or c.code LIKE %?3% LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Color> findSearching(Integer page, Integer limit, String searching);

    @Query(value="SELECT count(*) FROM Color c WHERE c.name LIKE %?1% or c.code LIKE %?1%", nativeQuery = true)
    long totalSearching(String searching);
}
