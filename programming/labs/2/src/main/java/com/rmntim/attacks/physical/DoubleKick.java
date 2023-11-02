package com.rmntim.attacks.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class DoubleKick extends PhysicalMove {
    public DoubleKick() {
        super(Type.FIGHTING, 30, 100, 0, 2);
    }

    @Override
    protected String describe() {
        return "использует способность Double Kick";
    }
}
