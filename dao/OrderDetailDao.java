package com.application.backend.dao;

import com.application.backend.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailDao extends JpaRepository<OrderDetail, Long> {
    @Query(value="select * from order_detail where order_id = ?1" ,nativeQuery = true)
    List<OrderDetail> findByOrder(long orderId);
}
