package com.rmntim;

import com.rmntim.pokemons.*;
import ru.ifmo.se.pokemon.Battle;

public class Main {
    public static void main(String[] args) {
        var b = new Battle();
        b.addFoe(new Joltik("Pudge", 20));
        b.addFoe(new Koffing("Terrorblade", 23));
        b.addFoe(new Sneasel("Snapfire", 19));
        b.addAlly(new Aggron("Anti-Mage", 25));
        b.addAlly(new Charizard("Invoker", 20));
        b.addAlly(new Electabuzz("Axe", 21));
        b.go();
    }
}