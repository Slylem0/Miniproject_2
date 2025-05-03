package models;

import java.util.List;
import java.util.Scanner;

public class Battle {
    public static void startBattle(Trainer trainer1, Trainer trainer2, Scanner scanner) {
        Pokemon pokemon1 = choosePokemon(trainer1, scanner);
        Pokemon pokemon2 = choosePokemon(trainer2, scanner);
        
        while (true) {
            // Determine turn order based on speed
            Pokemon firstPokemon = BattleManager.determineFirstAttacker(pokemon1, pokemon2);
            Pokemon secondPokemon = (firstPokemon == pokemon1) ? pokemon2 : pokemon1;
            Trainer firstTrainer = (firstPokemon == pokemon1) ? trainer1 : trainer2;
            Trainer secondTrainer = (firstPokemon == pokemon1) ? trainer2 : trainer1;
            
            System.out.println("\n" + firstPokemon.getName() + " moves first due to higher speed!");
            
            // First Pokémon's turn
            executeAttack(firstPokemon, secondPokemon, firstTrainer, scanner);
            if (!secondPokemon.isAlive()) {
                System.out.println(secondPokemon.getName() + " fainted!");
                announceWinner(firstTrainer);
                break;
            }
            
            // Second Pokémon's turn
            executeAttack(secondPokemon, firstPokemon, secondTrainer, scanner);
            if (!firstPokemon.isAlive()) {
                System.out.println(firstPokemon.getName() + " fainted!");
                announceWinner(secondTrainer);
                break;
            }
        }
    }

    private static void executeAttack(Pokemon attacker, Pokemon defender, Trainer attackerTrainer, Scanner scanner) {
        System.out.println("\n" + attackerTrainer.getName() + "'s " + attacker.getName() + "'s turn!");
        
        // Display attacks with their type
        List<Attack> attacks = attacker.getAttacks();
        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            System.out.printf("%d. %s (%s - %s, Power: %d)%n", 
                i + 1, 
                attack.getName(), 
                attack.getType(),
                attack.getDamageType(),
                attack.getPower()
            );
        }
        
        // Select attack
        System.out.print("Choose attack (1-" + attacks.size() + "): ");
        int choice = scanner.nextInt() - 1;
        Attack selectedAttack = attacks.get(choice);
        
        // Calculate and apply damage
        int damage = BattleManager.calculateDamage(selectedAttack, attacker, defender);
        defender.receiveDamage(damage);
        
        // Display attack results
        System.out.printf("%s used %s! ", attacker.getName(), selectedAttack.getName());
        if (selectedAttack.getType().equals(attacker.getType())) {
            System.out.print("STAB bonus applied! ");
        }
        if (hasAdvantage(selectedAttack.getType(), defender.getType())) {
            System.out.print("It's super effective! ");
        }
        System.out.printf("Dealt %d damage!%n", damage);
        System.out.printf("%s has %d HP remaining!%n", defender.getName(), defender.getHealthPoints());
    }

    private static void announceWinner(Trainer winner) {
        System.out.println("\n" + winner.getName() + " wins the battle!");
    }

    private static Pokemon choosePokemon(Trainer trainer, Scanner scanner) {
        System.out.println("\n" + trainer.getName() + ", choose your Pokémon:");
        List<Pokemon> team = trainer.getTeam();
        for (int i = 0; i < team.size(); i++) {
            System.out.println((i + 1) + ". " + team.get(i).getName());
        }
        int choice = scanner.nextInt();
        return team.get(choice - 1);
    }
    static boolean hasAdvantage(String attackType, String defenderType) {
        return switch (attackType) {
            case "Fire" -> defenderType.equals("Grass") || defenderType.equals("Steel");
            case "Water" -> defenderType.equals("Fire");
            case "Grass" -> defenderType.equals("Water");
            case "Electric" -> defenderType.equals("Water");
            case "Psychic" -> defenderType.equals("Fighting");
            case "Fighting" -> defenderType.equals("Dark") || defenderType.equals("Steel");
            case "Dark" -> defenderType.equals("Psychic");
            case "Steel" -> defenderType.equals("Dragon");
            case "Dragon" -> defenderType.equals("Dragon");
            default -> false;
        };
    }
    // Other existing methods...
}