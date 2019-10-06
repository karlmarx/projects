/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;

import com.karlmarxindustries.flooring.dto.Tax;
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
public class FlooringTaxDaoImplTest {

    FlooringTaxDaoImpl testTDao;

    public FlooringTaxDaoImplTest() {
        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("flooringTestApplicationContext.xml");
        testTDao = ctx.getBean("tDao", FlooringTaxDaoImpl.class);
    }

    @Test
    public void testGetAllTaxes() throws FilePersistenceException {
        testTDao.loadRateList("TaxesTEST.txt");
        List<Tax> allTaxes = testTDao.getAllTaxes();
        List<String> allStateAb = new ArrayList<>();
        List<String> allStates = new ArrayList<>();
        List<BigDecimal> allTaxRates = new ArrayList<>();
        for (Tax each : allTaxes) {
            allStateAb.add(each.getStateAbbreviation());
            allStates.add(each.getStateName());
            allTaxRates.add(each.getTaxRate());
        }
        //stopped here 
        Assertions.assertEquals(4, allTaxes.size(), "there should be 4 taxes in the array list");
        Assertions.assertEquals(4, allStateAb.size(), "there should be 4 state abbrevs  in the  4 state abbrevs  array list");
        Assertions.assertEquals(4, allStates.size(), "there should be 4 state names in the state names array list");
        Assertions.assertEquals(4, allTaxRates.size(), "there should be 4 tax rates in the tax rate array list");

        Assertions.assertTrue(allStateAb.contains("KS"), "KS should be a state abbr");
        Assertions.assertTrue(allStateAb.contains("MB"), "MB  should be a state abbr");
        Assertions.assertTrue(allStateAb.contains("KY"), "KY should be a state abbr");
        Assertions.assertTrue(allStateAb.contains("PR"), "PR should  be a state abbr");
        Assertions.assertFalse(allStateAb.contains("TX"), "TX should not be a state abbr");
        Assertions.assertFalse(allStateAb.contains("StateAbbreviation"), "StateAbbreviation should not be a state abbr");

        Assertions.assertTrue(allStates.contains("Kaohsiung"), "Kaohsiung should be a state abbr");
        Assertions.assertTrue(allStates.contains("Manitoba"), "Manitoba should be a state abbr");
        Assertions.assertTrue(allStates.contains("Kentucky"), "Kentucky should be a state abbr");
        Assertions.assertTrue(allStates.contains("Puerto Rico"), "Puerto Rico should  be a state");
        Assertions.assertFalse(allStates.contains("Texas"), "Texas should not be a state ");
        Assertions.assertFalse(allStates.contains("StateName"), "StateName should not be a state ");
    }

    @Test
    public void testGetTax() throws FilePersistenceException {
        Tax shouldBeNull = testTDao.getTax("KY");
        testTDao.loadRateList("TaxesTEST.txt");
        Tax gottenTax = testTDao.getTax("KY");
        Tax gottenTaxMB = testTDao.getTax("MB");
        Tax shouldBeNullState = testTDao.getTax("TX");

        Assertions.assertEquals("KY", gottenTax.getStateAbbreviation(), "State abbr should be KY.");
        Assertions.assertEquals("Kentucky", gottenTax.getStateName(), "State should be kentucky.");
        Assertions.assertEquals(new BigDecimal("6.00"), gottenTax.getTaxRate(), "Tax rate should be 6.00.");

        Assertions.assertEquals("MB", gottenTaxMB.getStateAbbreviation(), "State abbr should be KY.");
        Assertions.assertEquals("Manitoba", gottenTaxMB.getStateName(), "State should be Manitoba.");
        Assertions.assertEquals(new BigDecimal("9.25"), gottenTaxMB.getTaxRate(), "Tax rate should be 9.25.");

        Assertions.assertNull(shouldBeNull, "empty dao should mean no tax to get.");
        Assertions.assertNull(shouldBeNullState, "Passing TX as parameter should yield null result.");
    }

}
