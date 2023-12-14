package com.rmntim.models.common;

import com.rmntim.interfaces.Plurable;

public abstract class Wearable implements Plurable {
    private String name;

    public String getName() {
        return name;
    }
}
