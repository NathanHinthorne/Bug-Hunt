package com.BugHunt.view.GameScreen;

import com.BugHunt.model.cards.Card;
import com.BugHunt.model.players.Player;

import com.BugHunt.view.AudioManager;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;

public class Scoreboard extends HBox {
    public static final int CARD_SIZE = 35;
    public static final int PADDING = 10;
    public static final double CORNER_RADIUS = 12.0; // Adjust the radius as needed
    private static AudioManager audio;

    private Player player;
    private ArrayList<ImageView> cardViews;

    public Scoreboard(Player player) {
        this.player = player;
        this.cardViews = new ArrayList<>();
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);

        audio = AudioManager.getInstance();

        initializeScoreboard();
    }

    private void initializeScoreboard() {
        Font funFontBig = Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 40);
        Font funFontSmall = Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 30);

        Label playerName = new Label(player.getName());
        playerName.setFont(funFontBig);
        playerName.setTextFill(Color.BLUE);

        Label playerScore = new Label("Score: " + player.getScore());
        playerScore.setFont(funFontSmall);

        // add the player's score under their name
        VBox playerInfo = new VBox(playerName, playerScore);
        playerInfo.setAlignment(Pos.CENTER);
        this.getChildren().add(playerInfo);

        GridPane cards = new GridPane();
        cards.setHgap(10); // Horizontal gap
        cards.setVgap(10); // Vertical gap

        // add spacing between the top of the cards
        cards.setPadding(new Insets(10, 0, 10, 0));

        int maxRows = 2; // Maximum number of columns
        int column = 0;
        int row = 0;

        // add each card in the next available spot out of 3 rows
        for (Card card : player.getDisorganizedCards()) {
            ImageView cardImage = createCardView(card.getImage());
            cardImage.setVisible(false);
            cardViews.add(cardImage);
            cards.add(cardImage, column, row);

            row++;
            if (row >= maxRows) {
                row = 0;
                column++;
            }
        }

        // add gridpane to the scoreboard
        this.getChildren().add(cards);
    }

    public void popInCards() {
        for (int i = 0; i < cardViews.size(); i++) {
            // create a pauseTransition to delay each card's pop-in
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(200 * i));
            int finalI = i;
            pauseTransition.setOnFinished(event -> {
                ImageView cardView = cardViews.get(finalI);
                cardView.setVisible(true);
                audio.playSFX(audio.pop, 0.5);
            });
            pauseTransition.play();
        }
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
