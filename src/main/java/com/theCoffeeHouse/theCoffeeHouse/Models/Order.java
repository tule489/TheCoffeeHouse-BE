package com.theCoffeeHouse.theCoffeeHouse.Models;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "tbOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String totalMoney;
    private String dateTime;
    private String paymentMethods;
    @Value("${status: Đang chuẩn bị đơn hàng}")
    private String status;
    @Value("${status: Chưa thanh toán}")
    private String isPaid;

    public Order() {
    }

    public Order(String customerName, String phoneNumber, String address, String totalMoney, String dateTime, String paymentMethods, String status, String isPaid) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.totalMoney = totalMoney;
        this.dateTime = dateTime;
        this.paymentMethods = paymentMethods;
        this.status = status;
        this.isPaid = isPaid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }
}
