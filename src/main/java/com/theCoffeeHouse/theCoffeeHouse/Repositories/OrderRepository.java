package com.theCoffeeHouse.theCoffeeHouse.Repositories;

import com.theCoffeeHouse.theCoffeeHouse.Models.Order;
import com.theCoffeeHouse.theCoffeeHouse.Models.OrderByDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Order order SET order.status = ?1 where order.id = ?2")
    public void updateStatus(String status, Long id);

    @Transactional
    @Modifying
    @Query("SELECT order.day as day, sum(order.totalMoney) as totalMoney FROM Order order WHERE order.month = ?1 GROUP BY order.day ORDER BY day")
    Object[] getOrderByDay(String month);

    @Transactional
    @Modifying
    @Query("SELECT order.month as month, sum(order.totalMoney) as totalMoney FROM Order order WHERE order.year = ?1 GROUP BY order.month ORDER BY month")
    Object[] getOrderByMonth(String year);
}
