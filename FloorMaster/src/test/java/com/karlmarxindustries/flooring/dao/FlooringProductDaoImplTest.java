/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;

import com.karlmarxindustries.flooring.dto.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author karlmarx
 */
public class FlooringProductDaoImplTest {

    FlooringProductDaoImpl testPDao;

    public FlooringProductDaoImplTest() {
        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("flooringTestApplicationContext.xml");
        testPDao = ctx.getBean("pDao", FlooringProductDaoImpl.class);
    }

    @Test
    public void testGetAllProducts() throws FilePersistenceException {
        testPDao.loadProductInfo("ProductsTEST.txt");
        List<Product> allProducts = testPDao.getAllProducts();
        List<String> allProductTypes = new ArrayList<>();
        List<BigDecimal> allCostsPSqFt = new ArrayList<>();
        List<BigDecimal> allLaborCostsPSqFt = new ArrayList<>();
        for (Product each : allProducts) {
            allProductTypes.add(each.getProductType());
            allCostsPSqFt.add(each.getCostPerSquareFoot());
            allLaborCostsPSqFt.add(each.getLaborCostPerSquareFoot());
        }
        Assertions.assertEquals(4, allProducts.size(), "there should be 4 products in the product array list");
        Assertions.assertEquals(4, allProductTypes.size(), "there should be 4 product types in the product type array list");
        Assertions.assertEquals(4, allCostsPSqFt.size(), "there should be 4 costs per square foot in the cost per square foot array list");
        Assertions.assertEquals(4, allLaborCostsPSqFt.size(), "there should be 4 labor costs per square foot in the labor cost per square foot array list");

        Assertions.assertTrue(allProductTypes.contains("WOOD"), "Wood should be a product type");
        Assertions.assertTrue(allProductTypes.contains("LAMINATE"), "Laminate should be a product type");
        Assertions.assertTrue(allProductTypes.contains("TILE"), "Tile should be a product type");
        Assertions.assertFalse(allProductTypes.contains("COTTON"), "Cotton should not be a product type");
        Assertions.assertFalse(allProductTypes.contains("PRODUCTTYPE"), "PRODUCTTYPE should not be a product type");
    }

    @Test
    public void testGetProduct() throws FilePersistenceException {
        Product shouldBeNull = testPDao.getProduct("WOOD");

        testPDao.loadProductInfo("ProductsTEST.txt");
        Product gotNoProduct = testPDao.getProduct("PAPER");
        Product gottenProduct = testPDao.getProduct("WOOD");
        Assertions.assertEquals("WOOD", gottenProduct.getProductType(), "product type should be wood.");
        Assertions.assertEquals(new BigDecimal("5.15"), gottenProduct.getCostPerSquareFoot(), "Cost per sq ft should be 2.25.");
        Assertions.assertEquals(new BigDecimal("4.75"), gottenProduct.getLaborCostPerSquareFoot(), "Labor cost per sq ft should be 2.10.");
        Assertions.assertNull(shouldBeNull, "empty dao should mean no product to get.");
        Assertions.assertNull(gotNoProduct, "product type should be null for Paper.");
    }

}
