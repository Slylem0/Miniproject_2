package models.gui;

import models.entrenadores.Trainer;
import javax.swing.*;
import java.awt.*;

public class BattleFrame extends JFrame {
    private Trainer entrenador1;
    private Trainer entrenador2;

    public BattleFrame(Trainer entrenador1, Trainer entrenador2) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;

        setTitle("Batalla Pokémon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel principal de batalla
        JPanel battlePanel = new JPanel(new GridLayout(2, 1));

        JLabel vs = new JLabel(entrenador1.getName() + " VS " + entrenador2.getName(),
                SwingConstants.CENTER);
        vs.setFont(new Font("Arial", Font.BOLD, 24));

        battlePanel.add(vs);

        add(battlePanel, BorderLayout.CENTER);
    }
}