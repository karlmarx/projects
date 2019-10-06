/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.service;

import com.karlmarxindustries.flooring.dao.FilePersistenceException;
import com.karlmarxindustries.flooring.dao.FlooringTaxDao;
import com.karlmarxindustries.flooring.dto.Tax;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author karlmarx
 */
class FlooringTDaoStubImpl implements FlooringTaxDao {

    private Map<String, Tax> taxes = new HashMap<>();
    Tax firstTax;
    Tax secondTax;

    public FlooringTDaoStubImpl(Tax firstTax, Tax secondTax) {
        this.firstTax = firstTax;
        this.secondTax = secondTax;
    }

    @Override
    public List<Tax> getAllTaxes() throws FilePersistenceException {
        List<Tax> theStubTaxes = new ArrayList<>();
        theStubTaxes.add(firstTax);
        theStubTaxes.add(secondTax);
        return theStubTaxes;
    }

    @Override
    public Tax getTax(String stateAbbr) throws FilePersistenceException {

        if (stateAbbr.equalsIgnoreCase(firstTax.getStateAbbreviation())) {
            return firstTax;
        } else if (stateAbbr.equalsIgnoreCase(secondTax.getStateAbbreviation())) {
            return secondTax;
        } else {
            return null;
        }
    }

    @Override
    public List<String> getAllStates() throws FilePersistenceException {
        List<String> theStubStates = new ArrayList<>();
        List<Tax> theTaxes = this.getAllTaxes();
        for (Tax tax : theTaxes) {
            theStubStates.add(tax.getStateAbbreviation());
        }
        return theStubStates;
    }

    @Override
    public void loadRateList(String filename) throws FilePersistenceException {
        taxes.put(firstTax.getStateAbbreviation(), firstTax);
        taxes.put(secondTax.getStateAbbreviation(), secondTax);
    }

}
