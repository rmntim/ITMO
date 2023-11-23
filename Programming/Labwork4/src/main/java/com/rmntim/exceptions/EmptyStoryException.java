package com.rmntim.exceptions;

public class EmptyStoryException extends Exception {
    public EmptyStoryException() {
        super("Story is empty");
    }
}
