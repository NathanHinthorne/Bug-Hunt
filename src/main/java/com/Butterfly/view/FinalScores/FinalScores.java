package com.Butterfly.view.FinalScores;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.players.Player;
import javafx.scene.layout.HBox;

public class FinalScores extends HBox {

    private Board board;
    public FinalScores(Board board) {
        this.board = board;

        initializeBoard();
    }

    private void initializeBoard() {
        // add cushion between scoreboards
        this.setSpacing(30);

        // ensure window is a good size
        this.setMinWidth(500);
        this.setMinHeight(400);

        // center the scoreboards
        this.setTranslateX(50);



        for (Player player : board.getPlayers()) {
            this.getChildren().add(new Scoreboard(player));
        }


    }

}
