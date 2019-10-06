/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;

import com.karlmarxindustries.flooring.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author karlmarx
 */
public class FlooringOrderDaoImplTest1 {

    FlooringOrderDaoImpl testODao;

    public FlooringOrderDaoImplTest1() {
        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("flooringTestApplicationContext.xml");
        testODao = ctx.getBean("oDao", FlooringOrderDaoImpl.class);
    }

    @Test
    public void testGetFilenames() {
        String[] filenames = testODao.getOrderFileNames("OrdersTest/");
        List<String> filenamesList =Arrays.asList(filenames);
        Assertions.assertNotNull(filenames, "should not be null, there are files in dir");
        Assertions.assertEquals(2, filenames.length, "there should be two files in dir");
        Assertions.assertTrue(filenamesList.contains("Orders_06012013.txt"), "this filename (Orders_06012013.txt) should be in the list.");
        Assertions.assertTrue(filenamesList.contains("Orders_06022013.txt"), "this filename(Orders_06012013.txt)  should be in the list.");
        Assertions.assertFalse(filenamesList.contains("ProductsTEST.txt"), "this filename (ProductsTEST.txt)  should be in the list.");
    }

    @Test
    public void testCreateOrder() throws FilePersistenceException {
        int orderNumber1 = 21;
        Order orderToAdd = new Order(LocalDate.parse("2020-12-12"), "Bill", "TX", new BigDecimal("101.0"), "TILE");

        Order gotBack = testODao.createOrder(orderNumber1, orderToAdd);
        Order gottenOrder = testODao.getOrder(orderNumber1);

        Assertions.assertEquals(gottenOrder.getDate(), orderToAdd.getDate(), "order dates should match");
        Assertions.assertEquals(gottenOrder.getOrderNumber(), orderToAdd.getOrderNumber(), "order numbers should match");
        Assertions.assertEquals(gottenOrder.getCustomerName(), orderToAdd.getCustomerName(), "customer names should match");
        Assertions.assertEquals(gottenOrder.getState(), orderToAdd.getState(), "states should match");
        Assertions.assertEquals(gottenOrder.getArea(), orderToAdd.getArea(), "areas should match");
        Assertions.assertEquals(gottenOrder.getProductType(), orderToAdd.getProductType(), "order numbers should match");

        Assertions.assertNull(gotBack, "map should have been  empty");
    }

    @Test
    public void testGetAllOrders() throws FilePersistenceException {
        int orderNumber1 = 25;
        Order orderToAdd1 = new Order(LocalDate.parse("9999-01-01"), "Jim", "CA", new BigDecimal("123.0"), "WOOD");

        int orderNumber2 = 223;
        Order orderToAdd2 = new Order(LocalDate.parse("2040-01-01"), "Ted", "KY", new BigDecimal("121.0"), "LAMINATE");

        Order gotBackFirst = testODao.createOrder(orderNumber1, orderToAdd1);
        Order gotBackSecond = testODao.createOrder(orderNumber2, orderToAdd2);

        List<Order> allDaOrders = testODao.getAllOrders();

        Assertions.assertNotNull(allDaOrders, "Our Order list should not be null");
        Assertions.assertEquals(2, allDaOrders.size(), "there should be 2 orders in the list");
        Assertions.assertTrue(allDaOrders.contains(orderToAdd1), "orders list should have the first order  stored");
        Assertions.assertTrue(allDaOrders.contains(orderToAdd2), "orders list shoud have the second order stored");

        Assertions.assertNull(gotBackFirst, "There shouldn't be a order with that key stored in empty dao.");
        Assertions.assertNull(gotBackSecond, "There shouldn't be a order with that key stored in empty dao.");
    }

    @Test
    public void createRemoveOrderTest() throws FilePersistenceException {
        int orderNumber = 1231;
        Order toStore = new Order(LocalDate.parse("2020-01-01"), "Karl", "KY", new BigDecimal("1324.0"), "Carpet");
        testODao.createOrder(orderNumber, toStore);
        Order removed = testODao.removeOrder(orderNumber);
        Order shouldBeNullBcItWasRemoved = testODao.getOrder(orderNumber);

        Assertions.assertEquals(toStore, removed, "stored and removed order should be same");
        Assertions.assertNull(shouldBeNullBcItWasRemoved, " order should no longer be in dao");
    }

    @Test
    public void updateOrderTest() throws FilePersistenceException {
        int orderNumber = 1231546465;
        Order toStore = new Order(LocalDate.parse("2100-01-01"), "Karl", "KY", new BigDecimal("1324.0"), "Carpet");

        Order toStore2 = new Order(LocalDate.parse("2040-01-01"), "Ted", "KY", new BigDecimal("121.0"), "LAMINATE");

        testODao.createOrder(orderNumber, toStore);
        testODao.editOrder(orderNumber, toStore2);

        Order retrieved = testODao.getOrder(orderNumber);
        List<Order> allDaOrders = testODao.getAllOrders();

        Assertions.assertEquals(toStore2, retrieved, "order shoulda been replaced by second");
        Assertions.assertEquals(1, allDaOrders.size(), "there should be 1 order only");
        Assertions.assertTrue(allDaOrders.contains(toStore2));
    }

    @Test
    public void testDeleteFileIfEmpty() {

    }

    @Test
    public void updateOrderWithoutAddingTest() throws FilePersistenceException {
        Order emptyOrder = new Order();
        int orderNumber = 123123;
        testODao.editOrder(orderNumber, emptyOrder);
        List<Order> shouldBeEmpty = testODao.getAllOrders();
        Assertions.assertTrue(shouldBeEmpty.isEmpty(), "editing with empty order should not have added an order.");
    }

    @Test
    public void unMarshallOrderTest() throws FilePersistenceException {
        String orderLine = "1,Bender Bending Rodriguez,KY,25.00,Wood,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06";

        Order fromLine = testODao.unmarshallOrder(orderLine);

        Assertions.assertEquals(1, fromLine.getOrderNumber(), "order number should be 1");
        Assertions.assertEquals("Bender Bending Rodriguez", fromLine.getCustomerName(), "customer name should be Bender Bending Rodriguez");
        Assertions.assertEquals("KY", fromLine.getState(), "State should be ky");
        Assertions.assertEquals(new BigDecimal("25.00"), fromLine.getTaxRate(), "Tax rate should be 25.00");
        Assertions.assertEquals("Wood", fromLine.getProductType(), "Product type should be wood");
        Assertions.assertEquals(new BigDecimal("249.00"), fromLine.getArea(), "Area should be 249.00");
        Assertions.assertEquals(new BigDecimal("3.50"), fromLine.getCostPerSquareFoot(), "Cost per sq ft should be 3.50");
        Assertions.assertEquals(new BigDecimal("4.15"), fromLine.getLabourCostPerSquareFoot(), "labor cost per sq ft should be 4.15");
        Assertions.assertEquals(new BigDecimal("871.50"), fromLine.getMaterialCost(), "material cost should be 871.50");
        Assertions.assertEquals(new BigDecimal("1033.35"), fromLine.getLabourCost(), "Labour cost should be 1033.53");
        Assertions.assertEquals(new BigDecimal("476.21"), fromLine.getTax(), "Tax should be 476.21");
        Assertions.assertEquals(new BigDecimal("2381.06"), fromLine.getTotal(), "Total should be 2381.06");

    }

    @Test
    public void marshallOrderTest() {
        Order toTextify = new Order(LocalDate.parse("1983-10-26"), "Stephin Merritt", "NY", new BigDecimal(250.00).setScale(2), "Wood");
        toTextify.setOrderNumber(193);
        toTextify.setTaxRate(new BigDecimal("0.1"));
        toTextify.setCostPerSquareFoot(new BigDecimal("1.99"));
        toTextify.setLaborCostPerSquareFoot(new BigDecimal("1000.00"));
        toTextify.setMaterialCost(new BigDecimal("123123.00"));
        toTextify.setLaborCost(new BigDecimal("99999.00"));
        toTextify.setTax(new BigDecimal("4.00"));
        toTextify.setTotal(new BigDecimal("123313123123.00"));

        String orderAsText = testODao.marshallOrder(toTextify);

        Assertions.assertEquals("193,Stephin Merritt,NY,0.1,Wood,250.00,1.99,1000.00,123123.00,99999.00,4.00,123313123123.00", orderAsText, "lines should match.");

    }

    @Test
    public void circleOfMarshallingTest() throws FilePersistenceException {
        Order toTextify = new Order(LocalDate.parse("1983-10-26"), "null,null,null", "ky", new BigDecimal(250.00).setScale(2), "Wood");
        toTextify.setOrderNumber(193);
        toTextify.setTaxRate(new BigDecimal("0.1"));
        toTextify.setCostPerSquareFoot(new BigDecimal("1.99"));
        toTextify.setLaborCostPerSquareFoot(new BigDecimal("3.00"));
        toTextify.setMaterialCost(new BigDecimal("5.00"));
        toTextify.setLaborCost(new BigDecimal("1.00"));
        toTextify.setTax(new BigDecimal("4.00"));
        toTextify.setTotal(new BigDecimal("7.00"));

        String orderAsText = testODao.marshallOrder(toTextify);
        Order orderFromText = testODao.unmarshallOrder(orderAsText);
        Assertions.assertEquals(toTextify, orderFromText, "input/output should be same");
    }

}
