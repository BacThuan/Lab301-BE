package com.application.backend.dao;


import com.application.backend.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductItemDao extends JpaRepository<ProductItem, Long> {
    ProductItem findById(long id);

    @Query(value="SELECT i.* FROM Product_item i " +
            "inner join product p on i.product_id = p.id "+
            "inner join state s on i.state_id = s.id "+
            "inner join color c on i.color_id = c.id "+
            "WHERE (?1 IS NULL OR p.id = ?1) and " +
            "(p.name like %?2%) and " +
            "(?3 IS NULL OR i.quantity = ?3) and " +
            "(?4 IS NULL OR i.size = ?4) and " +
            "(s.state_name like %?5%) and " +
            "(c.name like %?6%) and " +
            "(?7 IS NULL OR i.price < ?7) and " +
            "(?8 IS NULL OR i.price >= ?8) "+
            "ORDER BY i.id DESC LIMIT ?10 OFFSET ?9", nativeQuery = true)
    List<ProductItem> getFilter(Long idProduct, String searching, Integer quantity, Integer size,String state, String color, Long lt, Long gt, Integer page, Integer limit);

    @Query(value="SELECT count(*) FROM Product_item i " +
            "inner join product p on i.product_id = p.id "+
            "inner join state s on i.state_id = s.id "+
            "inner join color c on i.color_id = c.id "+
            "WHERE (?1 IS NULL OR p.id = ?1) and "+
            "(p.name like %?2%) and " +
            "(?3 IS NULL OR i.quantity = ?3) and "+
            "(?4 IS NULL OR i.size = ?4) and " +
            "(s.state_name like %?5%) and " +
            "(c.name like %?6%) and "+
            "(?7 IS NULL OR i.price < ?7) and " +
            "(?8 IS NULL OR i.price >= ?8) "
            , nativeQuery = true)
    long totalFilter(Long idProduct, String searching, Integer quantity, Integer size ,String state, String color, Long lt, Long gt);


}
