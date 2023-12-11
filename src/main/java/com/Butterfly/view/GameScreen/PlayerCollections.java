package com.Butterfly.view.GameScreen;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.players.Player;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PlayerCollections extends StackPane { // shows up on the left side of the board

    private final Board board;
    public PlayerCollections(Board board) {
        this.board = board;

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        update();
    }

    private void update() {
        this.getChildren().add(new Text("(Player Collections)"));
    }

//    private void update() {
//        // show each players score on the StackPane, starting from the first player
//
//        for (int i = 0; i < board.getPlayers().size(); i++) {
//            Player player = board.getPlayers().get(i);
//
//            StringBuilder playerInfo = new StringBuilder();
//
//            // display player's name
//            playerInfo.append(player.getName()).append("\n");
//
//            // display player's collection
//            playerInfo.append(player.getCollection().toString()).append("\n"); // TODO: change this from text to images
//
//            // display player's score
//            playerInfo.append("Score: ").append(player.getScore());
//
//            // add the player's info to the StackPane
//            Text text = new Text(playerInfo.toString());
//            this.getChildren().add(text);
//        }
//    }
}
