package com.plantgrowth;

import java.util.Scanner;

/**
 * Console-based Virtual Plant Growth Simulator
 * Provides an interactive text-based interface when GUI is not available
 */
public class ConsolePlantSimulator {
    private static final int GROWTH_STAGE_COUNT = 4;
    private static final int TOTAL_STAGE_COUNT = 7;
    private static final int[] GROWTH_WATER_THRESHOLDS = {1, 2, 2, Integer.MAX_VALUE};
    private static final int SAFE_WATER_AT_FLOWERING = 3;
    private static final int DECLINE_WATER_THRESHOLD = 1;

    private int currentStage = 0;
    private int waterAtCurrentStage = 0;
    private int totalWaterGiven = 0;
    private final Scanner scanner;

    private enum GrowthStage {
        SEED("Seed", "A tiny seed settling into the soil."),
        SPROUT("Sprout", "First leaves are unfurling."),
        YOUNG_PLANT("Young Plant", "The stem is strengthening and new leaves appear."),
        FLOWERING_PLANT("Flowering Plant", "The plant is in full bloom. Water with care."),
        WILTING("Wilting", "Too much water is stressing the plant."),
        DYING("Dying", "Roots are decaying and the stem is weakening."),
        DEAD("Dead", "The plant has died. Reset to try again.");

        private final String displayName;
        private final String description;

        GrowthStage(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
    }

    public ConsolePlantSimulator() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Virtual Plant Growth Simulator - Console Edition ===");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            displayStatus();
            displayMenu();

            System.out.print("Choose an option (1-3): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> waterPlant();
                case "2" -> resetPlant();
                case "3" -> {
                    System.out.println("Thanks for playing! Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private void displayStatus() {
        GrowthStage stage = GrowthStage.values()[currentStage];

        System.out.println("\n" + "=".repeat(50));
        System.out.println("PLANT STATUS");
        System.out.println("=".repeat(50));
        System.out.println(getPlantArt(currentStage));
        System.out.println("Stage: " + stage.displayName + " (" + (currentStage + 1) + "/" + TOTAL_STAGE_COUNT + ")");
        System.out.println("Description: " + stage.description);
        System.out.println("Total water given: " + totalWaterGiven);
        System.out.println("Water at current stage: " + waterAtCurrentStage);
        System.out.println("Progress: " + getProgressBar(currentStage, TOTAL_STAGE_COUNT - 1));
    }

    private void displayMenu() {
        System.out.println("\n" + "─".repeat(30));
        System.out.println("MENU");
        System.out.println("─".repeat(30));
        System.out.println("1. Water the plant");
        System.out.println("2. Reset plant");
        System.out.println("3. Exit");
        System.out.println("─".repeat(30));
    }

    private void waterPlant() {
        totalWaterGiven++;
        waterAtCurrentStage++;

        System.out.println("Watering your plant...");

        try {
            if (currentStage < GROWTH_STAGE_COUNT - 1) {
                handleGrowthWatering();
            } else {
                handleDeclineWatering();
            }
        } catch (OverWateringException e) {
            System.out.println("OVERWATERING ALERT: " + e.getMessage());
            System.out.println("The plant has died. Please reset to try again.");
            return;
        }

        // Small delay for better UX
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleGrowthWatering() {
        if (waterAtCurrentStage >= GROWTH_WATER_THRESHOLDS[currentStage]) {
            currentStage++;
            waterAtCurrentStage = 0;

            if (currentStage < GROWTH_STAGE_COUNT) {
                GrowthStage newStage = GrowthStage.values()[currentStage];
                System.out.println("GROWTH PROGRESS: " + newStage.description);
                System.out.println("Your plant advanced to: " + newStage.displayName);
            } else {
                System.out.println("Your plant is in full bloom! Additional watering may cause decline.");
            }
        } else {
            System.out.println("Plant received water. Keep watering to advance to the next stage!");
        }
    }

    private void handleDeclineWatering() throws OverWateringException {
        if (currentStage == GROWTH_STAGE_COUNT) {
            if (waterAtCurrentStage <= SAFE_WATER_AT_FLOWERING) {
                System.out.println("The flowering plant absorbed the water. Give it time before watering again.");
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
            throw new OverWateringException("The plant is already dead. Please reset to start over.");
        }

        currentStage++;
        waterAtCurrentStage = 0;

        GrowthStage stage = GrowthStage.values()[currentStage];
        System.out.println("WARNING: " + stage.displayName.toUpperCase() + ": " + stage.description);

        if (currentStage == TOTAL_STAGE_COUNT - 1) {
            throw new OverWateringException("The plant could not recover from overwatering and has died.");
        }
    }

    private void resetPlant() {
        currentStage = 0;
        totalWaterGiven = 0;
        waterAtCurrentStage = 0;
        System.out.println("Plant has been reset to seed stage!");
        System.out.println("Start watering your new seed to begin growth.");
    }

    private String getPlantArt(int stage) {
        return switch (stage) {
            case 0 -> """
                    .
                     |
                    """;
            case 1 -> """
                    *
                     |
                     |
                    """;
            case 2 -> """
                    * *
                     |
                     |
                     |
                    """;
            case 3 -> """
                    @ * @
                     |
                     |
                     |
                     |
                    """;
            case 4 -> """
                    ~ * ~
                     |
                     |
                     |
                    """;
            case 5 -> """
                    X
                     |
                     |
                    """;
            case 6 -> """
                    [RIP]
                    """;
            default -> ".";
        };
    }

    private String getProgressBar(int current, int max) {
        int barLength = 20;
        int filled = (int) ((double) current / max * barLength);
        StringBuilder bar = new StringBuilder("[");

        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("█");
            } else if (i == filled) {
                bar.append("▒");
            } else {
                bar.append("░");
            }
        }
        bar.append("] ").append(current + 1).append("/").append(max + 1);
        return bar.toString();
    }

    public static void main(String[] args) {
        new ConsolePlantSimulator().run();
    }
}