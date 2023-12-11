package com.Butterfly.controller;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

public class Delay {
    public static CompletableFuture<Void> pause(double seconds) {
        // Create a CompletableFuture to signal completion
        CompletableFuture<Void> completionIndicator = new CompletableFuture<>();

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(seconds));
        pauseTransition.setOnFinished(event -> {
            // Complete the CompletableFuture when the sequentialTransition finishes
            completionIndicator.complete(null);
        });

        // Start playing the sequentialTransition
        pauseTransition.play();

        // Return the CompletableFuture to the caller
        return completionIndicator;
    }
}
