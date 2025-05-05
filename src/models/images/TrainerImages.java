package models.images;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TrainerImages {
    public static ImageIcon getTrainer1Image() {
        return new ImageIcon(Objects.requireNonNull(TrainerImages.class.getResource("/assets/trainer1.png")));
    }

    public static ImageIcon getTrainer2Image() {
        return new ImageIcon(Objects.requireNonNull(TrainerImages.class.getResource("/assets/trainer2.png")));
    }

    public static ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image image = icon.getImage(); // Obtener imagen original
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}