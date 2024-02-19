package com.application.backend.dao;

import com.application.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartDao extends JpaRepository<Cart,Long> {
    Cart findById(long id);
    @Query(value="SELECT * FROM Cart WHERE user_id = ?1 and product_item_id = ?2" ,nativeQuery = true)
    Cart findExistedCart(long userId, long itemId);

    @Query(value="SELECT * FROM Cart WHERE user_id = ?1" ,nativeQuery = true)
    List<Cart> getByUser(long userId);
    @Query(value="SELECT count(*) FROM Cart WHERE user_id = ?1" ,nativeQuery = true)
    Integer countCart(long id);

}
