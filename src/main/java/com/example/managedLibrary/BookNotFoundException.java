package com.example.managedLibrary;

class BookNotFoundException extends RuntimeException {

    BookNotFoundException(Long id) {
        super("Could not find Book " + id);
    }
}