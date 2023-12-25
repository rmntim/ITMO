package com.rmntim.pokemons;

import com.rmntim.attacks.physical.ShadowPunch;
import com.rmntim.attacks.special.ThunderShock;
import com.rmntim.attacks.status.LightScreen;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Electabuzz extends Pokemon {
    public Electabuzz(String name, int level) {
        super(name, level);
        setStats(65, 83, 57, 95, 85, 105);
        setType(Type.ELECTRIC);
        setMove(new LightScreen(), new ShadowPunch(), new ThunderShock());
    }
}
