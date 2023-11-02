package com.rmntim.attacks.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class TripleKick extends PhysicalMove {
    private int numberOfHits = 0;

    public TripleKick() {
        super(Type.FIGHTING, 10, 90, 0, 3);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        super.applyOppDamage(def, damage * ++numberOfHits);
    }

    @Override
    protected String describe() {
        return "использует способность Triple Kick";
    }
}
