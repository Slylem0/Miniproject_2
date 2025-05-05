package models.gui;

//BIEN


import models.batallas.Attack;
import models.batallas.BattleManager;
import models.entrenadores.Trainer;
import models.pokemones.Pokemon;

import javax.swing.*;
import java.awt.*;

public class BattleFrame extends JFrame {
    private final Trainer entrenador1;
    private final Trainer entrenador2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
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
        JPanel battlePanel = new JPanel(new GridBagLayout());
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
        JPanel controlPanel = new JPanel(new BorderLayout());

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

private void ejecutarAtaque(int attackIndex) {
    // Verificar que el índice sea válido para el Pokémon actual
    Pokemon attacker = isEntrenador1Turn ? pokemon1 : pokemon2;
    Pokemon defender = isEntrenador1Turn ? pokemon2 : pokemon1;

    if (attackIndex >= attacker.getAttacks().size()) {
        battleLog.append("¡Ataque no disponible!\n");
        actualizarInterfaz();
        return;
    }

    Attack attack = attacker.getAttacks().get(attackIndex);
    int damage = BattleManager.calculateDamage(attack, attacker, defender);
    defender.receiveDamage(damage);

    battleLog.append(String.format("%s usa %s y causa %d de daño!\n",
            attacker.getName(), attack.getName(), damage));

    // Actualizar la interfaz
    actualizarInterfaz();

    // Verificar si la batalla ha terminado
    if (!verificarEstadoBatalla()) {
        // Si la batalla continúa, cambiar el turno
        cambiarTurno();
    }
    // Revalidar y repintar el JTextArea para asegurar que se actualice visualmente
    battleLog.revalidate();
    battleLog.repaint();
}

private void cambiarTurno() {
    isEntrenador1Turn = !isEntrenador1Turn;
    actualizarControles();

    System.out.println("Turno cambiado a: " + (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()));

    // besure of all the buttons are there
    for (int i = 0; i < 4; i++) {
        Pokemon pokemonActual = isEntrenador1Turn ? pokemon1 : pokemon2;
        if (i < pokemonActual.getAttacks().size()) {
            attackButtons[i].setEnabled(true);
        } else {
            attackButtons[i].setEnabled(false);
        }
    }
    switchPokemonButton.setEnabled(true);
}

private void actualizarControles() {
    // Habilitar/deshabilitar controles según el turno
    boolean esJugadorActual = isEntrenador1Turn;

    // Habilitar botones de ataque solo para el jugador actual
    for (int i = 0; i < 4; i++) {
        Pokemon pokemonActual = isEntrenador1Turn ? pokemon1 : pokemon2;
        if (i < pokemonActual.getAttacks().size()) {
            Attack attack = pokemonActual.getAttacks().get(i);
            attackButtons[i].setText(attack.getName() + " " + attack.getPower() + " " + attack.getDamageType());
            attackButtons[i].setEnabled(esJugadorActual);
        } else {
            attackButtons[i].setEnabled(false);
        }
    }

    // Habilitar botón de cambio solo para el jugador actual
    switchPokemonButton.setEnabled(esJugadorActual);

    // Actualizar el texto que indica de quién es el turno
    battleLog.append("\nTurno de " + (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()) + "\n");
}

    private void cambiarPokemon() {
        Trainer currentTrainer = isEntrenador1Turn ? entrenador1 : entrenador2;
        Pokemon newPokemon = selectInitialPokemon(currentTrainer);
        
        if (newPokemon != null) {
            if (isEntrenador1Turn) {
                pokemon1 = newPokemon;
            } else {
                pokemon2 = newPokemon;
            }
            
            battleLog.append(String.format("%s cambia a %s!\n", 
                currentTrainer.getName(), newPokemon.getName()));
            
            actualizarInterfaz();
            cambiarTurno();
        }
    }

    private void actualizarInterfaz() {
        updatePokemonDisplay(pokemonPanel1, pokemon1, healthBar1);
        updatePokemonDisplay(pokemonPanel2, pokemon2, healthBar2);
        actualizarControles();
        // Revalidar y repintar los paneles para asegurarse de que la UI se actualice correctamente
        revalidate();
        repaint();
    }

    private boolean verificarEstadoBatalla() {
        if (!pokemon1.isAlive()) {
            if (entrenador1.getTeam().stream().anyMatch(Pokemon::isAlive)) {
                JOptionPane.showMessageDialog(this, entrenador1.getName() + ", tu Pokémon ha sido derrotado. Elige otro.");
                pokemon1 = selectInitialPokemon(entrenador1);
                actualizarInterfaz();
                return false; // La batalla continúa
            } else {
                anunciarGanador(entrenador2);
                return true; // Batalla terminada
            }
        }

        if (!pokemon2.isAlive()) {
            if (entrenador2.getTeam().stream().anyMatch(Pokemon::isAlive)) {
                JOptionPane.showMessageDialog(this, entrenador2.getName() + ", tu Pokémon ha sido derrotado. Elige otro.");
                pokemon2 = selectInitialPokemon(entrenador2);
                actualizarInterfaz();
                return false; // La batalla continúa
            } else {
                anunciarGanador(entrenador1);
                return true; // Batalla terminada
            }
        }

        return false; // Ambos siguen vivos
    }

    private void anunciarGanador(Trainer winner) {
        JOptionPane.showMessageDialog(this,
                "¡" + winner.getName() + " ha ganado la batalla!",
                "Fin de la Batalla",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }


    private void iniciarBatalla() {
        pokemon1 = selectInitialPokemon(entrenador1);
        pokemon2 = selectInitialPokemon(entrenador2);
        actualizarInterfaz();
        battleLog.append("¡La batalla ha comenzado!\n");
        actualizarControles();
    }

    private Pokemon selectInitialPokemon(Trainer trainer) {
        Pokemon[] options = trainer.getTeam().stream()
                .filter(Pokemon::isAlive)
                .toArray(Pokemon[]::new);

        if (options.length == 0) {
            return null; // No hay Pokémon vivos
        }

        return (Pokemon) JOptionPane.showInputDialog(
                this,
                trainer.getName() + ", elige tu Pokémon:",
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
        attackButtons[i].addActionListener(e -> {
            System.out.println("Botón de ataque " + (attackIndex+1) + " presionado por " + 
                (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()));
            ejecutarAtaque(attackIndex);
        });
    }

    // Configurar listener para el botón de cambio de Pokémon
    switchPokemonButton.addActionListener(e -> {
        System.out.println("Botón de cambio presionado por " + 
            (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()));
        cambiarPokemon();
    });
}

private void updatePokemonDisplay(JPanel panel, Pokemon pokemon, JProgressBar healthBar) {
    // Verificar si el Pokémon es nulo
    if (pokemon == null) {
        return;
    }
    
    // Actualizar el nombre y la salud
    ((JLabel)panel.getComponent(0)).setText(pokemon.getName() + " (" + pokemon.getHealthPoints() + " HP)");
    
    // Calcular el porcentaje de salud (evitar división por cero)
    int maxHealth = pokemon.getMaxHealthPoints(); // Asumiendo que existe este método
    if (maxHealth <= 0) maxHealth = 1;
    
    int healthPercentage = (pokemon.getHealthPoints() * 100) / maxHealth;
    healthBar.setValue(healthPercentage);
    healthBar.setString(pokemon.getHealthPoints() + "/" + maxHealth + " HP");
    healthBar.setStringPainted(true);
    
    // Cambiar el color de la barra según la salud
    if (healthPercentage > 50) {
        healthBar.setForeground(Color.GREEN);
    } else if (healthPercentage > 25) {
        healthBar.setForeground(Color.YELLOW);
    } else {
        healthBar.setForeground(Color.RED);
    }
}
}