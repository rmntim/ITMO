package com.rmntim.exceptions;

public class EmptyStoryException extends Throwable {
    public EmptyStoryException() {
        super("Story is empty");
    }
}
