/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;


/**
 *
 * @author karlmarx
 */
public interface FlooringLoggerDao {
    public void writeAuditEntry(String entry, boolean isTesting) throws FilePersistenceException;
}
