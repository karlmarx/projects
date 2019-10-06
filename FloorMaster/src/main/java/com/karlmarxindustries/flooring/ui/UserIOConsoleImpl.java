/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karlmarxindustries.flooring.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author karlmarx
 */
public class UserIOConsoleImpl implements UserIO {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public double readDouble(String prompt) {
        double result = 0;
        print(prompt);
        result = scanner.nextDouble();
        scanner.nextLine();
        return result;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double result = 0;
        boolean badInput = true;
        boolean continueInput = true;
        while (badInput) {
            do {
                try {
                    result = readDouble(prompt);
                    continueInput = false;
                    if (Double.compare(result, min) >= 0 && Double.compare(result, max) <= 0) {
                        badInput = false;
                    } else {
                        print("Input need to be >= " + min + " or <= " + max + ".");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Try Again.  A valid number is required.");
                    scanner.nextLine();
                }
            } while (continueInput);

            

        }
        return result;

    }

    @Override
    public int readInt(String prompt) throws InputMismatchException {
        int result = 0;
        print(prompt);
        boolean continueInput = true;
        do {
            try {
                result = scanner.nextInt();
                //changed to fix issue with alpha input in menu
                scanner.nextLine();
                continueInput = false;
            } catch (InputMismatchException e) {
                System.out.println("Try Again.  An integer is required.");
                scanner.nextLine();
            }
        } while (continueInput);
        return result;
    }

    @Override
    public double readDoubleAllowBlank(String prompt, double min, double max) {
        double result = 0;
        String resultString;
        boolean badInput = true;

        while (badInput) {
            resultString = readString(prompt);
            result = Double.parseDouble(resultString);
            if (resultString.equals("") || Double.compare(result, min) >= 0 && Double.compare(result, max) <= 0) {
                badInput = false;
            } else {
                print("Input need to be >= " + min + " or <= " + max + ".");
            }
            if (resultString.equals("")) {
                result = 0d;
            }
        }
        return result;
//MAkethis piggyback
    }
    //  @Override

    public float readFloat(String prompt) {
        float result = 0;
        print(prompt);
        result = scanner.nextFloat();
        scanner.nextLine();
        return result;
    }

    //   @Override
    public float readFloat(String prompt, float min, float max) {
        float result = 0;
        boolean badInput = true;

        while (badInput) {
            result = readFloat(prompt);
            if (Float.compare(result, min) >= 0 && Float.compare(result, max) <= 0) {
                badInput = false;
            } else {
                print("Input need to be >= " + min + " or <= " + max + ".");
            }
        }
        return result;
    }

    

    // @Override
    public int readInt(String prompt, int min, int max) {
        int result = 0;
        boolean badInput = true;

        while (badInput) {
            result = readInt(prompt);
            if (result >= min && result <= max) {
                badInput = false;
            } else {
                print("Input need to be >= " + min + " or <= " + max + ".");
            }

        }
        return result;
    }

    //@Override
    public long readLong(String prompt) {
        long result = 0;
        print(prompt);
        result = scanner.nextLong();
        scanner.nextLine();
        return result;
    }

    //@Override
    public long readLong(String prompt, long min, long max) {
        long result = 0;
        boolean badInput = true;

        while (badInput) {
            result = readLong(prompt);
            if (result >= min && result <= max) {
                badInput = false;
            } else {
                print("Input need to be >= " + min + " or <= " + max + ".");
            }
        }
        return result;
    }

    // @Override
    public String readString(String prompt) {
        String result = "";

        print(prompt);
        result = scanner.nextLine();

        return result;
    }

}
