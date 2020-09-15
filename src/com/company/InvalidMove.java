package com.company;

public class InvalidMove extends Exception {
    public InvalidMove(String errorMessage) {
        super(errorMessage);
    }
}
