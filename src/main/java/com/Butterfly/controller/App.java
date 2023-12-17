package com.Butterfly.controller;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.cards.Card;
import com.Butterfly.model.players.Player;
import com.Butterfly.view.AudioManager;
import com.Butterfly.view.GameScreen.GameInfo;
import com.Butterfly.view.GameScreen.GameScreen;
import com.Butterfly.view.GameScreen.CollectionList;
import com.Butterfly.view.GameScreen.VisualBoard;
import com.Butterfly.view.IntroScreen.IntroScreen;
import com.Butterfly.view.TitleScreen.TitleScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class App extends Application implements java.io.Serializable {

    // settings
    public static final boolean DEBUG_MODE = true;

    // game data
    public static GameState gameState;
    private static int roundCount = 0;

    // GUI
    private static TitleScreen titleScreen;
    private static IntroScreen introScreen;
    private static GameScreen gameScreen;

    // extra stuff
    private static Board board;
    private static AudioManager audio;
    private static Scanner input = new Scanner(System.in); // for testing
    private int numberOfHumanPlayers;
    private int numberOfComPlayers;
    private ArrayList<String> playerNames;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        audio = AudioManager.getInstance();

        if (DEBUG_MODE) {
            showIntroScreen(primaryStage);
        } else {
            // Create the title screen and set its properties
            titleScreen = new TitleScreen(primaryStage);
            // titleScreen.setStartButtonListener(event -> checkForSavedGame(primaryStage));
            titleScreen.setStartButtonListener(event -> {
                audio.playSFX(audio.mouseClick, 0.5);
                showIntroScreen(primaryStage);
            });
        }
    }

    private void checkForSavedGame(Stage primaryStage) {
        // Check for saved games
        File saveFile = new File("jvs.sav");

        if (saveFile.exists()) {
            // Ask the user if they want to load the saved game
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Load Game");
            alert.setHeaderText("A saved game was found.");
            alert.setContentText("Do you want to load the saved game?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // Load the saved game
                loadGame();
                // Transition to the game screen
                showGameScreen(primaryStage, introScreen);
            } else {
                // Start a new game
                showIntroScreen(primaryStage);
            }
        } else {
            // Start a new game
            showIntroScreen(primaryStage);
        }
    }

    private void showIntroScreen(Stage primaryStage) {
        primaryStage.close();

        Stage introStage = new Stage();
        introScreen = new IntroScreen(introStage, DEBUG_MODE);
        introScreen.setNextButtonListener(event -> {
            audio.playSFX(audio.mouseClick, 0.5);
            showGameScreen(introStage, introScreen);
        });
        // check if enter key is pressed
        // introScreen.requestFocus();
        // introScreen.onKeyPressedProperty().set(event -> {
        // if (event.getCode() == KeyCode.ENTER) {
        // showIntroScreen(primaryStage);
        // }
        // });
    }

    private void showGameScreen(Stage introStage, IntroScreen introScreen) {
        introStage.close();

        readGameData(introScreen);

        Stage gameStage = new Stage();
        gameScreen = new GameScreen(gameStage, board, DEBUG_MODE);

        startGame(gameStage);
    }

    private void readGameData(IntroScreen introScreen) {
        // read game data the user entered
        numberOfHumanPlayers = introScreen.getNumberOfHumanPlayers();
        numberOfComPlayers = introScreen.getNumberOfComputerPlayers();
        playerNames = introScreen.getPlayerNames();
        // TODO read rest of data...

        // now that we have the data, we can create the board
        board = new Board(numberOfHumanPlayers, numberOfComPlayers, playerNames);
    }

    private void startGame(Stage gameStage) {
        startNextPlayerTurn();
    }

    private void windowSetup(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // the main content
        BorderPane content = new BorderPane();
        GridPane visualBoard = new VisualBoard(board);
        Pane playerCollections = new CollectionList(board);
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

    // private static void gameLoop() {
    // while (gameState != GameState.GAME_OVER) {

    // // loop through players' turns
    // for (Player currentPlayer : board.getPlayers()) {

    // playerTurn(currentPlayer);
    // }
    // }

    // roundCount++;
    // System.out.println("Round " + roundCount + " is over!");
    // }

    private static void startNextPlayerTurn() {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null) {
            playerTurn(currentPlayer);

            if (gameState == GameState.GAME_OVER) {
                ending();
            }

        } else {
            // All players have had their turn, so the round is over
            roundCount++;
            System.out.println("\n-- Round " + roundCount + " is over! --\n");
            board.newRound();
            startNextPlayerTurn();
        }
    }

    private static void playerTurn(Player player) {

        gameScreen.getCollectionList().getFinishTurnButton().setVisible(false);
        board.setCurrentPlayer(player);
        gameState = GameState.WAITING_FOR_PLAYER_MOVE;
        System.out.println("Waiting for player move...");

        gameScreen.displayPlayerTurn(player.getName() + "'s turn!");
        System.out.println(player.getName() + "'s turn!");

        board.highlightCards();
        if (board.cannotMove()) { // board.setReadyToMove(true) updates the board state, so we need to check again
            System.out.println("No more moves left!");
            gameState = GameState.GAME_OVER;
            return;
        }
        board.setReadyToMove(true);

        board.setMoveCallback(() -> {

            gameScreen.getVisualBoard().update();

            audio.playSFX(audio.step1, 0.5);

            // take card from board
            Card card = board.takeCard(board.getHedgehog().getX(), board.getHedgehog().getY());

            // move card into player's collection
            player.collectCard(card);
            System.out.println("\nCollected a " + card.getFamily() + " card.");
            System.out.println();
            // Delay.pause(1).join(); // freezes screen, not sure why

            audio.playSFX(audio.collect, 0.8); // TODO play sfx based on card family (i.e. butterfly wing flapping,
            // grasshopper boing, etc.)

            gameScreen.getCollectionList().update();

            if (board.playerPassedOverNet()) {
                System.out.println("Passed over a net!");
                Card drawnCard = board.drawCard();
                player.collectCard(drawnCard);
                System.out.println("You pulled a " + drawnCard.getFamily() + " card from the net.\n");
                audio.playSFX(audio.collect, 0.8);
                gameScreen.getCollectionList().update();
            }

            gameScreen.getVisualBoard().update();

            gameScreen.getCollectionList().getFinishTurnButton().setVisible(true);

            checkForKeyPress(player); // not working? key presses aren't detected after button becomes visible

            // wait for player to click finish turn button
            gameScreen.getCollectionList().setFinishTurnButtonListener(event -> {
                audio.playSFX(audio.mouseClick, 0.5);
                gameScreen.getCollectionList().nextPlayerCollection();
                board.nextPlayer();
                startNextPlayerTurn();
                gameState = GameState.FINISHED_PLAYER_MOVE;
                System.out.println("Player move finished!");
            });
        });

        checkForKeyPress(player);
    }

    private static void checkForKeyPress(Player currentPlayer) {
        gameScreen.getVisualBoard().requestFocus();
        board.setKeyCallback((key) -> {
            System.out.println("Key pressed: " + key.toString());
            handleKeyPress(key);
            playerTurn(currentPlayer);
        });
    }

    private static void handleKeyPress(KeyCode key) {
        switch (key) {
            case ESCAPE:
                ending();
        }
    }

    private static void ending() {

        System.out.println("\n████ GAME HAS ENDED ████ \n");

        // erase visual board to make room for final scores
        gameScreen.ending();

    }

    private void loadGame() {
        try {
            FileInputStream fis = new FileInputStream("jvs.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            board = (Board) ois.readObject();
            ois.close();
            // Display a message to the user indicating that the game has been loaded
            // This assumes that you have a method for displaying messages to the user
            // Replace this with your actual method
            System.out.println("Game loaded successfully");
        } catch (Exception e) {
            // Display a message to the user indicating that the game could not be loaded
            // This assumes that you have a method for displaying messages to the user
            // Replace this with your actual method
            System.out.println("Failed to load game: " + e.getMessage());
        }
    }
}