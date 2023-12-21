package com.BugHunt.view.GameScreen;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class InfoBanner extends Pane {
    private Label infoLabel;

    public InfoBanner() {
        infoLabel = new Label();
        infoLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.otf"), 30));
        infoLabel.setPadding(new Insets(0, 0, 5, 0));

        this.getChildren().add(infoLabel);
    }

    public void displayInfo(String info, Color color, double seconds) {
        infoLabel.setTextFill(color);
        infoLabel.setText(info);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(seconds), infoLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }
}