package com.Butterfly.controller;

import com.Butterfly.model.board.Board;
import com.Butterfly.model.cards.Card;
import com.Butterfly.model.players.Player;
import com.Butterfly.view.AudioManager;
import com.Butterfly.view.GameScreen.GameInfo;
import com.Butterfly.view.GameScreen.GameScreen;
import com.Butterfly.view.GameScreen.PlayerCollections;
import com.Butterfly.view.GameScreen.VisualBoard;
import com.Butterfly.view.IntroScreen.IntroScreen;
import com.Butterfly.view.TitleScreen.TitleScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class App extends Application {

    // settings
    public static final boolean DEBUG_MODE = true;
    public static final int MAX_PLAYERS = 5;

    // game data
    public static GameState gameState;
    private static int roundCount = 0;

    // GUI
    private TitleScreen titleScreen;
    private IntroScreen introScreen;
    private GameScreen gameScreen;

    // extra stuff
    private static Board board;
    private static AudioManager audio;
    private static Scanner input = new Scanner(System.in); // for testing

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
            titleScreen.setStartButtonListener(event -> showIntroScreen(primaryStage));
        }
    }

    private void showIntroScreen(Stage primaryStage) {
        audio.playSFX(audio.mouseClick, 0.5);
        primaryStage.close();

        Stage introStage = new Stage();
        introScreen = new IntroScreen(introStage);
        introScreen.setNextButtonListener(event -> showGameScreen(introStage, introScreen));
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
        int numberOfHumanPlayers = introScreen.getNumberOfHumanPlayers();
        int numberOfComPlayers = introScreen.getNumberOfComputerPlayers();
        ArrayList<String> playerNames = introScreen.getPlayerNames();
        // TODO read rest of data...

        // now that we have the data, we can create the board
        board = new Board(numberOfHumanPlayers, numberOfComPlayers, playerNames);
    }

    private void startGame(Stage gameStage) { // TODO change all text related methods to GUI ones
        // game structure
        // textIntro();
        // setup();
        gameLoop();
        // ending();
    }

    private void windowSetup(Stage primaryStage) {
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

    private static void gameLoop() {
        while (gameState != GameState.GAME_OVER) {
            // loop through players' turns
            for (Player player : board.getPlayers()) {

                playerTurn(player);
            }

            roundCount++;

            // end of loop, check for game over
            if (!board.canMove()) {
                gameState = GameState.GAME_OVER;
            }
        }
    }

    private static void playerTurn(Player player) {

        board.setCurrentPlayer(player);
        gameState = GameState.WAITING_FOR_PLAYER_MOVE;
        board.setReadyToMove(true);

        board.setMoveCallback(() -> {

            gameState = GameState.FINISHED_PLAYER_MOVE;

            audio.playSFX(audio.step1, 0.5);

            // take card from board
            Card card = board.takeCard(board.getHedgehog().getX(), board.getHedgehog().getY());

            // move card into player's collection
            player.collectCard(card);
            System.out.println(player.getName() + " collected a " + card.getFamily() + " card.");
            System.out.println();
//            Delay.pause(1).join(); // freezes screen, not sure why

            audio.playSFX(audio.collect, 0.8); // TODO play sfx based on card family (i.e. butterfly wing flapping,
                                               // grasshopper boing, etc.)
        });
    }

    private static void ending() {

        // erase visual board to make room for final scores

    }
}