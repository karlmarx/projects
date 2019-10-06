/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.service;

import com.karlmarxindustries.flooring.dao.FilePersistenceException;
import com.karlmarxindustries.flooring.dao.FlooringOrderDao;
import com.karlmarxindustries.flooring.dao.TestingModeException;
import com.karlmarxindustries.flooring.dto.Order;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author karlmarx
 */
class FlooringODaoStubImpl implements FlooringOrderDao {
    Order firstOrderDate1;
    Order secondOrderDate1;
    Order thirdOrderDate2;
    Map<Integer, Order> orders = new HashMap<>();

    public FlooringODaoStubImpl(Order firstOrderDate1, Order secondOrderDate1, Order thirdOrderDate2) {
        this.firstOrderDate1 = firstOrderDate1;
        this.secondOrderDate1 = secondOrderDate1;
        this.thirdOrderDate2 = thirdOrderDate2;
    }

    @Override
    public Order createOrder(Integer orderNumber, Order order) throws FilePersistenceException {
        Order newOrder = orders.put(orderNumber, order);
        return newOrder;
    }

    @Override
    public void editOrder(Integer orderNumber, Order order) throws FilePersistenceException {
        orders.replace(orderNumber, order);
    }

    @Override
    public Order removeOrder(Integer orderNumber) throws FilePersistenceException {
        Order removedOrder = orders.remove(orderNumber);
        return removedOrder;
    }

    @Override
    public List<Order> getAllOrdersForDate(LocalDate date) throws FilePersistenceException {
        List<Order> ordersOnDate = new ArrayList<>();
        if (date.compareTo(firstOrderDate1.getDate()) == 0) {
            ordersOnDate.add(firstOrderDate1);
             ordersOnDate.add(secondOrderDate1);
        } else if (date.compareTo(thirdOrderDate2.getDate()) == 0) {
            ordersOnDate.add(thirdOrderDate2);
        }
        return ordersOnDate;
    }

    @Override
    public List<Order> getAllOrders() throws FilePersistenceException {
       List<Order> allOrders = new ArrayList<>();  
       allOrders.add(firstOrderDate1);
       allOrders.add(secondOrderDate1);
       allOrders.add(thirdOrderDate2);
       return allOrders;
    }

    @Override
    public Order getOrder(Integer orderNumber) throws FilePersistenceException {
        Order order = null;
        if (orderNumber == 1) {
            order = firstOrderDate1;
        }
        if (orderNumber == 2) {
            order = secondOrderDate1;
        }
        if (orderNumber == 7) {
            order = thirdOrderDate2;
        }
        return order;
    }

    

    @Override
    public void loadOrders() throws FilePersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isModeTesting() throws FilePersistenceException {
        return true;
    }

    @Override
    public void saveAllOrders() throws FilePersistenceException, TestingModeException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
