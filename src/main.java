import models.Trainer;
import models.TeamBuilder;
import models.Battle;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Trainer 1
        System.out.print("Enter name for Trainer 1: ");
        String name1 = scanner.nextLine();
        Trainer trainer1 = new Trainer(name1);

        System.out.println("Now u have a random team, enjoy it :)");
        TeamBuilder.generateRandomTeam(trainer1);

        System.out.println("_______________________________");

        // Trainer 2
        System.out.print("\nEnter name for Trainer 2: ");
        String name2 = scanner.nextLine();
        Trainer trainer2 = new Trainer(name2);

        System.out.println("Now u have a random team, so enjoy it :0 ");
        TeamBuilder.generateRandomTeam(trainer2);

        System.out.println("_______________________________");


        // Display teams
        System.out.println("\n--- Trainer Teams ---");
        System.out.println(trainer1);
        System.out.println(trainer2);

        // Start battle
        Battle.startBattle(trainer1, trainer2, scanner);
    }
}

