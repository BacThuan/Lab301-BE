package com.application.backend.dao;

import com.application.backend.model.Brand;
import com.application.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category, Long> {

    Category findById(long id);

    Category findByName(String name);

    @Query(value="SELECT * FROM Category LIMIT ?2 OFFSET ?1" ,nativeQuery = true)
    List<Category> findPageAndLimit(Integer page, Integer limit);

    @Query(value="SELECT count(*) FROM Category" ,nativeQuery = true)
    long totalPageAndLimit();

    @Query(value="SELECT * FROM Category c WHERE c.name LIKE %?3% LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Category> findSearching(Integer page, Integer limit, String searching);

    @Query(value="SELECT count(*) FROM Category c WHERE c.name LIKE %?1%", nativeQuery = true)
    long totalSearching(String searching);
}
