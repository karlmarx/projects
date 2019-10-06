/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.service;

import com.karlmarxindustries.flooring.dao.FilePersistenceException;
import com.karlmarxindustries.flooring.dto.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author karlmarx
 */
public class FlooringServiceLayerImplTest {

    private FlooringServiceLayerImpl service;
    Order testOrderOne;
    Order testOrderTwo;
    Order testOrderThree;

    public FlooringServiceLayerImplTest() {
        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("flooringTestApplicationContext.xml");
        service = ctx.getBean("serviceLayer", FlooringServiceLayerImpl.class);
    }

    @Test
    public void testCalculateCostsTaxesTotal() throws FilePersistenceException, FlooringDuplicateIdException, AreaTooSmallException {
        Order order = new Order(LocalDate.parse("2019-10-01"), "mister mister", "KY", new BigDecimal("120.00"), "Tile");
        Order calculatedOrder = service.calculateCostsTaxesTotal(order);
        BigDecimal shouldBeArea = new BigDecimal("120.00");
        BigDecimal shouldBeCostPSFTile = new BigDecimal("1.01");
        BigDecimal shouldBeLabourCostPSFTile = new BigDecimal("11.01");
        BigDecimal shouldBeKYTax = new BigDecimal("12.02").divide(new BigDecimal("100.00"));
        BigDecimal shouldBeMaterialCost = shouldBeArea.multiply(shouldBeCostPSFTile).setScale(2, RoundingMode.HALF_UP);
        BigDecimal shouldBeLabourCost = shouldBeArea.multiply(shouldBeLabourCostPSFTile).setScale(2, RoundingMode.HALF_UP);
        BigDecimal shouldBeTax = (shouldBeMaterialCost.add(shouldBeLabourCost)).multiply(shouldBeKYTax).setScale(2, RoundingMode.HALF_UP);
        BigDecimal shouldBeTotal = shouldBeMaterialCost.add(shouldBeLabourCost).add(shouldBeTax).setScale(2, RoundingMode.HALF_UP);

        Assertions.assertEquals(shouldBeMaterialCost, order.getMaterialCost(), "Material cost should match calculation");
        Assertions.assertEquals(shouldBeLabourCost, order.getLabourCost(), "Labour cost should match calculation");
        Assertions.assertEquals(shouldBeTax, order.getTax(), "Tax should match calculation");
        Assertions.assertEquals(shouldBeTotal, order.getTotal(), "Total should match calculation");

    }

    @Test
    public void testGetMatchingOrderCorrectInfo() throws FilePersistenceException {
        try {
            Order matchingOrder = service.getMatchingOrderForDate(LocalDate.parse("2020-12-20"), 1);
            Assertions.assertEquals("Jon Hodgman", matchingOrder.getCustomerName(), "Customer name should be Jon Hodgman");
            Assertions.assertEquals(LocalDate.parse("2020-12-20"), matchingOrder.getDate(), "Date should be 2020-12-20");
            Assertions.assertEquals(1, matchingOrder.getOrderNumber(), "Order number should be 1");
            Assertions.assertEquals("KY", matchingOrder.getState(), "State should be KY");
            Assertions.assertEquals(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_UP), matchingOrder.getArea(), "Area should be 123.45");
            Assertions.assertEquals("Tile", matchingOrder.getProductType(), "Area should be 123.45");
        } catch (NoMatchingOrdersException e) {
            Assertions.fail("Exception [No Matching Orders] was thrown, but should not have been.");
        }

    }

    @Test
    public void testGetMatchingOrderWrongDate() throws FilePersistenceException {
        Order matchingOrder = null;
        try {
            matchingOrder = service.getMatchingOrderForDate(LocalDate.parse("2020-12-31"), 2);
            Assertions.fail("Exception [No Matching Orders] should have been thrown, but wasn't");
        } catch (NoMatchingOrdersException e) {
            //do nothing 
        }
        Assertions.assertNull(matchingOrder, "MatchingOrder should be null.");
    }

    @Test
    public void testGetMatchingOrderWrongOrderNumber() throws FilePersistenceException {
        Order matchingOrder = null;
        try {
            matchingOrder = service.getMatchingOrderForDate(LocalDate.parse("2020-12-20"), 3);
            Assertions.fail("Exception [No Matching Orders] should have been thrown, but wasn't");
        } catch (NoMatchingOrdersException e) {

        }
        Assertions.assertNull(matchingOrder, "MatchingOrder should still be null.");
    }

    @Test
    public void testFirstLetterCapRestLower() {
        String toTest = "lowercase";
        String toTest2 = "UPPERCASE";
        String toTest3 = "mIxEDcase2394";
        String toTest4 = "bACKWARDS";
        Assertions.assertEquals("Lowercase", service.firstLetterCapRestLower(toTest), "Result should be Lowercase");
        Assertions.assertEquals("Uppercase", service.firstLetterCapRestLower(toTest2), "Result should be Uppercase");
        Assertions.assertEquals("Mixedcase2394", service.firstLetterCapRestLower(toTest3), "Result should be Mixedcase2394");
        Assertions.assertEquals("Backwards", service.firstLetterCapRestLower(toTest4), "Result should be Backwards");
    }

    @Test
    public void testThrowExceptionIfAreaTooSmallCalculateAndOrderNumber() throws AreaTooSmallException, FlooringDuplicateIdException, FilePersistenceException {
        try {
            Order wayTooSmall = new Order(LocalDate.parse("2019-10-01"), "farnsworth", "KY", new BigDecimal("0.00"), "Tile");
            service.calculateAndOrderNumber(wayTooSmall);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order tooSmall = new Order(LocalDate.parse("2019-02-01"), "leela", "KY", new BigDecimal("70.00"), "Tile");
            service.calculateAndOrderNumber(tooSmall);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order aLittleSmall = new Order(LocalDate.parse("2019-02-01"), "bender", "KY", new BigDecimal("99.90"), "Tile");
            service.calculateAndOrderNumber(aLittleSmall);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order negativeArea = new Order(LocalDate.parse("2019-02-01"), "bender", "KY", new BigDecimal("-100.00"), "Tile");
            service.calculateAndOrderNumber(negativeArea);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order justBigEnough = new Order(LocalDate.parse("2019-02-01"), "zoidberg", "KY", new BigDecimal("100.00"), "Tile");
            service.calculateAndOrderNumber(justBigEnough);

        } catch (AreaTooSmallException e) {
            Assertions.fail("This exception should not have been thrown: area >100.00 sq ft");
        }
        try {

            Order reallyBig = new Order(LocalDate.parse("2019-02-01"), "hedonism bot", "KY", new BigDecimal("1000000000.00"), "Tile");
            service.calculateAndOrderNumber(reallyBig);
        } catch (AreaTooSmallException e) {
            Assertions.fail("This exception should not have been thrown: area >100.00 sq ft");
        }
        try {
            Order reallyBig = new Order(LocalDate.parse("2019-02-01"), "robot devil", "KY", new BigDecimal("1000000000.00"), "Tile");
            service.calculateAndOrderNumber(reallyBig);
        } catch (AreaTooSmallException e) {
            Assertions.fail("This exception should not have been thrown: area >100.00 sq ft");
        }

    }

    @Test
    public void testThrowExceptionIfAreaTooSmallCalculateCostsTaxesTotal() throws FlooringDuplicateIdException, FilePersistenceException {
        try {
            Order wayTooSmall = new Order(LocalDate.parse("2019-10-01"), "farnsworth", "KY", new BigDecimal("0.00"), "Tile");
            service.calculateCostsTaxesTotal(wayTooSmall);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order tooSmall = new Order(LocalDate.parse("2019-02-01"), "leela", "KY", new BigDecimal("70.00"), "Tile");
            service.calculateCostsTaxesTotal(tooSmall);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order aLittleSmall = new Order(LocalDate.parse("2019-02-01"), "bender", "KY", new BigDecimal("99.90"), "Tile");
            service.calculateCostsTaxesTotal(aLittleSmall);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order negativeArea = new Order(LocalDate.parse("2019-02-01"), "bender", "KY", new BigDecimal("-100.00"), "Tile");
            service.calculateCostsTaxesTotal(negativeArea);
            Assertions.fail("Exception should have been thrown");
        } catch (AreaTooSmallException e) {

        }
        try {
            Order justBigEnough = new Order(LocalDate.parse("2019-02-01"), "zoidberg", "KY", new BigDecimal("100.00"), "Tile");
            service.calculateCostsTaxesTotal(justBigEnough);
          
        } catch (AreaTooSmallException e) {
Assertions.fail("This exception should not have been thrown: area =100.00 sq ft");
        }
        try {

            Order reallyBig = new Order(LocalDate.parse("2019-02-01"), "bender", "KY", new BigDecimal("1000000000.00"), "Tile");
            service.calculateCostsTaxesTotal(reallyBig);
        } catch (AreaTooSmallException e) {
            Assertions.fail("This exception should not have been thrown: area >100.00 sq ft");
        }
        try {
            Order reallyBig = new Order(LocalDate.parse("2019-02-01"), "bender", "KY", new BigDecimal("1000000000.00"), "Tile");
            service.calculateCostsTaxesTotal(reallyBig);
        } catch (AreaTooSmallException e) {
            Assertions.fail("This exception should not have been thrown: area >100.00 sq ft");
        }
    }
}
