package com.Butterfly.view.GameScreen;

import com.Butterfly.model.board.Board;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.StackPane;
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
    private static VisualBoard visualBoard;
    private static CollectionList collectionList;
    // private static GameInfo gameInfo;
    private Text turnMessage;

    public GameScreen(final Stage primaryStage, final Board board, final boolean debugMode) {
        this.primaryStage = primaryStage;
        this.board = board;

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
            alert.setContentText("Any unsaved progress will be lost.");

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
        // Load the icon image
        Image iconImage = loadImage("/images/icon.jpg");

        // Set the icon for the stage
        primaryStage.getIcons().add(iconImage);
    }


    private void setupGameScene() {
        BorderPane root = new BorderPane();

        setupTurnMessage(root);
        setupToolbar(root);

        // the main content
        BorderPane content = new BorderPane();
        visualBoard = new VisualBoard(board);
        collectionList = new CollectionList(board);
        // gameInfo = new GameInfo(board);

        // gameInfo.setPrefWidth(200);
        collectionList.setPrefWidth(400);

        content.setCenter(visualBoard);
        // content.setRight(gameInfo);
        content.setLeft(collectionList);

        // Set the same margin for each child of the BorderPane
        Insets margin = new Insets(10);
        BorderPane.setMargin(visualBoard, margin);
        BorderPane.setMargin(collectionList, margin);
        // BorderPane.setMargin(gameInfo, margin);

        // put padding between the content and the window
        // BorderPane.setMargin(content, margin);
        root.setCenter(content); // put the main content in the center of the root

        Scene scene = new Scene(root);

        // Set the scene in the stage and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Butterfly Game");
        primaryStage.setResizable(false);
        primaryStage.show();

        // Set icon
        InputStream stream = getClass().getResourceAsStream("/images/icon.jpg");
        Image icon = new Image(stream);
        primaryStage.getIcons().add(icon);
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
        MenuItem helpItem = new MenuItem("Help");
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

    private void setupTurnMessage(BorderPane root) {
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
        BorderPane root = new BorderPane();
        // Create the Help text
        Label helpText = new Label("Help");
        helpText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 50));
        helpText.setTextFill(Color.BLACK);
        // Add the Help text to the root and center it
        root.setTop(helpText);
        BorderPane.setAlignment(helpText, Pos.CENTER);

        // Create the instructions text
        Label instructionsText = new Label("Instructions:\n\n"
                + "1. Each player takes turns moving the hedgehog\n"
                + "2. Move the hedgehog on the card you want to collect\n"
                + "3. The hedgehog can only move FORWARD, LEFT, or RIGHT\n"
                + "4. Each card has abilities (Check the cheat sheet below!) \n"
                + "5. The player with the highest score wins\n\n");
        instructionsText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 20));
        instructionsText.setTextFill(Color.BLACK);

        // place cheat sheet title
        Label cheatSheetTitle = new Label("Card Cheat Sheet");
        cheatSheetTitle.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 30));
        cheatSheetTitle.setTextFill(Color.BLACK);
        BorderPane.setAlignment(cheatSheetTitle, Pos.CENTER);

        // Load the cheat sheet image
        Image cheatsheet = loadImage("/images/cheatsheet.png");
        ImageView cheatsheetView = new ImageView(cheatsheet);
        cheatsheetView.preserveRatioProperty().set(true);
        cheatsheetView.setFitWidth(400);

        // Create a VBox to hold the instructions and cheat sheet
        VBox vbox = new VBox(10, instructionsText, cheatSheetTitle, cheatsheetView); // 10 is the spacing between
                                                                                     // elements
        vbox.setPadding(new Insets(10)); // Add some padding around the VBox

        // Create a ScrollPane and add the VBox to it
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true); // This will make the VBox fill the width of the ScrollPane

        // Add the ScrollPane to the root
        root.setCenter(scrollPane);

        // Create a Scene and set it to the stage
        Scene scene = new Scene(root);
        // Set the size of the stage
        helpStage.setWidth(450);
        helpStage.setHeight(450);
        // set icon
        InputStream stream = getClass().getResourceAsStream("/images/icon.jpg");
        Image icon = new Image(stream);
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

    // public void setupFinalScoresScene() {
    // // delete game scenes
    // primaryStage.hide();

    // BorderPane root = new BorderPane();

    // // the main content
    // FinalScores finalScores = new FinalScores(board);
    // root.setCenter(finalScores);

    // // set the scene
    // Scene scene = new Scene(root);
    // primaryStage.setScene(scene);
    // primaryStage.setTitle("Final Scores");
    // primaryStage.setResizable(true);
    // primaryStage.show();
    // }

    public void ending() {
        // Fade out the VisualBoard
        visualBoard.fadeOut();

        // Create a Label to display "Game Over"
        Label gameOverLabel = new Label("Game Over");
        Font funFont = Font.loadFont(getClass().getResourceAsStream("/fonts/storytime/Storytime.ttf"), 50);
        gameOverLabel.setFont(funFont);
        gameOverLabel.setTextFill(Color.RED);

        // Create a new StackPane to hold the Game Over label
        StackPane overlay = new StackPane(gameOverLabel);
        // Bind the size of the overlay StackPane to the size of the GameScreen
        overlay.prefWidthProperty().bind(visualBoard.widthProperty());
        overlay.prefHeightProperty().bind(visualBoard.heightProperty());

        // Add the overlay StackPane to the GameScreen
        visualBoard.getChildren().add(overlay);

        // Create a FadeTransition that fades the Label in over 2 seconds
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(2), gameOverLabel);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        // Start the fade in transition
        fadeInTransition.play();
    }

    public void displayPlayerTurn(String message) {
        turnMessage.setText(message);
        turnMessage.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            turnMessage.setVisible(false);
        });
        pause.play();
    }

    public VisualBoard getVisualBoard() {
        return visualBoard;
    }

    public CollectionList getCollectionList() {
        return collectionList;
    }


}