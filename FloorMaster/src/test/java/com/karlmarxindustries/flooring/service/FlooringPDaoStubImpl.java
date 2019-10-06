/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.service;

import com.karlmarxindustries.flooring.dao.FilePersistenceException;
import com.karlmarxindustries.flooring.dao.FlooringProductDao;
import com.karlmarxindustries.flooring.dto.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author karlmarx
 */
class FlooringPDaoStubImpl implements FlooringProductDao {

    Product firstProduct;
    Product secondProduct;
    Map<String, Product> products = new HashMap<>();

    public FlooringPDaoStubImpl(Product firstProduct, Product secondProduct) {
        this.firstProduct = firstProduct;
        this.secondProduct = secondProduct;
    }

    @Override
    public List<Product> getAllProducts() throws FilePersistenceException {
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(firstProduct);
        allProducts.add(secondProduct);
        return allProducts;
    }

    @Override
    public Product getProduct(String productType) throws FilePersistenceException {
        if (productType.equalsIgnoreCase(firstProduct.getProductType())) {
            return firstProduct;
        } else if (productType.equalsIgnoreCase(secondProduct.getProductType())) {
            return secondProduct;
        } else {
            return null;
        }
    }

    @Override
    public void loadProductInfo(String filename) throws FilePersistenceException {
        products.put(firstProduct.getProductType(), firstProduct);
        products.put(secondProduct.getProductType(), secondProduct);
    }

}
