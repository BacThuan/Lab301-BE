package com.application.backend.dao;

import com.application.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {
    @Query(value="select * from user_order where user_id = ?1 ORDER BY updated_at DESC" ,nativeQuery = true)
    List<Order> findByUser(long id);

    Order findById(long orderId);

    @Query(value="SELECT o.* FROM user_order o  " +
            "inner join state ds on o.delivery_state_id = ds.id "+
            "inner join state ps on o.payment_state_id = ps.id "+
            "where (ds.state_name like %?3%) and (ps.state_name like %?4%) " +
            "and (o.email like %?5% or o.phone like %?5%) " +
            "ORDER BY o.id DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Order> findPageAndLimit(int page, int limit, String deliveryState, String paymentState, String searching);
    @Query(value="SELECT count(*) FROM user_order o  " +
            "inner join state ds on o.delivery_state_id = ds.id "+
            "inner join state ps on o.payment_state_id = ps.id "+
            "where (ds.state_name like %?1%) and (ps.state_name like %?2%) " +
            "and (o.email like %?3% or o.phone like %?3%)", nativeQuery = true)
    long totalPageAndLimit(String deliveryState, String paymentState, String searching);


    @Query(value="SELECT count(*) FROM user_order o " +
            "inner join state s on o.payment_state_id = s.id " +
            "where YEAR(o.updated_at) = ?1 AND MONTH(o.updated_at) = ?2 and s.state_name = 'PAYMENT_PAID'" , nativeQuery = true)
    long countMonthData(int year, int month);

    @Query(value="SELECT sum(total) FROM user_order o " +
            "inner join state s on o.payment_state_id = s.id " +
            "where YEAR(o.updated_at) = ?1 AND MONTH(o.updated_at) = ?2 and s.state_name = 'PAYMENT_PAID'", nativeQuery = true)
    Long countEarningData(int year, int month);

    @Query(value="SELECT o.* FROM user_order o where YEAR(o.updated_at) = ?1 AND MONTH(o.updated_at) = ?2 ORDER BY o.id DESC LIMIT 8 OFFSET 0", nativeQuery = true)
    List<Order> nearestEightOrder(int year, int month);

    @Query(value="select sum(o.total) from user_order o " +
            "inner join state s on o.payment_state_id = s.id " +
            "WHERE YEAR(o.updated_at) = ?1 and s.state_name = 'PAYMENT_PAID' GROUP BY MONTH(o.updated_at)" ,nativeQuery = true)
    List<Long> totalSales(Integer year);

    @Query(value="select count(*) from user_order o " +
            "inner join state s on o.payment_state_id = s.id " +
            "WHERE YEAR(o.updated_at) = ?1 and s.state_name = 'PAYMENT_PAID' GROUP BY MONTH(o.updated_at)" ,nativeQuery = true)
    List<Long> totalOrders(Integer year);
}
