package com.rmntim.attacks.status;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class Amnesia extends StatusMove {
    public Amnesia() {
        super(Type.PSYCHIC, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.SPECIAL_DEFENSE, 2);
    }

    @Override
    protected String describe() {
        return "использует способность Amnesia";
    }
}
