package com.application.backend.dao;


import com.application.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long> {
    Product findById(long id);

    @Query(value="select p.* from product p " +
            "inner join brand b on p.brand_id = b.id " +
            "inner join category c on p.category_id = c.id " +
            "where b.name like %?1% and " +
            "c.name like %?2% and " +
            "p.gender like ?3% and " +
            "p.name LIKE %?4% order by p.id desc " +
            "LIMIT ?6 OFFSET ?5", nativeQuery = true)
    List<Product> findFilter(String brand, String category, String gender, String searching, Integer page, Integer limit);

    @Query(value="select count(*) from product p " +
            "inner join brand b on p.brand_id = b.id " +
            "inner join category c on p.category_id = c.id " +
            "where b.name like %?1% and " +
            "c.name like %?2% and " +
            "p.gender like ?3% and " +
            "p.name LIKE %?4%", nativeQuery = true)
    long totalFilter(String brand, String category, String gender, String searching);


    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join color c on c.id = i.color_id order by i.price desc limit 8", nativeQuery = true)
    List<String> getEightHighest();


    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join brand b on b.id = p.brand_id " +
            "inner join category cate on cate.id = p.category_id " +
            "inner join color c on c.id = i.color_id " +
            "where b.name like %?2% and " +
            "p.name like %?7% and " +
            "cate.name like %?3% and " +
            "c.name like %?4% " +
            "and (?6 IS NULL OR i.price < ?6) " +
            "and (?5 IS NULL OR i.price >= ?5) "+
            "limit 9 offset ?1 "
            ,nativeQuery = true)
    List<String> getShopProduct(int page, String brand, String category, String color, Long gt, Long lt , String product);

    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join brand b on b.id = p.brand_id " +
            "inner join category cate on cate.id = p.category_id " +
            "inner join color c on c.id = i.color_id " +
            "where b.name like %?2% and " +
            "p.name like %?7% and " +
            "cate.name like %?3% and " +
            "c.name like %?4% " +
            "and (?6 IS NULL OR i.price < ?6) " +
            "and (?5 IS NULL OR i.price >= ?5) "+
            "order by p_name asc limit 9 offset ?1 "
            ,nativeQuery = true)
    List<String> getShopProductSortA(int i, String brand, String category, String color, Long gt, Long lt, String product);
    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join brand b on b.id = p.brand_id " +
            "inner join category cate on cate.id = p.category_id " +
            "inner join color c on c.id = i.color_id " +
            "where b.name like %?2% and " +
            "p.name like %?7% and " +
            "cate.name like %?3% and " +
            "c.name like %?4% " +
            "and (?6 IS NULL OR i.price < ?6) " +
            "and (?5 IS NULL OR i.price >= ?5) "+
            "order by p_name desc limit 9 offset ?1 "
            ,nativeQuery = true)
    List<String> getShopProductSortZ(int i, String brand, String category, String color, Long gt, Long lt, String product);
    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join brand b on b.id = p.brand_id " +
            "inner join category cate on cate.id = p.category_id " +
            "inner join color c on c.id = i.color_id " +
            "where b.name like %?2% and " +
            "p.name like %?7% and " +
            "cate.name like %?3% and " +
            "c.name like %?4% " +
            "and (?6 IS NULL OR i.price < ?6) " +
            "and (?5 IS NULL OR i.price >= ?5) "+
            "order by i.price asc limit 9 offset ?1 "
            ,nativeQuery = true)
    List<String> getShopProductShortSmall(int i, String brand, String category, String color, Long gt, Long lt, String product);

    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join brand b on b.id = p.brand_id " +
            "inner join category cate on cate.id = p.category_id " +
            "inner join color c on c.id = i.color_id " +
            "where b.name like %?2% and " +
            "p.name like %?7% and " +
            "cate.name like %?3% and " +
            "c.name like %?4% " +
            "and (?6 IS NULL OR i.price < ?6) " +
            "and (?5 IS NULL OR i.price >= ?5) "+
            "order by i.price desc limit 9 offset ?1 "
            ,nativeQuery = true)
    List<String> getShopProductSortHigh(int i, String brand, String category, String color, Long gt, Long lt, String product);

    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join color c on c.id = i.color_id " +
            "where p.category_id = ?1 "+
            "ORDER BY RAND ( ) limit 5", nativeQuery = true)
    List<String> getSameCategory(long categoryId);

    @Query(value="select p.gender, p.name as p_name, c.name as c_name, i.primary_image, i.price, i.id, p.id from product p " +
            "inner join product_item i on p.id = i.product_id " +
            "inner join color c on c.id = i.color_id " +
            "where p.brand_id = ?1 "+
            "ORDER BY RAND ( ) limit 5", nativeQuery = true)
    List<String> getSameBrand(long brandId);
}
