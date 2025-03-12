package com.ohgiraffers.transactional.section01.annotation;

public class OrderMenu {
    private int orderCode;
    private int menuCode;
    private int orderAmount;

    public OrderMenu() {
    }

    public OrderMenu(int orderCode, int menuCode, int orderAmount) {
        this.orderCode = orderCode;
        this.menuCode = menuCode;
        this.orderAmount = orderAmount;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Override
    public String toString() {
        return "OrderMenu{" +
                "orderCode=" + orderCode +
                ", menuCode=" + menuCode +
                ", orderAmount=" + orderAmount +
                '}';
    }
}
