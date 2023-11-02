package com.rmntim.attacks.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class MegaPunch extends PhysicalMove {
    public MegaPunch() {
        super(Type.NORMAL, 80, 85);
    }

    @Override
    protected String describe() {
        return "использует способность Mega Punch";
    }
}
