package com.BugHunt.view.TitleScreen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URISyntaxException;

public class TitleScreen extends Parent {

    private final Stage primaryStage;
    private ImageView backgroundImage;
    private ImageView startButton; // not a button object because it needs to have customized look
    private EventHandler<ActionEvent> startButtonListener;

    public TitleScreen(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupImage();
        setupButton();
        setupIcon();
        setupScene();
        intializeCSS();
    }

    private void intializeCSS() {
        // Load the custom CSS file
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/styling/button.css").toExternalForm());
    }

    private void setupImage() {
        // Load the original image
//        Image originalImage = loadImage("/images/icon.jpg");
        Image originalImage = loadImage("/images/background0.png");

        // Create an ImageView with the resized image
        backgroundImage = new ImageView(originalImage);
        backgroundImage.setPreserveRatio(true); // Maintain the aspect ratio

        backgroundImage.setSmooth(true);
        backgroundImage.setCache(true);
        backgroundImage.setCacheHint(CacheHint.QUALITY);

        // shrink image to 600 width and 700 height
        backgroundImage.setFitHeight(500);
    }

    private void setupButton() {
        // Load button images
        Image normalImage = loadImage("/images/button_normal.png");
        Image shadedImage = loadImage("/images/button_shaded.png");

        // Create an ImageView with the normal image
        startButton = new ImageView(normalImage);

        // Set the size of the button
        double buttonWidth = normalImage.getWidth() / 3;
        double buttonHeight = normalImage.getHeight() / 3;
        startButton.setFitWidth(buttonWidth);
        startButton.setFitHeight(buttonHeight);

        try {
            setupButtonListeners(normalImage, shadedImage);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        applyHoverEffect();
    }

    private void setupButtonListeners(Image theNormalImage, Image theShadedImage) throws URISyntaxException {
//        AudioManager audio = AudioManager.getInstance();

        startButton.setOnMousePressed(event -> {
            // Change the image to the shaded image when pressed
            startButton.setImage(theShadedImage);

            // Play press sound
//            audio.playSFX(audio.mouseClick, 100);

            // Shift the button's position slightly down and to the right
            double shiftAmount = 3; // Adjust this value as needed
            startButton.setTranslateX(startButton.getTranslateX() + shiftAmount);
            startButton.setTranslateY(startButton.getTranslateY() + shiftAmount);
        });

        startButton.setOnMouseReleased(event -> {

            // Change the image back to normal when released
            startButton.setImage(theNormalImage);

            if (startButtonListener != null) {
                ActionEvent actionEvent = new ActionEvent(startButton, null);
                startButtonListener.handle(actionEvent);
            } else {
                System.out.println("No listener for start button!");
            }

            // Reset the button's position
            startButton.setTranslateX(0);
            startButton.setTranslateY(0);
        });
    }

    private void applyHoverEffect() {
        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(javafx.scene.paint.Color.rgb(50, 50, 50, 0.2));
        glowEffect.setRadius(6);
        glowEffect.setSpread(1);

        startButton.setOnMouseEntered(event -> startButton.setEffect(glowEffect));
        startButton.setOnMouseExited(event -> startButton.setEffect(null));
    }

    private void setupIcon() {
        // Load the icon image
//        Image iconImage = loadImage("/images/icon.jpg");
        Image iconImage = loadImage("/images/net.png");

        // Set the icon of the stage
        primaryStage.getIcons().add(iconImage);
    }

    private void setupScene() {
        StackPane root = new StackPane();

        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(startButton, new Insets(0, 0, 150, 0));

        root.getChildren().addAll(backgroundImage, startButton);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Bug Hunt - Title");
        primaryStage.show();
    }

    public void hide() {
        primaryStage.hide();
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public void setStartButtonListener(EventHandler<ActionEvent> listener) {
        this.startButtonListener = listener;
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
