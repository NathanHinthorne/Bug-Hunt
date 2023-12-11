package com.Butterfly.view.IntroScreen;

import com.Butterfly.view.AsciiArt;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class IntroScreen {
    private final Stage introStage;
    private ImageView startButton; // not a button object because it needs to have customized look
    private EventHandler<ActionEvent> startButtonListener;

    private Slider humanPlayerSlider;
    private Slider comPlayerSlider;
    private CheckBox checkBox1;
    private ArrayList<TextField> playerNames;

    public IntroScreen(final Stage introStage) {
        this.introStage = introStage;
        playerNames = new ArrayList<>();

        setupButton();
        setupIcon();
        setupScene();
        intializeCSS();
    }



    // button stuff
    private void intializeCSS() {
        // Load the custom CSS file
        introStage.getScene().getStylesheets().add(getClass().getResource("/styling/button.css").toExternalForm());
    }
    private void setupButton() {
        // Load button images
        Image normalImage = loadImage("/images/button_normal.png");
        Image shadedImage = loadImage("/images/button_shaded.png");

        // Create an ImageView with the normal image
        startButton = new ImageView(normalImage);

        // Set the size of the button
        startButton.setPreserveRatio(true); // Maintain the aspect ratio
        startButton.setFitWidth(100);

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
    public void setNextButtonListener(EventHandler<ActionEvent> listener) {
        this.startButtonListener = listener;
    }



    private void setupIcon() {
        // Load the icon image
        Image iconImage = loadImage("/images/icon.jpg");

        // Set the icon of the stage
        introStage.getIcons().add(iconImage);
    }

    public void hide() {
        introStage.hide();
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResource(path).toExternalForm());
    }


    private void setupScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label butterflies = new Label("\n" + AsciiArt.butterflies());
        Label title = new Label(AsciiArt.butterflyTitle());

        // Set the font to a monospaced font
        Font monospaceFont = Font.font("Courier New", FontWeight.NORMAL, 12);
        butterflies.setFont(monospaceFont);
        title.setFont(monospaceFont);

        VBox labelsBox = new VBox(10);
        labelsBox.getChildren().addAll(butterflies, title);
        labelsBox.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));


        // Sliders, checkboxes, and text fields on the left side
        humanPlayerSlider = new Slider(1, 5, 2);
        comPlayerSlider = new Slider(0, 4, 0);

        // set up the sliders
        humanPlayerSlider.setShowTickLabels(true);
        humanPlayerSlider.setShowTickMarks(true);
        humanPlayerSlider.setMajorTickUnit(4);
        humanPlayerSlider.setBlockIncrement(1);
        humanPlayerSlider.setSnapToTicks(true);

        comPlayerSlider.setShowTickLabels(true);
        humanPlayerSlider.setShowTickMarks(true);
        comPlayerSlider.setMajorTickUnit(4);
        comPlayerSlider.setBlockIncrement(1);
        comPlayerSlider.setSnapToTicks(true);


        checkBox1 = new CheckBox("Option 1");

        for (int i = 0; i < humanPlayerSlider.getValue(); i++) {
            playerNames.add(new TextField());
        }

        VBox controlsBox = new VBox(10);
        controlsBox.getChildren().addAll(
                new VBox(10, new Label("Number of Human Players: "), humanPlayerSlider),
                new VBox(10, new Label("Number of Computer Players: "), comPlayerSlider),
                checkBox1
        );
        for (int i = 0; i < humanPlayerSlider.getValue(); i++) {
            controlsBox.getChildren().add(new VBox(10, new Label("Player " + (i + 1) + " Name:"), playerNames.get(i)));
        }
        controlsBox.getChildren().add(startButton);


        // Set the alignment for the controls box
        controlsBox.setAlignment(Pos.CENTER_LEFT);

        // Set up the root VBox
        HBox hbox = new HBox(50, labelsBox, controlsBox);
        hbox.setPadding(new Insets(20));

        Scene titleScene = new Scene(hbox);
        introStage.setScene(titleScene);
        introStage.setTitle("Butterfly - Intro Screen");
        introStage.show();
    }

    public int getNumberOfHumanPlayers() {
        return (int) humanPlayerSlider.getValue();
    }

    public int getNumberOfComputerPlayers() {
        return (int) comPlayerSlider.getValue();
    }

    public boolean getCheckBox1() {
        return checkBox1.isSelected();
    }



    public ArrayList<String> getPlayerNames() {
        ArrayList<String> names = new ArrayList<>();
        for (TextField name : playerNames) {
            names.add(name.getText());
        }
        return names;
    }
}