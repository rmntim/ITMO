package ru.rmntim.common;

import java.io.BufferedReader;

/**
 * Manages getting user input from stdin or file.
 * {@code reader} must be set at the start of the program.
 */
public final class GlobalInput {
    private static BufferedReader reader;
    private static boolean inFile = false;

    private GlobalInput() {
    }

    public static BufferedReader getReader() {
        return reader;
    }

    public static void setReader(BufferedReader reader) {
        GlobalInput.reader = reader;
    }

    public static boolean isInFile() {
        return inFile;
    }

    public static void setInFile(boolean inFile) {
        GlobalInput.inFile = inFile;
    }
}
