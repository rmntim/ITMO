package com.rmntim.pokemons;

import com.rmntim.attacks.physical.ShadowPunch;
import com.rmntim.attacks.physical.TripleKick;
import com.rmntim.attacks.special.IceBeam;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Joltik extends Pokemon {
    public Joltik(String name, int level) {
        super(name, level);
        setStats(50, 47, 50, 57, 50, 65);
        setType(Type.BUG, Type.ELECTRIC);
        setMove(new IceBeam(), new TripleKick(), new ShadowPunch());
    }
}
