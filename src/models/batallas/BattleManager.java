package models.batallas;

import models.pokemones.Pokemon;

import static models.batallas.Battle.hasAdvantage;

public class BattleManager {
    private static final double TYPE_ADVANTAGE_BONUS = 1.3;

    public static Pokemon determineFirstAttacker(Pokemon pokemon1, Pokemon pokemon2) {
        return pokemon1.getSpeed() >= pokemon2.getSpeed() ? pokemon1 : pokemon2;
    }

    public static int calculateDamage(Attack attack, Pokemon attacker, Pokemon defender) {
        // Base damage calculation
        double baseDamage = attack.getPower();

        // Type advantage
        if (hasAdvantage(attack.getType(), defender.getType())) {
            baseDamage *= TYPE_ADVANTAGE_BONUS;
        }

        // Apply defense reduction based on attack type
        if (attack.getDamageType() == Attack.DamageType.PHYSICAL) {
            baseDamage = baseDamage * 100 / (100 + defender.getDefense());
        } else { // SPECIAL
            baseDamage = baseDamage * 100 / (100 + defender.getDefenseEspecial());
        }

        return Math.max(1, (int) baseDamage);
    }
}
