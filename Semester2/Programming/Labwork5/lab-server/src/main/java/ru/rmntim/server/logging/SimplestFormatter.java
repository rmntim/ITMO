package ru.rmntim.server.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimplestFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return "[" + record.getLevel().toString() + "]: " + record.getMessage() + "\n";
    }
}
