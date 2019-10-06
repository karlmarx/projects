/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.dao;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author karlmarx
 */
public class FlooringLoggerDaoImpl implements FlooringLoggerDao {
    public static final String AUDIT_FILE = "logger.txt";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
  

    @Override
    public void writeAuditEntry(String entry, boolean isTesting) throws FilePersistenceException {
            PrintWriter out;
            String mode = "Production";
            if (isTesting) {
                mode = "Testing";
            }
            try {
                    out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
            } catch (IOException e ) { 
                throw new FilePersistenceException("Could not persist audit information.", e);
            }
            LocalDateTime timeStamp = LocalDateTime.now();
            out.println(timeStamp.format(formatter)+ " : [" + entry + "] [Mode = " + mode + "]");
            out.flush();
    }
    
}
