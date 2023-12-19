package com.Butterfly.controller;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.cards.Card;
import com.Butterfly.model.cards.CardFamily;
import com.Butterfly.model.players.Player;
import com.Butterfly.view.AudioManager;
import com.Butterfly.view.GameScreen.GameScreen;
import com.Butterfly.view.IntroScreen.IntroScreen;
import com.Butterfly.view.TitleScreen.TitleScreen;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class App extends Application implements java.io.Serializable {

    // settings
    public static final boolean DEBUG_MODE = false;

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
                audio.playSFX(audio.fancyClick, 0.3);
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
            audio.playSFX(audio.fancyClick, 0.3);
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
        board.unhighlightAllCards();
        gameScreen.getVisualBoard().update();
        gameScreen.getCollectionList().getFinishTurnButton().setVisible(false);
        audio.playMusic(audio.gameMusic2, true, 0.8);

        if (DEBUG_MODE) {
            startNextPlayerTurn();

        } else {
            gameScreen.getInfoBanner().displayInfo("Welcome to the game of BUTTERFLY!", Color.CORNFLOWERBLUE, 4);

            PauseTransition pause = new PauseTransition(Duration.seconds(4));
            pause.setOnFinished(event -> {
                gameScreen.getInfoBanner().displayInfo("Check out the instructions located in \"Help\" to get started",
                        Color.CORNFLOWERBLUE, 6);

                PauseTransition pause2 = new PauseTransition(Duration.seconds(6));
                pause2.setOnFinished(event2 -> {
                    audio.playSFX(audio.win, 0.9);
                    startNextPlayerTurn();
                });
                pause2.play();
            });
            pause.play();
        }
    }

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
        gameState = GameState.PLAYER_TURN;

        gameScreen.getCollectionList().getFinishTurnButton().setVisible(false);
        board.setCurrentPlayer(player);
        System.out.println("Waiting for player move...");

        gameScreen.getInfoBanner().displayInfo(player.getName() + "'s turn!", Color.BLACK, 2);
        System.out.println(player.getName() + "'s turn!");

        board.highlightCards();
        if (board.cannotMove()) { // board.setReadyToMove(true) updates the board state, so we need to check again
            System.out.println("No more moves left!");
            gameState = GameState.GAME_OVER;
            return;
        }

        if (player.isHuman()) {
            humanTurnProcess(player);
        } else {
            computerTurnProcess(player);
        }
    }

    private static void humanTurnProcess(Player player) {
        gameState = GameState.PROCESSING_MOVE;
        board.setReadyToMove(true);

        board.setMoveCallback(() -> {
            gameState = GameState.GAME_UPDATING;

            board.unhighlightAllCards();
            gameScreen.getVisualBoard().update();

            audio.playSFX(audio.step1, 0.5);

            takeCardFromBoard(player);
            checkForNet(player);

            gameScreen.getCollectionList().getFinishTurnButton().setVisible(true);

            checkForKeyPress(player); // not working? key presses aren't detected after button becomes visible

            // wait for player to click finish turn button
            gameScreen.getCollectionList().setFinishTurnButtonListener(event -> {
                audio.playSFX(audio.fancyClick, 0.3);
                endTurn();
            });
        });

        checkForKeyPress(player);
    }

    private static void computerTurnProcess(Player player) {
        gameState = GameState.PROCESSING_MOVE;
        gameScreen.getVisualBoard().computerAnimations();

        // If the player is a computer player, automatically move to a card
        Card cardToMoveTo = player.cardToMoveTo(); // computer player chooses a card to move to (randomly or highest
                                                   // value?)
        board.move(cardToMoveTo.getCoordinates().getX(), cardToMoveTo.getCoordinates().getY());

        // wait two seconds, display "thinking", wait two more seconds, then move
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            gameScreen.getInfoBanner().displayInfo(player.getName() + " is thinking...", Color.CORNFLOWERBLUE, 4);
            PauseTransition pause2 = new PauseTransition(Duration.seconds(2));
            pause2.setOnFinished(event2 -> {
                gameState = GameState.GAME_UPDATING;
                audio.playSFX(audio.step1, 0.5);
                board.unhighlightAllCards();
                gameScreen.getVisualBoard().update();
                takeCardFromBoard(player);
                checkForNet(player);
                PauseTransition pause3 = new PauseTransition(Duration.seconds(2));
                pause3.setOnFinished(event3 -> {
                    endTurn();
                });
                pause3.play();
            });
            pause2.play();
        });
        pause.play();
    }

    private static void takeCardFromBoard(Player player) {
        // take card from board
        Card card = board.takeCard(board.getHedgehog().getX(), board.getHedgehog().getY());
        player.collectCard(card);
        audio.playSFX(audio.collect, 0.8);
        // playBugSound(card.getFamily());

        gameScreen.getCollectionList().update();
        gameScreen.getVisualBoard().update();
    }

    private static void checkForNet(Player player) {
        if (board.playerPassedOverNet()) {
            System.out.println("Passed over a net!");
            Card drawnCard = board.drawCard();
            board.getCurrentPlayer().collectCard(drawnCard);
            System.out.println(player.getName() + " pulled a " + drawnCard.getFamily() + " card from the net.\n");
            gameScreen.getInfoBanner().displayInfo("You pulled a " + drawnCard.getFamily() + " card from the net!",
                    Color.GREEN, 3);
            audio.playSFX(audio.collect, 0.8);
            gameScreen.getCollectionList().update();
        }
    }

    private static void endTurn() {
        gameScreen.getCollectionList().nextPlayerCollection();
        board.nextPlayer();
        startNextPlayerTurn();
        System.out.println("Player move finished!");
    }

    private static void playBugSound(CardFamily family) {
        if (family == CardFamily.RED_BUTTERFLY || family == CardFamily.BLUE_BUTTERFLY
                || family == CardFamily.YELLOW_BUTTERFLY || family == CardFamily.GREEN_BUTTERFLY) {
            audio.playSFX(audio.butterfly, 0.9);
        } else if (family == CardFamily.DRAGONFLY) {
            audio.playSFX(audio.dragonfly, 0.9);
        } else if (family == CardFamily.FIREFLY) {
            audio.playSFX(audio.firefly, 0.9);
        } else if (family == CardFamily.GRASSHOPPER) {
            audio.playSFX(audio.grasshopper, 0.9);
        }
        // else if (family == CardFamily.BEE) {
        // audio.playSFX(audio.bee, 0.5);
        // } else if (family == CardFamily.WASP) {
        // audio.playSFX(audio.wasp, 0.5);
        // } else if (family == CardFamily.HONEYCOMB) {
        // audio.playSFX(audio.honeycomb, 0.5);
        // } else if (family == CardFamily.FLOWER) {
        // audio.playSFX(audio.flower, 0.5);
        // }

        else {
            System.out.println("Error: invalid card family");
        }
    }

    private static void checkForKeyPress(Player currentPlayer) {
        board.setKeyCallback((key) -> {
            System.out.println("Key pressed: " + key.toString());
            audio.playSFX(audio.blip, 0.5);
            handleKeyPress(key);
            playerTurn(currentPlayer);
        });
    }

    private static void handleKeyPress(KeyCode key) {
        switch (key) {
            case ESCAPE:
                ending();
                break;
            case R:
                gameScreen.getVisualBoard().leaveAllEmptyCards();
                gameScreen.getVisualBoard().update();
                break;
        }
    }

    private static void ending() {

        audio.stopMusic();
        audio.playSFX(audio.win, 0.8);

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