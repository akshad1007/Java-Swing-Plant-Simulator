package com.plantgrowth;

/**
 * Custom exception thrown when the plant is watered too many times.
 * Thrown by the VirtualPlantGrowthSimulator when the user exceeds the allowed
 * number of waterings for a fully-grown plant.
 */
public class OverWateringException extends Exception {
    /**
     * Create exception with message.
     * @param message detail message
     */
    public OverWateringException(String message) {
        super(message);
    }

    /**
     * Create exception with message and cause.
     * @param message detail message
     * @param cause root cause
     */
    public OverWateringException(String message, Throwable cause) {
        super(message, cause);
    }
}
