package com.Butterfly.view.GameScreen;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.board.BoardObserver;
import com.Butterfly.model.board.GlobalDir;
import com.Butterfly.model.cards.Card;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class VisualBoard extends GridPane implements BoardObserver {

    public static final int CARD_SIZE = 60;
    public static final int PADDING = 10;
    public static final double CORNER_RADIUS = 12.0; // Adjust the radius as needed

    private final Board board;
    private final Rectangle[][] overlays;
    private final ImageView[][] cards;
    private final ArrayList<Node> oldImages;

    public VisualBoard(Board board) {

        this.board = board;

        board.addObserver(this); // Registering VisualBoard as an observer

        this.setPadding(new Insets(10));
        this.setHgap(PADDING);
        this.setVgap(PADDING);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        oldImages = new ArrayList<>();

        overlays = new Rectangle[board.getHeight()][board.getWidth()];
        cards = new ImageView[board.getHeight()][board.getWidth()];
        initializeBoard();
    }

    @Override
    public void onBoardStateChanged(boolean readyToMove) {
        if (readyToMove) {
            System.out.println("Ready to move!");
            board.highlightCards();
            enableClicking(true);

        } else {
            System.out.println("Not ready to move!");

            update();
            enableClicking(false);
            board.playerMoveCompleted(); // ending statement
        }
    }

    private void initializeBoard() {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {

                placeCard(x, y);
                addCardOverlay(x, y);
            }
        }

        updateHedgehog();
    }

    public void update() {
        clearOldImages();

        // get card the hedgehog is on


        // TODO remove card that was taken? or is it already gone?

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {

                updateCardOverlay(x, y);

//                if (board.getCard(x, y).isEmpty()) {
//                    ImageView imageView = createCardView(new Image("/images/empty.png"));
//                    this.add(imageView, x, y);
//                    takenCards.add(imageView);
//
//                    if (board.hasNet(x, y)) { // move this to initializeBoard()?
//                        displayNet(x, y);
//                    }
//                }
            }
        }

        updateHedgehog();
    }

    private void updateHedgehog() {
        ImageView imageView = createHedgehogImage(board.getHedgehog().getImage());
        rotateHedgehog(imageView, board.getHedgehog().getDir());
        this.add(imageView, board.getHedgehog().getX(), board.getHedgehog().getY());
        oldImages.add(imageView);
    }

    private void rotateHedgehog(ImageView imageView, GlobalDir dir) {
        switch (dir) {
            case NORTH:
                imageView.setRotate(90);
                break;
            case EAST:
                imageView.setScaleX(-1);
                break;
            case SOUTH:
                imageView.setRotate(270);
                break;
            case WEST:
                imageView.setRotate(0);
                break;
        }
    }

    private void removeCard(int x, int y) {

    }

    private void displayNet(int x, int y) {
        //
    }

    private void addCardOverlay(int x, int y) {
        Rectangle overlay = new Rectangle(CARD_SIZE, CARD_SIZE, Color.TRANSPARENT); // default color is transparent
        overlay.setArcWidth(CORNER_RADIUS);
        overlay.setArcHeight(CORNER_RADIUS);

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

                // TODO call method to perform move action
                board.move(x, y);
                update();

                board.setReadyToMove(false);
            }
        });

        this.add(overlay, x, y);
        overlays[x][y] = overlay;
    }

    private void updateCardOverlay(int x, int y) {
        Rectangle overlay = overlays[x][y];

        if (board.getCard(x, y).isHighlighted()) {
            overlay.setStroke(Color.YELLOW);
            overlay.setStrokeWidth(4);
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
        for (Rectangle[] row : overlays) {
            for (Rectangle overlay : row) {
                overlay.setDisable(!clickable);
            }
        }
    }

}
