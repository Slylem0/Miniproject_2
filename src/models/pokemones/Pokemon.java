package models.pokemones;

import models.batallas.Attack;
import models.names.Name;

import java.util.ArrayList;
import java.util.List;

public class Pokemon extends Creature {
    private Name name;
    private Integer defense;
    private Integer defenseEspecial;
    private Integer speed;
    private Integer maxhealthPoints;
    private List<Attack> attacks;

    //builder
    public Pokemon(Name name, int healthPoints, Integer defense,
                   int defenseEspecial, int speed){
        super(name.getType(), healthPoints);
        this.name = name;
        this.attacks = new ArrayList<>();
        this.defense = defense;
        this.defenseEspecial = defenseEspecial;
        this.speed = speed;
        this.maxhealthPoints = healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxhealthPoints;
    }

    public Name getName() {
        return name;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public boolean addAttack(Attack attack) {
        if (attacks.size() < 4) {
            attacks.add(attack);
            return true;
        }
        return false;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getDefenseEspecial() {
        return defenseEspecial;
    }

    public void setDefenseEspecial(Integer defenseEspecial) {
        this.defenseEspecial = defenseEspecial;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    //print the attacks
    public void displayAttacks() {
        if (attacks.isEmpty()) {
            System.out.println(name + " has no attacks.");
        } else {
            System.out.println("Attacks of " + name + ":");
            for (Attack attack : attacks) {
                System.out.println("- " + attack);
            }
        }
    }
    @Override
    public String toString() {
        return name.toString();
    }
}
