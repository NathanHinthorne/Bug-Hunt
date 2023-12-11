package com.Butterfly.view.GameScreen;

import com.Butterfly.model.board.Board;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameInfo extends StackPane { // shows up on the right side of the board

    private final Board board;
    public GameInfo(Board board) {
        this.board = board;

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        initialize();
    }

    private void initialize() {
        update();
    }

    private void update() {
        this.getChildren().add(new Text("(Game Info)\n\n"));
        this.getChildren().add(new Text("Current Player: " + board.getCurrentPlayer().getName() + "\n\n"));
        this.getChildren().add(new Text("Hedgehog Facing: " + board.getHedgehog().getDir()));
    }

}
