package com.Butterfly.view.GameScreen;

import com.Butterfly.model.board.Board;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;

public class GameScreen {
    private final Stage primaryStage;
    private final Board board;

    public GameScreen(final Stage primaryStage, final Board board, final boolean debugMode) {
        this.primaryStage = primaryStage;
        this.board = board;

        setupIcon();
        setupScene();

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

    private void setupScene() {
        BorderPane root = new BorderPane();

        // the main content
        BorderPane content = new BorderPane();
        GridPane visualBoard = new VisualBoard(board);
        Pane playerCollections = new PlayerCollections(board);
        Pane gameInfo = new GameInfo(board);

        playerCollections.setPrefWidth(350);
        gameInfo.setPrefWidth(200);

        BorderPane.setMargin(playerCollections, new Insets(0, 10, 0, 0)); // 10 pixels padding on the right
        BorderPane.setMargin(gameInfo, new Insets(0, 0, 0, 10)); // 10 pixels padding on the left

        content.setCenter(visualBoard);
        content.setRight(gameInfo);
        content.setLeft(playerCollections);


        // put padding between the content and the window
        BorderPane.setMargin(content, new Insets(10));
        root.setCenter(content); // put the main content in the center of the root

        // Create a Scene and set it to the stage
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


    private Image loadImage(String path) {
        return new Image(getClass().getResource(path).toExternalForm());
    }
}