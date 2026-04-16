package agh.ics.oop;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException {
        System.out.println("Start");

       try {
           Application.launch(SimulationApp.class, args);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("End");
    }

}

