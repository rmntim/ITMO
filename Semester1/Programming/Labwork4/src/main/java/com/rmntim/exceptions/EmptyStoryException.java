package com.rmntim.exceptions;

public class EmptyStoryException extends RuntimeException {
    public EmptyStoryException() {
        super("Story is empty");
    }
}
