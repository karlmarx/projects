/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;

import com.karlmarxindustries.flooring.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author karlmarx
 */
public interface FlooringOrderDao {

    public Order createOrder(Integer orderNumber, Order order) throws FilePersistenceException;

    public void editOrder(Integer orderNumber, Order order) throws FilePersistenceException;

    public Order removeOrder(Integer orderNumber) throws FilePersistenceException;

    public List<Order> getAllOrdersForDate(LocalDate date) throws FilePersistenceException;

    public List<Order> getAllOrders() throws FilePersistenceException;

    public Order getOrder(Integer orderNumber) throws FilePersistenceException;

    public void loadOrders() throws FilePersistenceException;

    public boolean isModeTesting() throws FilePersistenceException;

    public void saveAllOrders() throws FilePersistenceException, TestingModeException;
}
