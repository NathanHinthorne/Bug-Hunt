package com.BugHunt.view.GameScreen;

import com.BugHunt.model.board.Board;
import com.BugHunt.model.board.BoardObserver;
import com.BugHunt.model.board.GlobalDir;
import com.BugHunt.model.cards.Card;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class VisualBoard extends GridPane implements BoardObserver, java.io.Serializable {

    public static final int CARD_SIZE = 60;
    public static final int CARD_PADDING = 10;
    public static final double CORNER_RADIUS = 12.0; // Adjust the radius as needed

    private final Board board;
    private final Rectangle[][] overlays;
    private final ImageView[][] cards;
    private ImageView hedgehogImage;
    private final ArrayList<Node> oldImages;
    private boolean isWobbling;
    private boolean isFlipped;

    public VisualBoard(Board board) {

        this.board = board;
        oldImages = new ArrayList<>();
        overlays = new Rectangle[board.getHeight()][board.getWidth()];
        cards = new ImageView[board.getHeight()][board.getWidth()];

        board.addObserver(this); // Registering VisualBoard as an observer

        this.setPadding(new Insets(10));
        this.setHgap(CARD_PADDING);
        this.setVgap(CARD_PADDING);

        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/scenery/meadow.jpg"));
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true));
        this.setBackground(new Background(background));

        initializeBoard();
    }

    @Override
    public void onBoardStateChanged(boolean readyToMove) {
        if (readyToMove) {
            update();
            wobbleHedgehog();
            glowHedgehog();
            this.requestFocus();
            enableClicking(true);
            enableKeyPressing(true);

        } else {
            enableClicking(false);
            enableKeyPressing(false);
            update();

            board.playerMoveCompleted(); // ending statement
        }
    }

    public void computerAnimations() {
        update();
        wobbleHedgehog();
        glowHedgehog();
    }

    private void initializeBoard() {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {

                placeCard(x, y);
                addCardOverlay(x, y);
            }
        }

        updateHedgehog();
        addKeyListeners();
    }

    private void addKeyListeners() {
        this.setOnKeyPressed(event -> {
            board.keyPressed(event.getCode());
        });
    }

    public void update() {

        // if this is the first update, don't take the card the hedgehog is on
        if (!board.containsMaxCards()) {
            leaveEmptyCard(board.getHedgehog().getX(), board.getHedgehog().getY());
        }

        clearOldImages();

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {

                updateCardOverlay(x, y);
            }
        }

        updateHedgehog();
    }

    public void leaveAllEmptyCards() {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {

                leaveEmptyCard(x, y);
            }
        }
    }

    private void leaveEmptyCard(int x, int y) {

        // find the card the hedgehog is on
        ImageView cardImage = cards[x][y];
        oldImages.add(cardImage);

        // replace with empty card
        ImageView emptyCardImage = createCardView(new Image(getClass().getResourceAsStream("/images/cards/empty.png")));
        this.add(emptyCardImage, x, y);

        // add net if necessary
        if (board.hasNet(x, y)) {
            ImageView netImage = createCardView(new Image(getClass().getResourceAsStream("/images/net.png")));
            this.add(netImage, x, y);
        }
    }

    private void updateHedgehog() {
        hedgehogImage = createHedgehogImage(board.getHedgehog().getImage());
        rotateHedgehog(hedgehogImage, board.getHedgehog().getDir());
        this.add(hedgehogImage, board.getHedgehog().getX(), board.getHedgehog().getY());
        oldImages.add(hedgehogImage);
    }

    private void rotateHedgehog(ImageView imageView, GlobalDir dir) {
        switch (dir) {
            case NORTH:
                imageView.setRotate(90);
                isFlipped = false;
                break;
            case EAST:
//                imageView.setScaleX(-1);
                imageView.setRotate(180);
                isFlipped = true;
                break;
            case SOUTH:
                imageView.setRotate(270);
                isFlipped = false;
                break;
            case WEST:
                imageView.setRotate(0);
                isFlipped = false;
                break;
        }
    }

    /**
     * Wobble hedgehog back and forth to indicate that it's ready to move
     */
    private void wobbleHedgehog() {
        double initialRotation = hedgehogImage.getRotate();

        // Create a ScaleTransition that enlarges the hedgehog to 1.5 times its original
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), hedgehogImage);
        scaleTransition.setToX(1.3);
        scaleTransition.setToY(1.3);

        // Create a RotateTransition that rotates the hedgehog back and forth by 15
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.3), hedgehogImage);
        if (board.getHedgehog().getDir() == GlobalDir.EAST) {
            //TODO if hedgehog is facing east, keep the image flipped
        }
        rotateTransition.setFromAngle(initialRotation - 15);
        rotateTransition.setToAngle(initialRotation + 15);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(4); // Rotate back and forth 4 times

        // Create a ScaleTransition that shrinks the hedgehog back to its original size
        ScaleTransition shrinkTransition = new ScaleTransition(Duration.seconds(0.3), hedgehogImage);
        shrinkTransition.setToX(1);
        shrinkTransition.setToY(1);

        // Create a RotateTransition that rotates the hedgehog back to its default
        // upright position
        RotateTransition rotateBackTransition = new RotateTransition(Duration.seconds(0.3), hedgehogImage);
        rotateBackTransition.setToAngle(initialRotation);

        // Create a ParallelTransition that plays the shrinkTransition and
        // rotateBackTransition at the same time
        ParallelTransition parallelTransition = new ParallelTransition(shrinkTransition, rotateBackTransition);

        // Create a SequentialTransition that plays the scaleTransition,
        // rotateTransition, and parallelTransition in sequence
        SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition, rotateTransition,
                parallelTransition);

        // Play the SequentialTransition
        sequentialTransition.play();
        isWobbling = true;

        // When the SequentialTransition is finished, set isWobbling to false
        sequentialTransition.setOnFinished(event -> isWobbling = false);
    }

    private void glowHedgehog() {
        // Create a Glow effect
        Glow glow = new Glow();
        // Set the initial glow level
        glow.setLevel(0.0);

        // Create a DropShadow effect
        DropShadow dropShadow = new DropShadow();
        // Set the color of the drop shadow to the color of the glow
        dropShadow.setColor(Color.WHITE);
        // Set the radius of the drop shadow to make it cover a larger area
        dropShadow.setRadius(20.0); // Increase this value to make the glow larger
        // Set the input of the DropShadow effect to the Glow effect
        dropShadow.setInput(glow);

        // Apply the DropShadow effect to the hedgehog image
        hedgehogImage.setEffect(dropShadow);

        // Create a KeyValue that animates the glow level from 0.0 to 1.0
        KeyValue keyValueStart = new KeyValue(glow.levelProperty(), 1.0);
        // Create a KeyFrame that starts the glow at the start of the animation
        KeyFrame keyFrameStart = new KeyFrame(Duration.ZERO, keyValueStart);

        // Create a KeyValue that animates the glow level from 1.0 to 0.0
        KeyValue keyValueEnd = new KeyValue(glow.levelProperty(), 0.0);
        // Create a KeyFrame that ends the glow at the end of the animation
        KeyFrame keyFrameEnd = new KeyFrame(Duration.seconds(3), keyValueEnd); // Increase this value to make the glow
                                                                               // last longer

        // Create a Timeline that plays the KeyFrames
        Timeline timeline = new Timeline(keyFrameStart, keyFrameEnd);
        // Set the Timeline to play once
        timeline.setCycleCount(1);
        // Play the Timeline
        timeline.play();
    }

    private void addCardOverlay(int x, int y) {
        Rectangle overlay = new Rectangle(CARD_SIZE, CARD_SIZE, Color.TRANSPARENT); // default color is transparent
        overlay.setTranslateX(-CARD_PADDING / 4); // move overlay a little to the left
        overlay.setArcWidth(CORNER_RADIUS);
        overlay.setArcHeight(CORNER_RADIUS);

        // quickOverlayGlow(overlay);

        if (board.getCard(x, y).isHighlighted()) {
            overlay.setStroke(Color.YELLOW);
            overlay.setStrokeWidth(4);
        }

        overlay.setOnMouseEntered(event -> {
            if (board.getCard(x, y).isHighlighted()) {
                overlay.setStroke(Color.RED);
                overlay.setStrokeWidth(4);
            }
        });
        overlay.setOnMouseExited(event -> { // do we need this? or is re-highlighting done by update()?
            if (board.getCard(x, y).isHighlighted()) {
                overlay.setStroke(Color.YELLOW);
                overlay.setStrokeWidth(4);
            }
        });

        overlay.setOnMousePressed(event -> {
            if (board.getCard(x, y).isHighlighted()) {
                overlay.setFill(Color.BLACK);
                overlay.setOpacity(0.8);
            }
        });

        overlay.setOnMouseReleased(event -> {
            if (board.getCard(x, y).isHighlighted()) {
                overlay.setFill(Color.TRANSPARENT);

                handlePlayerInput(x, y);
            }
        });

        this.add(overlay, x, y);
        overlays[x][y] = overlay;
    }

    private void quickOverlayGlow(Rectangle overlay) {
        // Create a DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.YELLOW);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.6);

        // Apply the DropShadow effect to the overlay
        overlay.setEffect(dropShadow);

        // Add a PauseTransition to remove the glow effect after some time
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> overlay.setEffect(null));
        pause.play();
    }

    private void handlePlayerInput(int x, int y) {
        board.move(x, y);

        board.setReadyToMove(false);
    }

    private void updateCardOverlay(int x, int y) {
        Rectangle overlay = overlays[x][y];

        if (board.getCard(x, y).isHighlighted()) {
            overlay.setStroke(Color.YELLOW);
            overlay.setStrokeWidth(4);
            // quickOverlayGlow(overlay);
        } else {
            overlay.setStroke(Color.TRANSPARENT);
            overlay.setStrokeWidth(4);
        }
    }

    private void placeCard(int x, int y) {
        Card card = board.getCard(x, y);
        ImageView cardImage = createCardView(card.getImage());
        cards[x][y] = cardImage;
        this.add(cardImage, x, y);
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

    private ImageView createHedgehogImage(Image image) {
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(CARD_SIZE / 1.2);
        imageView.setFitWidth(CARD_SIZE / 1.2);

        // adjust to center
        imageView.setTranslateX(CARD_SIZE / 10);

        return imageView;
    }

    private void clearOldImages() {
        for (Node thing : oldImages) {
            this.getChildren().remove(thing);
        }
        oldImages.clear();
    }

    private void enableClicking(boolean clickable) {
        // for (Rectangle[] row : overlays) {
        // for (Rectangle overlay : row) {
        // overlay.setDisable(!clickable);
        // }
        // }
        this.setDisable(!clickable);
    }

    private void enableKeyPressing(boolean canPressKeys) {
        // TODO: implement
    }

}
