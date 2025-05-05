package models.batallas;

import models.pokemones.Pokemon;
import models.entrenadores.Trainer;

import java.util.List;
import java.util.Scanner;

public class Battle {
    public static void startBattle(Trainer trainer1, Trainer trainer2, Scanner scanner) {
        Pokemon pokemon1 = choosePokemon(trainer1, scanner);
        Pokemon pokemon2 = choosePokemon(trainer2, scanner);
        
        while (true) {
            // Determinar el orden de los turnos basado en la velocidad
            Pokemon firstPokemon = BattleManager.determineFirstAttacker(pokemon1, pokemon2);
            Pokemon secondPokemon = (firstPokemon == pokemon1) ? pokemon2 : pokemon1;
            Trainer firstTrainer = (firstPokemon == pokemon1) ? trainer1 : trainer2;
            Trainer secondTrainer = (firstPokemon == pokemon1) ? trainer2 : trainer1;
            
            System.out.println("\n" + firstPokemon.getName() + " moves first due to higher speed!");
        
        // Turno del primer Pokémon
        Pokemon resultDefender = executeTurn(firstPokemon, secondPokemon, firstTrainer, secondTrainer, scanner);
        // Actualizar las referencias correctas
        if (secondPokemon == pokemon1) {
            pokemon1 = resultDefender;
        } else {
            pokemon2 = resultDefender;
        }
        
        if (!secondTrainer.hasAlivePokemon()) {
            announceWinner(firstTrainer);
            break;
        }

        // Turno del segundo Pokémon
        Pokemon resultAttacker = executeTurn(resultDefender, firstPokemon, secondTrainer, firstTrainer, scanner);
        // Actualizar las referencias correctas
        if (firstPokemon == pokemon1) {
            pokemon1 = resultAttacker;
        } else {
            pokemon2 = resultAttacker;
        }
        
        if (!firstTrainer.hasAlivePokemon()) {
            announceWinner(secondTrainer);
            break;
        }
    }
}


    private static Pokemon executeTurn(Pokemon attacker, Pokemon defender, Trainer attackerTrainer,
                                       Trainer defenderTrainer, Scanner scanner) {
        System.out.println("\n" + attackerTrainer.getName() + "'s turn!");
        System.out.println("1. Attack");
        System.out.println("2. Switch Pokemon");
        System.out.print("Choose your action: ");

        int choice = scanner.nextInt();
        if (choice == 1) {
            executeAttack(attacker, defender, attackerTrainer, scanner);
            if (!defender.isAlive()) {
                System.out.println(defender.getName() + " fainted!");
                return choosePokemon(defenderTrainer, scanner);
            }
            return defender;
        } else {
            return choosePokemon(attackerTrainer, scanner);
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
        while (true) {
            System.out.println("\n" + trainer.getName() + ", choose your Pokémon:");
            List<Pokemon> team = trainer.getTeam();
            for (int i = 0; i < team.size(); i++) {
                Pokemon pokemon = team.get(i);
                System.out.printf("%d. %s (HP: %d/%d)%n",
                        i + 1,
                        pokemon.getName(),
                        pokemon.getHealthPoints(),
                        pokemon.getHealthPoints()
                );
            }

            int choice = scanner.nextInt() - 1;
            if (choice >= 0 && choice < team.size()) {
                Pokemon chosen = team.get(choice);
                if (chosen.isAlive()) {
                    return chosen;
                } else {
                    System.out.println("That Pokémon has fainted! Choose another one.");
                }
            } else {
                System.out.println("Invalid choice! Try again.");
            }
        }

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


}