package com.plantgrowth;

import java.awt.*;
import javax.swing.*;

/**
 * Virtual Plant Growth Simulator - interactive Swing application that visualises
 * a plant thriving through four growth stages and declining through three
 * overwatering stages.
 */
public class VirtualPlantGrowthSimulator extends JFrame {
    
    // Constants
    private static final int GROWTH_STAGE_COUNT = 4;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 650;
    private static final int TOTAL_STAGE_COUNT = GrowthStage.values().length;
    private static final int[] GROWTH_WATER_THRESHOLDS = {2, 2, 2, Integer.MAX_VALUE};
    private static final int SAFE_WATER_AT_FLOWERING = 3;
    private static final int DECLINE_WATER_THRESHOLD = 1;

    private int currentStage = 0;
    private int waterAtCurrentStage = 0;

    private PlantPanel plantPanel;
    private JPanel mainPanel;
    private JLabel stageLabel;
    private JLabel descriptionLabel;
    private JButton waterButton;
    private JButton resetButton;
    
    private enum GrowthStage {
        SEED("Seed", new Color(210, 180, 140), "Foundation: Plant strong roots before reaching for the sky"),
        SPROUT("Sprout", new Color(173, 216, 130), "Emergence: Break through limitations with consistent effort"),
        YOUNG_PLANT("Young Plant", new Color(144, 198, 126), "Growth: Build strength through challenges and adaptation"),
        FLOWERING_PLANT("Flowering Plant", new Color(255, 205, 210), "Blossom: Share your beauty and wisdom with the world"),
        WILTING("Wilting", new Color(255, 228, 181), "Warning: Excess drains your vital energy"),
        DYING("Dying", new Color(205, 145, 102), "Decline: Overindulgence leads to irreversible damage"),
        DEAD("Dead", new Color(169, 169, 169), "Lesson: Balance is the key to sustainable growth");

        private final String displayName;
        private final Color backgroundColor;
        private final String description;

        GrowthStage(String displayName, Color backgroundColor, String description) {
            this.displayName = displayName;
            this.backgroundColor = backgroundColor;
            this.description = description;
        }
    }
    
    public VirtualPlantGrowthSimulator() {
        super("Virtual Plant Growth Simulator");

        // Set up proper look and feel before creating components
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Enable anti-aliasing for better text rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            // Use default look and feel if system L&F fails
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Last resort - use default
            }
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        // Ensure proper rendering hints
        setBackground(Color.WHITE);

        initialiseComponents();
        updatePlantDisplay();

        // Force repaint to ensure everything is visible
        revalidate();
        repaint();

        setVisible(true);
    }
    
    private void initialiseComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setOpaque(true);

        JLabel titleLabel = new JLabel("Virtual Plant Growth Simulator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        plantPanel = new PlantPanel();
        plantPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        stageLabel = new JLabel();
        stageLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        stageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        descriptionLabel = new JLabel();
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(600, 80));

        waterButton = new JButton("Water");
        waterButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        waterButton.setPreferredSize(new Dimension(120, 55));
        waterButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        waterButton.setForeground(new Color(0, 0, 139)); // Dark Blue text
        waterButton.setFocusPainted(false);
        waterButton.setOpaque(true);
        waterButton.setBorderPainted(true);
        waterButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 139), 3));
        // Ensure button text is visible
        waterButton.setContentAreaFilled(true);
        waterButton.addActionListener(e -> handleWatering());

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resetButton.setPreferredSize(new Dimension(120, 55));
        resetButton.setBackground(new Color(0, 0, 205)); // Medium Blue
        resetButton.setForeground(new Color(0, 0, 139)); // Dark Blue text
        resetButton.setFocusPainted(false);
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(true);
        resetButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 139), 3));
        // Ensure button text is visible
        resetButton.setContentAreaFilled(true);
        resetButton.addActionListener(e -> resetPlant());

        buttonPanel.add(waterButton);
        buttonPanel.add(resetButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(plantPanel);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(stageLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(descriptionLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        add(mainPanel);
    }

    private void handleWatering() {
        waterAtCurrentStage++;

        try {
            if (currentStage < GROWTH_STAGE_COUNT - 1) {
                handleGrowthWatering();
            } else {
                handleDeclineWatering();
            }
        } catch (OverWateringException exception) {
            JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Overwatering Alert",
                JOptionPane.WARNING_MESSAGE
            );
        }

        updatePlantDisplay();
    }
    
    private void handleGrowthWatering() {
        if (waterAtCurrentStage >= GROWTH_WATER_THRESHOLDS[currentStage]) {
            currentStage++;
            waterAtCurrentStage = 0;

            if (currentStage < GROWTH_STAGE_COUNT) {
                JOptionPane.showMessageDialog(
                    this,
                    GrowthStage.values()[currentStage].description,
                    "Growth Progress",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Your plant is in full bloom. Additional watering may cause decline.",
                    "Flowering Plant",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
    
    private void handleDeclineWatering() throws OverWateringException {
        // For flowering plant, allow some watering but too much causes decline
        if (currentStage == GROWTH_STAGE_COUNT) {
            if (waterAtCurrentStage <= SAFE_WATER_AT_FLOWERING) {
                JOptionPane.showMessageDialog(
                    this,
                    "The flowering plant absorbed the water. Give it time before watering again.",
                    "Hydrated",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            advanceDeclineStage();
            return;
        }

        advanceDeclineStage();
    }
    
    private void advanceDeclineStage() throws OverWateringException {
        if (waterAtCurrentStage < DECLINE_WATER_THRESHOLD) {
            return;
        }

        if (currentStage >= TOTAL_STAGE_COUNT - 1) {
            throw new OverWateringException("The plant is already dead. Please reset to start over. Every ending is a new beginning.");
        }

        currentStage++;
        waterAtCurrentStage = 0;

        GrowthStage stage = GrowthStage.values()[currentStage];
        JOptionPane.showMessageDialog(
            this,
            stage.description,
            stage.displayName,
            JOptionPane.WARNING_MESSAGE
        );

        if (currentStage == TOTAL_STAGE_COUNT - 1) {
            throw new OverWateringException("The plant could not recover from overwatering and has died. Remember: Too much of anything destroys what you cherish most.");
        }
    }
    
    private void updatePlantDisplay() {
        GrowthStage stage = GrowthStage.values()[currentStage];

        plantPanel.setStage(currentStage);
        mainPanel.setBackground(stage.backgroundColor);

        stageLabel.setText(stage.displayName);
        descriptionLabel.setText(stage.description);

        waterButton.setEnabled(currentStage < TOTAL_STAGE_COUNT - 1);
    }

    private void resetPlant() {
        currentStage = 0;
        waterAtCurrentStage = 0;
        waterButton.setEnabled(true);
        updatePlantDisplay();

        JOptionPane.showMessageDialog(
            this,
            "New beginning: Every seed holds infinite potential. Nurture wisely.",
            "Reset Complete",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        // Set system properties for better rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        System.setProperty("sun.java2d.dpiaware", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            // Use default look and feel if system L&F fails
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Last resort - use default
            }
        }

        SwingUtilities.invokeLater(() -> new VirtualPlantGrowthSimulator());
    }
}