/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;

import com.karlmarxindustries.flooring.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author karlmarx
 */
public class FlooringProductDaoImpl implements FlooringProductDao {

    private Map<String, Product> products = new HashMap<>();
    private static final String DELIMITER = ",";

    Product unmarshallProduct(String productAsText) throws FilePersistenceException {
        String[] productTokens = productAsText.split(DELIMITER);
        String productType = productTokens[0].toUpperCase();
        Product productFromFile = new Product();
        productFromFile.setProductType(productType);
        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));
        return productFromFile;
    }
    
    @Override
    public List<Product> getAllProducts() throws FilePersistenceException {
        return new ArrayList<Product>(products.values());
    }

    @Override
    public Product getProduct(String productType) throws FilePersistenceException {
        return products.get(productType);
    }

   

    @Override
    public void loadProductInfo(String filename) throws FilePersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(filename)));
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("Uh-oh!", e);
        }
        String currentLine;
        Product currentProduct;
        String headerLine = scanner.nextLine(); 
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();
    }

    

}
