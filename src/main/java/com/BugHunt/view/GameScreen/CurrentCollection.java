package com.BugHunt.view.GameScreen;

import com.BugHunt.model.board.Board;
import com.BugHunt.model.players.Player;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class CurrentCollection extends Pane { // shows up on the left side of the board
    private final Board board;
    private Map<Player, PlayerCollection> playerCollections;
    private ImageView finishTurnButton;
    private EventHandler<ActionEvent> finishTurnButtonListener;

    public CurrentCollection(Board board) {
        this.board = board;

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        initialize();
    }

    private void initialize() {
        playerCollections = new HashMap<>();
        for (int i = 0; i < board.getPlayers().size(); i++) {
            Player player = board.getPlayers().get(i);
            playerCollections.put(player, new PlayerCollection(player));
            this.getChildren().add(playerCollections.get(player));
            // hide all but the first player's collection
            if (i != 0) {
                playerCollections.get(player).setVisible(false);
            }
        }
        update();

        addButton();
    }

    // public void update1() {
    // this.getChildren().clear();

    // PlayerCollection playerCollection =
    // playerCollections.get(board.getCurrentPlayer());
    // playerCollection.update();
    // this.getChildren().add(playerCollection);
    // }

    public void update() {
        PlayerCollection currentCollection = playerCollections.get(board.getCurrentPlayer());
        currentCollection.update();
    }

    public void nextPlayerCollection() {
        // Get the current player's collection
        PlayerCollection oldCollection = playerCollections.get(board.getCurrentPlayer());
        PlayerCollection newCollection = playerCollections.get(board.getNextPlayer());

        // Create a TranslateTransition that moves the old collection to the left
        TranslateTransition moveOldOut = new TranslateTransition(Duration.seconds(0.8), oldCollection);
        moveOldOut.setByX(-50); // Adjust as needed

        // Create a FadeTransition that fades out the old collection
        FadeTransition fadeOldOut = new FadeTransition(Duration.seconds(0.8), oldCollection);
        fadeOldOut.setFromValue(1.0);
        fadeOldOut.setToValue(0.0);

        // After the old collection has faded out, remove it
        fadeOldOut.setOnFinished(e -> oldCollection.setVisible(false));

        // Play the transitions for the old collection together
        ParallelTransition oldCollectionTransition = new ParallelTransition(moveOldOut, fadeOldOut);

        oldCollectionTransition.setOnFinished(e -> {
            // Make the new collection visible and update it
            newCollection.setVisible(true);

            // Create a TranslateTransition that moves the new collection from the right
            TranslateTransition moveNewIn = new TranslateTransition(Duration.seconds(1), newCollection);
            moveNewIn.setFromX(50); // Adjust as needed
            moveNewIn.setToX(0);

            // Create a FadeTransition that fades in the new collection
            FadeTransition fadeNewIn = new FadeTransition(Duration.seconds(0.8), newCollection);
            fadeNewIn.setFromValue(0.0);
            fadeNewIn.setToValue(1.0);

            // Play the transitions for the new collection together
            ParallelTransition newCollectionTransition = new ParallelTransition(moveNewIn, fadeNewIn);

            // Play the transitions for the new collection
            newCollectionTransition.play();
        });

        // Play the transitions for the old collection
        oldCollectionTransition.play();
    }

    private void addButton() {
        // Load button images
        Image normalImage = loadImage("/images/button_finish_turn.png");
        Image shadedImage = loadImage("/images/button_finish_turn_shaded.png");

        // Create an ImageView with the normal image
        finishTurnButton = new ImageView(normalImage);

        // Set the size of the button
        finishTurnButton.setPreserveRatio(true); // Maintain the aspect ratio
        finishTurnButton.setFitWidth(100);

        // Position the button at the bottom right corner
        finishTurnButton.layoutXProperty()
                .bind(this.widthProperty().subtract(finishTurnButton.fitWidthProperty().add(10)));
        finishTurnButton.layoutYProperty()
                .bind(this.heightProperty().subtract(finishTurnButton.fitHeightProperty().add(50)));
        try {
            setupButtonListeners(normalImage, shadedImage);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        applyHoverEffect();
        this.getChildren().add(finishTurnButton);
    }

    private void setupButtonListeners(Image theNormalImage, Image theShadedImage) throws URISyntaxException {
        // AudioManager audio = AudioManager.getInstance();

        finishTurnButton.setOnMousePressed(event -> {
            // Change the image to the shaded image when pressed
            finishTurnButton.setImage(theShadedImage);

            // Play press sound
            // audio.playSFX(audio.mouseClick, 100);

            // Shift the button's position slightly down and to the right
            double shiftAmount = 3; // Adjust this value as needed
            // finishTurnButton.setTranslateX(finishTurnButton.getTranslateX() +
            // shiftAmount);
            // finishTurnButton.setTranslateY(finishTurnButton.getTranslateY() +
            // shiftAmount);
        });

        finishTurnButton.setOnMouseReleased(event -> {

            // Change the image back to normal when released
            finishTurnButton.setImage(theNormalImage);

            if (finishTurnButtonListener != null) {
                ActionEvent actionEvent = new ActionEvent(finishTurnButton, null);
                finishTurnButtonListener.handle(actionEvent);
            } else {
                System.out.println("No listener for start button!");
            }

            // Reset the button's position
            // finishTurnButton.setTranslateX(0);
            // finishTurnButton.setTranslateY(0);
        });
    }

    private void applyHoverEffect() {
        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(javafx.scene.paint.Color.rgb(50, 50, 50, 0.2));
        glowEffect.setRadius(6);
        glowEffect.setSpread(1);

        finishTurnButton.setOnMouseEntered(event -> finishTurnButton.setEffect(glowEffect));
        finishTurnButton.setOnMouseExited(event -> finishTurnButton.setEffect(null));
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public void setFinishTurnButtonListener(EventHandler<ActionEvent> listener) {
        this.finishTurnButtonListener = listener;
    }

    public ImageView getFinishTurnButton() {
        return finishTurnButton;
    }

}