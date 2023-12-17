package com.Butterfly.view.GameScreen;

import com.Butterfly.model.board.Board;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameInfo extends FlowPane {
    private final Board board;
    private final Label title;
    private final Text gameInfoText;

    public GameInfo(Board board) {
        this.board = board;

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        // Set the padding for the VBox
        this.setPadding(new Insets(10));

        // Load the custom font
        Font funFont = Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 20);

        // Create the Text object for the title and set the font
        Text titleText = new Text("-- Game Info --");
        title = new Label(titleText.getText());
        title.setFont(funFont);
        // center the title
        title.setPadding(new Insets(0, 0, 0, 40));
        this.getChildren().add(title);

        // Create the Text object for the game info and set the font
        gameInfoText = new Text();
        gameInfoText.setFont(funFont);
        this.getChildren().add(gameInfoText);

        initialize();
    }

    private void initialize() {
        String gameInfo = "\n\n" +
                "Current Player: " + board.getCurrentPlayer().getName() + "\n\n" +
                "Hedgehog Facing: " + board.getHedgehog().getDir();
        gameInfoText.setText(gameInfo);
    }

    void update() {
        String gameInfo = "\n\n" +
                "Current Player: " + board.getCurrentPlayer().getName() + "\n\n" +
                "Hedgehog Facing: " + board.getHedgehog().getDir();
        gameInfoText.setText(gameInfo);
    }
}