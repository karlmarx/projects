/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.service;

/**
 *
 * @author karlmarx
 */
public class FlooringDuplicateIdException extends Exception {

    public FlooringDuplicateIdException(String message) {
        super(message);
    }

    public FlooringDuplicateIdException(String message,
            Throwable cause) {
        super(message, cause);
    }

}
