/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.dvdlibrary;

import com.karlmarxindustries.dvdlibrary.controller.DvdLibraryController;
import com.karlmarxindustries.dvdlibrary.dao.DvdLibraryDao;
import com.karlmarxindustries.dvdlibrary.dao.DvdLibraryDaoException;
import com.karlmarxindustries.dvdlibrary.dao.DvdLibraryDaoFileImpl;
import com.karlmarxindustries.ui.DvdLibraryView;
import com.karlmarxindustries.ui.UserIO;
import com.karlmarxindustries.ui.UserIOConsoleImpl;

/**
 *
 * @author karlmarx
 */
public class App {
    
        public static void main(String[] args) throws DvdLibraryDaoException {
            UserIO myIO = new UserIOConsoleImpl();
            DvdLibraryView myView = new DvdLibraryView(myIO);
            DvdLibraryDao myDao = new DvdLibraryDaoFileImpl();
            DvdLibraryController controller = new DvdLibraryController(myDao, myView);
            controller.run();
    }   
}
