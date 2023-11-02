package com.rmntim.pokemons;

import com.rmntim.attacks.status.Amnesia;
import com.rmntim.attacks.status.ThunderWave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Koffing extends Pokemon {
    public Koffing(String name, int level) {
        super(name, level);
        setStats(40, 65, 95, 60, 45, 35);
        setType(Type.POISON);
        setMove(new Amnesia(), new ThunderWave());
    }
}
