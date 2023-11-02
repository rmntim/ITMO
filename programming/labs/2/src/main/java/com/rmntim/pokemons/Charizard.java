package com.rmntim.pokemons;

import com.rmntim.attacks.physical.MegaPunch;
import com.rmntim.attacks.status.Amnesia;
import com.rmntim.attacks.status.ThunderWave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Charizard extends Pokemon {
    public Charizard(String name, int level) {
        super(name, level);
        setStats(78, 84, 78, 109, 85, 100);
        setType(Type.FIRE, Type.FLYING);
        setMove(new Amnesia(), new ThunderWave(), new MegaPunch());
    }
}
