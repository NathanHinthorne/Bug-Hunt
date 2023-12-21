package com.BugHunt.view.GameScreen;

import com.BugHunt.model.board.Board;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;

public class GameScreen {
    private final Stage primaryStage;
    private final Board board;
    private static BorderPane root;
    private static VBox everything;
    private static InfoBanner infoBanner;
    private static VisualBoard visualBoard;
    private static CurrentCollection currentCollection;
    private static Scene scene;
    private Text turnMessage;

    public GameScreen(final Stage primaryStage, final Board board, final boolean debugMode) {
        this.primaryStage = primaryStage;
        this.board = board;

        // setupTurnMessage();
        setupIcon();
        setupGameScene();

        if (!debugMode) {
            setupQuitConfirmation();
        }
    }

    private void setupQuitConfirmation() {
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // Consume the close request event to prevent immediate window closure

            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to quit?");

            // Show the confirmation dialog
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User clicked OK, so close the game stage
                    primaryStage.close();
                }
            });
        });
    }

    private void setupIcon() {
        // Set icon
//        InputStream stream = getClass().getResourceAsStream("/images/icon.jpg");
        InputStream stream = getClass().getResourceAsStream("/images/net.png");
        Image icon = new Image(stream);
        primaryStage.getIcons().add(icon);
    }

    private void setupGameScene() {
        root = new BorderPane();

        // the main content
        HBox mainContent = new HBox(10);
        visualBoard = new VisualBoard(board);
        currentCollection = new CurrentCollection(board);
        currentCollection.setPrefWidth(400);
        mainContent.getChildren().addAll(currentCollection, visualBoard);

        // Set the same margin for each child of the HBox
        Insets margin = new Insets(10);
        HBox.setMargin(currentCollection, margin);
        HBox.setMargin(visualBoard, margin);

        // the info banner
        infoBanner = new InfoBanner();

        // move the info banner a little to the right by putting it in a HBox
        HBox infoBannerBox = new HBox(infoBanner);
        infoBannerBox.setPadding(new Insets(0, 10, 0, 10));

        everything = new VBox(10);
        everything.getChildren().addAll(infoBannerBox, mainContent);

        // put padding between the content and the window
        root.setCenter(everything);

        setupToolbar(root);

        Scene scene = new Scene(root);

        // Set the scene in the stage and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bug Hunt - Game");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setupToolbar(BorderPane root) {
        // Create the menu bar
        MenuBar menuBar = new MenuBar();

        // Create the File menu
        Menu fileMenu = new Menu("File");

        // Create the Save and Exit buttons
        // MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");

        // Add action listeners to the Save and Exit buttons
        // saveItem.setOnAction(event -> saveGame());
        exitItem.setOnAction(event -> Platform.exit());

        // Add the Save and Exit buttons to the File menu
        fileMenu.getItems().addAll(exitItem);

        // Create the Help menu
        Menu helpMenu = new Menu("Help");

        // Create the Help button
        MenuItem helpItem = new MenuItem("Instructions");
        helpItem.setOnAction(event -> showHelpScene());

        // Add the Help button to the Help menu
        helpMenu.getItems().add(helpItem);

        // Add the File and Help menus to the menu bar
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        // Add the menu bar to the root
        root.setTop(menuBar);
    }

    private void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream("jvs.sav");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(board);
            oos.flush();
            oos.close();
            // Display a message to the user indicating that the game has been saved
            // This assumes that you have a method for displaying messages to the user
            // Replace this with your actual method
            System.out.println("Game saved successfully");

        } catch (Exception e) {
            // Display a message to the user indicating that the game could not be saved
            // This assumes that you have a method for displaying messages to the user
            // Replace this with your actual method
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    private void setupTurnMessage() {
        turnMessage = new Text();
        turnMessage.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 50));
        turnMessage.setFill(Color.BLACK);
        turnMessage.setVisible(true); // Make the text visible
        BorderPane.setAlignment(turnMessage, Pos.CENTER);
    }

    private void showHelpScene() {
        // Create a new Stage
        Stage helpStage = new Stage();
        // Create the root node
        BorderPane helpRoot = new BorderPane();
        // Create the Help text
        Label helpText = new Label("Help");
        helpText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 50));
        helpText.setTextFill(Color.BLACK);
        // Add the Help text to the root and center it
        helpRoot.setTop(helpText);
        BorderPane.setAlignment(helpText, Pos.CENTER);

        // Create the instructions text
        Label instructionsTitle = new Label("Instructions");
        instructionsTitle.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 30));
        Label instructionsText = new Label(""
                + "1. Each player takes turns moving the hedgehog\n"
                + "2. Move the hedgehog on the card you want to collect\n"
                + "3. The hedgehog can only move FORWARD, LEFT, or RIGHT\n"
                + "4. Passing a net will earn you a random bonus card \n"
                + "5. Each card has abilities (Check the cheat sheet below!)\n"
                + "6. The player with the highest score wins\n\n");
        instructionsText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 20));
        instructionsText.setTextFill(Color.BLACK);

        // place cheat sheet title
        Label cheatSheetTitle = new Label("Card Cheat Sheet");
        cheatSheetTitle.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 30));
        cheatSheetTitle.setTextFill(Color.BLACK);
        BorderPane.setAlignment(cheatSheetTitle, Pos.CENTER);

        // Load the cheat sheet image
//        Image cheatsheet = loadImage("/images/cheatsheet.png");
        Image cheatsheet = loadImage("/images/cheatsheet2.png");
        ImageView cheatsheetView = new ImageView(cheatsheet);
        cheatsheetView.setSmooth(true);
        cheatsheetView.setCache(true);
        cheatsheetView.setCacheHint(CacheHint.QUALITY);
        cheatsheetView.setPreserveRatio(true);
        cheatsheetView.setFitWidth(350);

        // Create a VBox to hold the instructions and cheat sheet
        VBox vbox = new VBox(10, instructionsTitle, instructionsText, cheatSheetTitle, cheatsheetView); // 10 is the
                                                                                                        // spacing
                                                                                                        // between
        // elements
        vbox.setPadding(new Insets(10)); // Add some padding around the VBox

        // Create a ScrollPane and add the VBox to it
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true); // This will make the VBox fill the width of the ScrollPane

        // Add the ScrollPane to the root
        helpRoot.setCenter(scrollPane);

        // Create a Scene and set it to the stage
        Scene scene = new Scene(helpRoot);
        // Set the size of the stage
        helpStage.setWidth(450);
        helpStage.setHeight(500);
        // set icon
//        Image icon = loadImage("/images/icon.jpg");
        Image icon = loadImage("/images/net.png");
        helpStage.getIcons().add(icon);
        // Set the scene in the stage and show the stage
        helpStage.setScene(scene);
        helpStage.setTitle("Help");
        helpStage.setResizable(false);
        helpStage.show();
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public void ending() {
        // Create a new BorderPane to hold the Game Over label and the FinalScores
        VBox endingPane = new VBox();
        endingPane.prefWidthProperty().bind(root.widthProperty());
        endingPane.prefHeightProperty().bind(root.heightProperty());

        // Fade out everything except the toolbar
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(2), everything);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Remove everything except the toolbar after the fade out transition
        fadeOutTransition.setOnFinished(event -> {
            root.getChildren().remove(everything);

            // Add the endingPane to the root
            root.setCenter(endingPane);
        });

        // Create a Label to display "Game Over"
        Label gameOverLabel = new Label("Game Over");
        Font funFont = Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 50);
        gameOverLabel.setFont(funFont);
        gameOverLabel.setTextFill(Color.RED);

        // Create an HBox to hold the FinalScores and align it to the left
        HBox gameOverBox = new HBox(gameOverLabel);
        gameOverBox.setAlignment(Pos.CENTER);
        endingPane.getChildren().add(gameOverBox);

        // put spacing on the bottom
        endingPane.setPadding(new Insets(0, 0, 50, 0));

        // Create a FadeTransition that fades the Label in over 2 seconds
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(2), gameOverLabel);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        // Add a callback to the FadeTransition that displays the final scores after the
        // "Game Over" label has faded in
        fadeInTransition.setOnFinished(event -> {
            // Create an instance of FinalScores
            FinalScores finalScores = new FinalScores(board);
            finalScores.setOpacity(0); // Start invisible for the fade-in transition

            // Create an HBox to hold the FinalScores and align it to the left
            HBox scoresBox = new HBox(finalScores);
            scoresBox.setAlignment(Pos.CENTER_LEFT);

            // Add the HBox to the endingPane
            endingPane.getChildren().add(scoresBox);

            // Create a FadeTransition that fades the FinalScores in over 2 seconds
            FadeTransition fadeInScoresTransition = new FadeTransition(Duration.seconds(2), finalScores);
            fadeInScoresTransition.setFromValue(0.0);
            fadeInScoresTransition.setToValue(1.0);
            fadeInScoresTransition.setOnFinished(e -> finalScores.popInCards());
            fadeInScoresTransition.play();
        });

        // create a sequence of animations
        SequentialTransition endingTransition = new SequentialTransition(fadeOutTransition, fadeInTransition);
        endingTransition.play();
    }

    public VisualBoard getVisualBoard() {
        return visualBoard;
    }

    public CurrentCollection getCollectionList() {
        return currentCollection;
    }

    public InfoBanner getInfoBanner() {
        return infoBanner;
    }
}