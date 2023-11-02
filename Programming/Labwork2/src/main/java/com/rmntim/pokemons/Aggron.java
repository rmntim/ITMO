package com.rmntim.pokemons;

import com.rmntim.attacks.physical.ShadowPunch;
import com.rmntim.attacks.physical.WingAttack;
import com.rmntim.attacks.special.ThunderShock;
import com.rmntim.attacks.status.LightScreen;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Aggron extends Pokemon {
    public Aggron(String name, int level) {
        super(name, level);
        setStats(70, 110, 180, 60, 60, 50);
        setType(Type.STEEL, Type.ROCK);
        setMove(new LightScreen(), new ShadowPunch(), new ThunderShock(), new WingAttack());
    }
}
