/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.service;

import com.karlmarxindustries.flooring.dao.FilePersistenceException;
import com.karlmarxindustries.flooring.dao.FlooringLoggerDao;

/**
 *
 * @author karlmarx
 */
public class FlooringLDaoStubImpl implements FlooringLoggerDao{

  

    public FlooringLDaoStubImpl() {
    }

    @Override
    public void writeAuditEntry(String entry, boolean isTesting) throws FilePersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
