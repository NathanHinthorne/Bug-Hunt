package com.Butterfly.view.GameScreen;

import com.Butterfly.model.cards.Card;
import com.Butterfly.model.players.Player;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.scene.layout.StackPane;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerCollection extends VBox {
    public static final int CARD_SIZE = 50;
    public static final int PADDING = 5;
    public static final double CORNER_RADIUS = 12.0;
    private final Player player;
    private Text text;
    private final FlowPane cardPane;
    private final Map<Card, ImageView> cardViews;
    private Font funFont;

    public PlayerCollection(Player player) {
        this.player = player;
        cardViews = new HashMap<>();

        // Load the custom font
        Font funFont = Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 20);

        // Set the font for the player's name and score
        text = new Text(updatedText());
        text.setFont(funFont);

        this.setPrefWidth(200);

        // Add the text to the top of the VBox
        this.getChildren().add(0, text);

        // Set the padding for the VBox
        this.setPadding(new Insets(10));

        // Set padding between the text and the cards
        this.setSpacing(10);

        // Create a FlowPane to hold the cards
        cardPane = new FlowPane();
        cardPane.setHgap(PADDING); // Horizontal gap between cards
        cardPane.setVgap(PADDING); // Vertical gap between cards

        this.getChildren().add(cardPane);
    }

    private String updatedText() {
        StringBuilder playerInfo = new StringBuilder();

        // display player's name
        playerInfo.append(player.getName()).append("\n");

        // display player's score
        playerInfo.append("Score: ").append(player.getScore());

        return playerInfo.toString();
    }

    private void addCard(Card card) {
        ImageView cardImage = createCardView(card.getImage());

        Rectangle overlay = createNewCardOverlay();
        quickOverlayGlow(overlay);

        // create a stackpane to hold the card and the overlay
        StackPane cardWithOverlay = new StackPane();
        cardWithOverlay.getChildren().addAll(cardImage, overlay);

        cardViews.put(card, cardImage);
        cardPane.getChildren().add(cardWithOverlay);
    }

    private Rectangle createNewCardOverlay() {
        Rectangle overlay = new Rectangle(CARD_SIZE, CARD_SIZE, Color.TRANSPARENT); // default color is transparent
        overlay.setArcWidth(CORNER_RADIUS);
        overlay.setArcHeight(CORNER_RADIUS);

        overlay.setFill(Color.YELLOW);
        overlay.setOpacity(0.20);
        return overlay;
    }

    private void quickOverlayGlow(Rectangle overlay) {

        // Create a DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.YELLOW);
        dropShadow.setRadius(12);
        dropShadow.setSpread(0.8);

        // Apply the DropShadow effect to the overlay
        overlay.setEffect(dropShadow);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(dropShadow.radiusProperty(), 10.0)),
                new KeyFrame(Duration.seconds(0.35), new KeyValue(dropShadow.radiusProperty(), 30.0)),
                new KeyFrame(Duration.seconds(0.7), new KeyValue(dropShadow.radiusProperty(), 10.0)));
        timeline.setCycleCount(3);
        timeline.setOnFinished(event -> {
            overlay.setEffect(null);
            overlay.setFill(Color.TRANSPARENT);
        });
        timeline.play();
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

    public void update() {
        // Get the current cards in the player's collection
        Set<Card> currentCards = new HashSet<>(player.getDisorganizedCards());

        // Add the new cards to the player's collection
        for (Card card : currentCards) {
            if (!cardViews.containsKey(card)) {
                addCard(card);
            }
        }

        // Update the player's name and score
        text.setText(updatedText());
    }
}