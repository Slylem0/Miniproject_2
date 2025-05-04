import models.gui.BattleSetupFrame;

import javax.swing.*;

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new BattleSetupFrame().setVisible(true);
    });
}

