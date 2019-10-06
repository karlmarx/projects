/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.dvdlibrary.dao;

import com.karlmarxindustries.dvdlibrary.dto.DVD;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author karlmarx
 */
public class DvdLibraryDaoFileImpl implements DvdLibraryDao {
    
    public static final String LIBRARY_FILE = "library.txt";
    public static final String DELIMITER = "::";
    
    private DVD unmarshallDvd(String dvdAsText){
        String[] dvdTokens = dvdAsText.split(DELIMITER);
        String title = dvdTokens[0];
        DVD dvdFromFile = new DVD(title);
        dvdFromFile.setReleaseDate(Integer.valueOf(dvdTokens[1]));
        dvdFromFile.setRating(dvdTokens[2]);
        dvdFromFile.setDirector(dvdTokens[3]);
        dvdFromFile.setStudio(dvdTokens[4]);
        dvdFromFile.setUserRatingOrNote(dvdTokens[5]);
        return dvdFromFile;
    }
    
    private String marshallDvd(DVD aDvd){
        String dvdAsText = aDvd.getTitle() + DELIMITER;
        dvdAsText += String.valueOf(aDvd.getReleaseDate()) + DELIMITER;
        dvdAsText += aDvd.getRating() + DELIMITER; 
        dvdAsText += aDvd.getDirector() + DELIMITER;
        dvdAsText += aDvd.getStudio() + DELIMITER;
        dvdAsText += aDvd.getUserRatingOrNote();
        return dvdAsText;
    }
    
    public void loadLibrary() throws DvdLibraryDaoException {
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(LIBRARY_FILE)));
        } catch (FileNotFoundException e) {
            throw new DvdLibraryDaoException("Uh-oh! Could not load library data into memory", e);
        }
        String currentLine;
        DVD currentDvd;
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentDvd = unmarshallDvd(currentLine);
            dvds.put(currentDvd.getTitle(), currentDvd);
        }
        scanner.close();
    }
    
    public void writeLibrary() throws DvdLibraryDaoException {
        PrintWriter out;
        try{
            out = new PrintWriter(new FileWriter(LIBRARY_FILE));
        } catch (IOException e){
            throw new DvdLibraryDaoException("Could not save DVD data", e);
        }
        String dvdAsText;
        List<DVD> dvdList = this.getAllDvds();
        for (DVD currentDvd : dvdList){
            dvdAsText = marshallDvd(currentDvd);
            out.println(dvdAsText);
            out.flush();
        }
               out.close();
    }
    
    @Override
    public void print(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double readDouble(String prompt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float readFloat(String prompt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int readInt(String prompt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long readLong(String prompt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String readString(String prompt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DVD> getAllDvds() throws DvdLibraryDaoException {
        return  new ArrayList<DVD>(dvds.values());
    }

    @Override
    public DVD getDvd(String title) throws DvdLibraryDaoException {
        return dvds.get(title);
    }

    @Override
    public DVD removeDvd(String title) throws DvdLibraryDaoException {
        DVD removedDvd = dvds.remove(title);
        return removedDvd;
    }   
    

    @Override
    public DVD addDVD (String title, DVD dvd) throws DvdLibraryDaoException {
        DVD newDvd =dvds.put(title, dvd);
        return newDvd;
    }
    
    
    @Override
    public DVD editDVD (String title, DVD dvd) throws DvdLibraryDaoException{
        //this needs to be different - find and update in map
        DVD editedDvd = dvds.put(title, dvd); //make sure first part is good with updates using an if 
        return editedDvd;
    }
    
    private Map<String, DVD> dvds = new HashMap<>();

    private Writer newFileWriter(String LIBRARY_FILE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
