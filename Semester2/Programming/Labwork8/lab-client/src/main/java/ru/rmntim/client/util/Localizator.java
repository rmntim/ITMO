package ru.rmntim.client.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class Localizer {
    private ResourceBundle bundle;

    public Localizer(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getKeyString(String key) {
        return bundle.getString(key);
    }

    public String getDate(LocalDate date) {
        if (date == null) return "null";
        var formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(bundle.getLocale());
        return date.format(formatter);
    }

    public String getDate(LocalDateTime date) {
        if (date == null) return "null";
        var formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(bundle.getLocale());
        return date.format(formatter);
    }
}
