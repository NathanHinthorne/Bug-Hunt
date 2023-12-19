package com.Butterfly.view.GameScreen;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.players.Player;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FinalScores extends VBox {

    private Board board;

    public FinalScores(Board board) {
        this.board = board;
        this.setSpacing(30);
        this.setAlignment(Pos.CENTER);
        initializeBoard();
    }

    private void initializeBoard() {
        for (Player player : board.getPlayers()) {
            this.getChildren().add(new Scoreboard(player));
        }
    }

    public void popInCards() {
        for (int i = 0; i < this.getChildren().size(); i++) {
            Scoreboard scoreboard = (Scoreboard) this.getChildren().get(i);
            scoreboard.popInCards();
        }
    }

}
