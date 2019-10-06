/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;

import com.karlmarxindustries.flooring.dto.Product;
import java.util.List;

/**
 *
 * @author karlmarx
 */
public interface FlooringProductDao {

    public List<Product> getAllProducts() throws FilePersistenceException;

    public Product getProduct(String productType) throws FilePersistenceException;

    public void loadProductInfo(String filename) throws FilePersistenceException;
}
