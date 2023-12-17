package com.Butterfly.view.FinalScores;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.cards.Card;
import com.Butterfly.model.cards.CardFamily;
import com.Butterfly.model.players.Player;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Scoreboard extends StackPane {
    public static final int CARD_SIZE = 30;
    public static final int PADDING = 10;
    public static final double CORNER_RADIUS = 12.0; // Adjust the radius as needed

    private Board board;
    private Player player;
    public Scoreboard(Player player) {
        this.board = board;
        this.player = player;

        initializeScoreboard();
    }

    private void initializeScoreboard() {
        StringBuilder playerInfo = new StringBuilder();
        playerInfo.append("- " + player.getName() + " -").append("\n");
        playerInfo.append("Score: ").append(player.getScore()).append("\n");

        // list the cards in the player's collection
        playerInfo.append("Collection: ").append("\n");
        for (CardFamily cardFamily : CardFamily.values()) {

            ArrayList<Card> cardGroup = player.getOrganizedCards().get(cardFamily);

            for (int i = 0; i < cardGroup.size(); i++) {
                Card card = cardGroup.get(i);
                ImageView cardImage = createCardView(card.getImage());
                this.getChildren().add(cardImage);
            }
            playerInfo.append("\n");
        }

        Label label = new Label(playerInfo.toString());

        label.setFont(new javafx.scene.text.Font("Arial", 20));

        this.getChildren().add(label);
    }

    /**
     * Creates a card view with rounded corners
     *
     * @param image the image to display
     * @return the ImageView with rounded corners
     */
    private ImageView createCardView(Image image) {
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(CARD_SIZE);
        imageView.setFitWidth(CARD_SIZE);

        // Create a Rectangle to use as a clip for rounded corners
        Rectangle clip = new Rectangle(CARD_SIZE, CARD_SIZE);
        clip.setArcWidth(CORNER_RADIUS);
        clip.setArcHeight(CORNER_RADIUS);

        // Apply the clip to the ImageView
        imageView.setClip(clip);

        return imageView;
    }
}
