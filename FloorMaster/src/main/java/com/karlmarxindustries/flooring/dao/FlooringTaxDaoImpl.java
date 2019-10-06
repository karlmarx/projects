/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;

import com.karlmarxindustries.flooring.dto.Tax;
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
public class FlooringTaxDaoImpl implements FlooringTaxDao {

    private Map<String, Tax> taxes = new HashMap<>();
    private static final String DELIMITER = ",";

    Tax unmarshallTax(String taxAsText) throws FilePersistenceException {
        String[] taxTokens = taxAsText.split(DELIMITER);
        String stateAbbreviation = taxTokens[0];
        Tax taxFromFile = new Tax();
        taxFromFile.setStateAbbreviation(stateAbbreviation);
        taxFromFile.setStateName(taxTokens[1]);
        taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));
        return taxFromFile;
    }

    @Override
    public List<Tax> getAllTaxes() throws FilePersistenceException {
        return new ArrayList<Tax>(taxes.values());
    }

    @Override
    public Tax getTax(String stateAbbr) throws FilePersistenceException {
        return taxes.get(stateAbbr);
    }

    @Override
    public List<String> getAllStates() throws FilePersistenceException {
        List<String> states = new ArrayList<>(taxes.keySet());    
       return states;
    }

    @Override
    public void loadRateList(String filename) throws FilePersistenceException {
          Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(filename)));
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("Uh-oh!", e);
        }
        String currentLine;
        Tax currentTax;
        String headerLine = scanner.nextLine(); 
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);
            taxes.put(currentTax.getStateAbbreviation(), currentTax);
        }
        scanner.close();
    }

}
