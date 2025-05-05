package models.gui;

import models.batallas.Attack;
import models.batallas.BattleManager;
import models.entrenadores.Trainer;
import models.pokemones.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleFrame extends JFrame {
    private final Trainer entrenador1;
    private final Trainer entrenador2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private final JPanel battlePanel;
    private final JPanel controlPanel;
    private final JTextArea battleLog;
    private final JPanel pokemonPanel1;
    private final JPanel pokemonPanel2;
    private final JProgressBar healthBar1;
    private final JProgressBar healthBar2;
    private final JButton[] attackButtons;
    private final JButton switchPokemonButton;
    private boolean isEntrenador1Turn = true;

    public BattleFrame(Trainer entrenador1, Trainer entrenador2) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;
        
        // Configuración básica del frame
        setTitle("Batalla Pokémon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel principal de batalla
        battlePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel del Pokémon 2 (arriba a la derecha)
        pokemonPanel2 = new JPanel(new BorderLayout());
        healthBar2 = new JProgressBar(0, 100);
        pokemonPanel2.add(new JLabel("Pokemon 2"), BorderLayout.NORTH);
        pokemonPanel2.add(healthBar2, BorderLayout.SOUTH);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        battlePanel.add(pokemonPanel2, gbc);

        // Panel del Pokémon 1 (abajo a la izquierda)
        pokemonPanel1 = new JPanel(new BorderLayout());
        healthBar1 = new JProgressBar(0, 100);
        pokemonPanel1.add(new JLabel("Pokemon 1"), BorderLayout.NORTH);
        pokemonPanel1.add(healthBar1, BorderLayout.SOUTH);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        battlePanel.add(pokemonPanel1, gbc);

        // Panel de control (abajo)
        controlPanel = new JPanel(new BorderLayout());
        
        // Panel de botones de ataque
        JPanel attackButtonsPanel = new JPanel(new GridLayout(2, 2));
        attackButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            attackButtons[i] = new JButton("Ataque " + (i + 1));
            attackButtonsPanel.add(attackButtons[i]);
        }

        // Botón de cambio de Pokémon
        switchPokemonButton = new JButton("Cambiar Pokémon");
        
        // Log de batalla
        battleLog = new JTextArea(5, 40);
        battleLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(battleLog);

        // Agregar componentes al panel de control
        controlPanel.add(attackButtonsPanel, BorderLayout.CENTER);
        controlPanel.add(switchPokemonButton, BorderLayout.EAST);
        controlPanel.add(scrollPane, BorderLayout.SOUTH);

        // Agregar paneles principales al frame
        add(battlePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Inicializar la batalla
        iniciarBatalla();
        setupListeners();
    }

    private void iniciarBatalla() {
        pokemon1 = selectInitialPokemon(entrenador1);
        pokemon2 = selectInitialPokemon(entrenador2);
        actualizarInterfaz();
    }

    private Pokemon selectInitialPokemon(Trainer trainer) {
        Pokemon[] options = trainer.getTeam().toArray(new Pokemon[0]);
        return (Pokemon) JOptionPane.showInputDialog(
            this,
            trainer.getName() + ", elige tu Pokémon inicial:",
            "Selección de Pokémon",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    }

    private void setupListeners() {
        // Configurar listeners para los botones de ataque
        for (int i = 0; i < attackButtons.length; i++) {
            final int attackIndex = i;
            attackButtons[i].addActionListener(e -> ejecutarAtaque(attackIndex));
        }

        // Configurar listener para el botón de cambio de Pokémon
        switchPokemonButton.addActionListener(e -> cambiarPokemon());
    }

    private void ejecutarAtaque(int attackIndex) {
        Pokemon attacker = isEntrenador1Turn ? pokemon1 : pokemon2;
        Pokemon defender = isEntrenador1Turn ? pokemon2 : pokemon1;

        Attack attack = attacker.getAttacks().get(attackIndex);
        int damage = BattleManager.calculateDamage(attack, attacker, defender);
        defender.receiveDamage(damage);

        // Actualizar log y estado
        battleLog.append(String.format("%s usa %s y causa %d de daño!\n",
            attacker.getName(), attack.getName(), damage));

        actualizarInterfaz();
        verificarEstadoBatalla();
        isEntrenador1Turn = !isEntrenador1Turn;
    }

    private void cambiarPokemon() {
        Trainer currentTrainer = isEntrenador1Turn ? entrenador1 : entrenador2;
        Pokemon newPokemon = selectInitialPokemon(currentTrainer);

        if (isEntrenador1Turn) {
            pokemon1 = newPokemon;
        } else {
            pokemon2 = newPokemon;
        }

        battleLog.append(String.format("%s cambia a %s!\n",
            currentTrainer.getName(), newPokemon.getName()));

        actualizarInterfaz();
        isEntrenador1Turn = !isEntrenador1Turn;
    }

    private void actualizarInterfaz() {
        // Actualizar información de los Pokémon
        updatePokemonDisplay(pokemonPanel1, pokemon1, healthBar1);
        updatePokemonDisplay(pokemonPanel2, pokemon2, healthBar2);

        // Actualizar botones de ataque
        Pokemon currentPokemon = isEntrenador1Turn ? pokemon1 : pokemon2;
        for (int i = 0; i < 4; i++) {
            if (i < currentPokemon.getAttacks().size()) {
                Attack attack = currentPokemon.getAttacks().get(i);
                attackButtons[i].setText(attack.getName());
                attackButtons[i].setEnabled(true);
            } else {
                attackButtons[i].setEnabled(false);
            }
        }
    }

    private void updatePokemonDisplay(JPanel panel, Pokemon pokemon, JProgressBar healthBar) {
        ((JLabel)panel.getComponent(0)).setText(pokemon.getName() +
            " (" + pokemon.getHealthPoints() + " HP)");
        int healthPercentage = (pokemon.getHealthPoints() * 100) / pokemon.getHealthPoints();
        healthBar.setValue(healthPercentage);
        healthBar.setString(pokemon.getHealthPoints() + " HP");
        healthBar.setStringPainted(true);
    }

    private void verificarEstadoBatalla() {
        if (!pokemon1.isAlive() || !pokemon2.isAlive()) {
            Trainer winner = !pokemon2.isAlive() ? entrenador1 : entrenador2;
            JOptionPane.showMessageDialog(this,
                "¡" + winner.getName() + " ha ganado la batalla!",
                "Fin de la Batalla",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}