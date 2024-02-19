package com.application.backend.dao;

import com.application.backend.model.Brand;
import com.application.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandDao extends JpaRepository<Brand, Long> {

    Brand findById(long id);
    Brand findByName(String name);

    @Query(value="SELECT * FROM Brand LIMIT ?2 OFFSET ?1" ,nativeQuery = true)
    List<Brand> findPageAndLimit(Integer page, Integer limit);

    @Query(value="SELECT count(*) FROM Brand" ,nativeQuery = true)
    long totalPageAndLimit();

    @Query(value="SELECT * FROM Brand b WHERE b.name LIKE %?3% LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Brand> findSearching(Integer page, Integer limit, String searching);

    @Query(value="SELECT count(*) FROM Brand b WHERE b.name LIKE %?1%", nativeQuery = true)
    long totalSearching(String searching);
}
