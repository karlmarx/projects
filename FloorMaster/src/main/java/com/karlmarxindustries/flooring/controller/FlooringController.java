/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.controller;

import com.karlmarxindustries.flooring.service.AreaTooSmallException;
import com.karlmarxindustries.flooring.service.NoOrdersOnDateException;
import com.karlmarxindustries.flooring.dao.FilePersistenceException;
import com.karlmarxindustries.flooring.service.NoMatchingOrdersException;
import com.karlmarxindustries.flooring.dao.TestingModeException;
import com.karlmarxindustries.flooring.dto.Order;
import com.karlmarxindustries.flooring.dto.Product;

import com.karlmarxindustries.flooring.service.FlooringDuplicateIdException;
import com.karlmarxindustries.flooring.ui.FlooringView;
import java.util.List;
import com.karlmarxindustries.flooring.service.FlooringServiceLayer;

import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author karlmarx
 */
public class FlooringController {

    FlooringView view;
    private FlooringServiceLayer service;
    //idea : secret method to switch between test and production

    Locale aLocale = new Locale("en", "US");
    Locale deLocale = new Locale("de", "DE");

    ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", aLocale);
    ResourceBundle messagesDE = ResourceBundle.getBundle("MessagesBundle", deLocale);

    public FlooringController(FlooringServiceLayer service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    public void run() throws FilePersistenceException, NoOrdersOnDateException, FlooringDuplicateIdException, NoMatchingOrdersException {
        service.logEntry("The user started the program.");
        view.displayTitleBanner();
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            service.loadOrderData();
            service.initialLoadProductTaxInfo();
        } catch (FilePersistenceException e) {
            view.displayErrorMessage(e.getMessage());
            service.logEntry("A file persistence exception was thrown whilst logging order/product/tax data.");
            keepGoing = false;
        }

        while (keepGoing) {
            try {
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        saveCurrentWork();
                        break;
                    case 6:
                        languageMenu();
                        break;
                    case 7:
                        service.logEntry("The user exited the program.");
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            } catch (NoOrdersOnDateException e) {
                view.displayErrorMessage(e.getMessage());
                service.logEntry("NoOrdersOnDateException was thrown: " + messages.getString(e.getMessage()));
            }
        }
        exitMessage();
    }

    private void displayOrders() throws FilePersistenceException, NoOrdersOnDateException {
        view.displayDisplayOrdersBanner();
        LocalDate searchDate = view.getDesiredDate();
        List<Order> ordersForDate = service.getOrdersForDate(searchDate);
        view.displayOrdersForDate(ordersForDate);
        service.logEntry("The user displayed the orders for " + searchDate.toString());
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void addOrder() throws FilePersistenceException {
        view.displayAddOrderBanner();

        List<String> states = service.loadValidStates();
        List<Product> products = service.loadProducts();
        boolean hasErrors = false;
        do {
            Order currentOrder = view.getNewOrderInfo(states, products);
            try {
                Order toAdd = service.calculateAndOrderNumber(currentOrder);
                boolean isConfirmed = view.displayConfirmOrderToAdd(toAdd);
                if (isConfirmed) {
                    service.addOrder(toAdd);
                    service.logEntry("The user added " + toAdd.toString());
                }
                hasErrors = false;
            } catch (FlooringDuplicateIdException | AreaTooSmallException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
                service.logEntry("AreaTooSmallException was thrown: " + messages.getString(e.getMessage()));
            }
        } while (hasErrors);
    }

    private void removeOrder() throws FilePersistenceException {
        view.displayRemoveOrderBanner();
        int searchOrderNumber = view.getOrderNumber();
        LocalDate searchDate = view.getDesiredDate();

        //try catch for no such order
        try {
            Order toRemove = service.getMatchingOrderForDate(searchDate, searchOrderNumber);
            boolean isConfirmed = view.displayConfirmOrderToRemove(toRemove);
            if (isConfirmed) {
                service.removeOrder(toRemove);
                service.logEntry("The user removed " + toRemove.toString());
                view.displayRemoveSuccessBanner();
            } else {
                view.displayNoChangesMade();
            }
        } catch (NoMatchingOrdersException e) {
            view.displayErrorMessage(e.getMessage());
            service.logEntry("NoMatchingOrdersException was thrown: " + messages.getString(e.getMessage()));
        }
    }

    private void saveCurrentWork() throws FilePersistenceException {
        boolean isConfirmed = view.displayConfirmSave();
        if (isConfirmed) {
            try {
                service.saveWorks();
                service.logEntry("The user saved their progress.");
                view.displaySaveSuccess();
            } catch (TestingModeException e) {
                view.displayErrorMessage(e.getMessage());
            } catch (FilePersistenceException f) {
                view.displayErrorMessage(f.getMessage());
            }
        } else {
            view.displayNotSaved();
        }

    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    private void editOrder() throws FlooringDuplicateIdException, FilePersistenceException, NoMatchingOrdersException {
        view.displayEditOrderBanner();
        int searchOrderNumber = view.getOrderNumber();
        LocalDate searchDate = view.getDesiredDate();
        
        try {
            Order toEdit = service.getMatchingOrderForDate(searchDate, searchOrderNumber);
            List<String> states = service.loadValidStates();
            List<Product> products = service.loadProducts();
            Order edited = view.displayCurrentGetEdits(states, products, toEdit);
            Order editedAndCalculated = service.calculateCostsTaxesTotal(edited);
            boolean isConfirmed = view.displayConfirmEditing(editedAndCalculated);
            if (isConfirmed) {
                view.displayEditSuccess();
                service.editOrder(editedAndCalculated);
                service.logEntry("The user edited " + toEdit.toString() + "to" + edited.toString() + ".");
            } else {
                view.displayNoChangesMade();
            }
        } catch (NoMatchingOrdersException | AreaTooSmallException e) {
            view.displayErrorMessage(e.getMessage());
            service.logEntry("AreaTooSmallException was thrown: " + messages.getString(e.getMessage()));
        }
    }

    public void languageMenu() throws FilePersistenceException {

        boolean keepGoing = true;
        int menuSelection = 0;

        while (keepGoing) {

            menuSelection = getLanguageMenuSelection();

            switch (menuSelection) {

                case 1:
                    view.switchDeutsch();
                    service.logEntry("The user changed the UI language to German.");
                    keepGoing = false;
                    break;
                case 2:
                    view.switchHebrew();
                    service.logEntry("The user changed the UI language to Hebrew.");
                    keepGoing = false;
                    break;
                case 3:
                    view.switchEnglish();
                    service.logEntry("The user changed the UI language to English.");
                    keepGoing = false;
                    break;
                case 4:
                    view.switchDutch();
                    service.logEntry("The user changed the UI language to Dutch.");
                    keepGoing = false;
                    break;
                case 5:
                    view.switchChinese();
                    service.logEntry("The user changed the UI language to Traditional Chinese.");
                    keepGoing = false;
                    break;
                case 6:
                    view.switchKorean();
                    service.logEntry("The user changed the UI language to Korean.");
                    keepGoing = false;
                    break;
                case 7:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }

        }

    }

    private int getLanguageMenuSelection() {
        return view.displayLanguageGetChoice();
    }

}
