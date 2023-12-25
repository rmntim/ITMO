package com.rmntim.pokemons;

import com.rmntim.attacks.physical.DoubleKick;
import com.rmntim.attacks.physical.MegaPunch;
import com.rmntim.attacks.status.Amnesia;
import com.rmntim.attacks.status.ThunderWave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Sneasel extends Pokemon {
    public Sneasel(String name, int level) {
        super(name, level);
        setStats(55, 95, 55, 35, 75, 115);
        setType(Type.DARK, Type.ICE);
        setMove(new Amnesia(), new ThunderWave(), new MegaPunch(), new DoubleKick());
    }
}
