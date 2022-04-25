package com.example.managedLibrary;

public class LoanNotFoundException extends RuntimeException {

    LoanNotFoundException( String message) {
        super(message);
    }
}