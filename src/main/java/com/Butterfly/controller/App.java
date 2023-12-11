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
    private static boolean gameRunning = true;
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
        //TODO read rest of data...

        // now that we have the data, we can create the board
        board = new Board(numberOfHumanPlayers, numberOfComPlayers, playerNames);
    }

    private void startGame(Stage gameStage) { //TODO change all text related methods to GUI ones
        // game structure
//        textIntro();
//        setup();
        gameLoop();
//        ending();
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


    private static void ending() {
        System.out.println("" +
                " .d8888b.                                                                           888 \n" +
                "d88P  Y88b                                                                          888 \n" +
                "888    888                                                                          888 \n" +
                "888         8888b.  88888b.d88b.   .d88b.         .d88b.  888  888  .d88b.  888d888 888 \n" +
                "888  88888     \"88b 888 \"888 \"88b d8P  Y8b       d88\"\"88b 888  888 d8P  Y8b 888P\"   888 \n" +
                "888    888 .d888888 888  888  888 88888888       888  888 Y88  88P 88888888 888     Y8P \n" +
                "Y88b  d88P 888  888 888  888  888 Y8b.           Y88..88P  Y8bd8P  Y8b.     888      \"  \n" +
                " \"Y8888P88 \"Y888888 888  888  888  \"Y8888         \"Y88P\"    Y88P    \"Y8888  888     888 \n\n");

        Delay.pause(1.5).join();

        // final scores

        int playerCount = 1;
        for (Player player : board.getPlayers()) {

            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
            System.out.println("Player " + playerCount + " - \"" + player.getName() + "\"\n");
            player.printTextCollection();
            System.out.println("Total Points: " + player.getScore());
            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n");


            playerCount++;
        }
    }

    private static void gameLoop() {
        while (gameRunning) {
            // loop through players' turns
            for (Player player : board.getPlayers()) {

                playerTurn(player);
            }


            roundCount++;

            // end of loop, check for game over
            if (!board.canMove()) {
                gameRunning = false;
            }
        }
    }

    private static void playerTurn(Player player)  {

        board.setCurrentPlayer(player);
        board.setReadyToMove(true);
        audio.playSFX(audio.step1, 0.5);

        // collect card
        Card card = board.takeCard(board.getHedgehog().getX(), board.getHedgehog().getY());

        System.out.println();
        if (player.isHuman()) {
            if (card.isEmpty()) {
                System.out.println("\nThat square is empty.");
                System.out.println("Press try again.\n");
                playerTurn(player);
                return;
            }

            player.collectCard(card);
            System.out.println(player.getName() + " collected a " + card.getFamily() + " card.");
            System.out.println();
            Delay.pause(1000).join();
            System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
            System.out.println();
        }

        // play sfx
    }

//    private static void setup() {
//        int numHumanPlayers = findNumHumanPlayers();
//        int numComPlayers = findNumComPlayers(numHumanPlayers);
////        board = new Board(numHumanPlayers, numComPlayers, );
//
//        // assign player names
//        for (int i = 0; i < numHumanPlayers; i++) {
//            System.out.print("Enter a name for player " + (i + 1) + ": ");
//            String name = input.next();
//            board.getPlayers().get(i).setName(name);
//        }
//        System.out.println();
//
//        // assign com player names
//        int comCount = 1;
//        for (int i = numHumanPlayers; i < numHumanPlayers + numComPlayers; i++) {
//            board.getPlayers().get(i).setName("Com " + comCount);
//            comCount++;
//        }
//
//        // print player names
//        int playerCount = 1;
//        for (Player player : board.getPlayers()) {
//            System.out.println("Player " + playerCount + ": " + player.getName());
//            playerCount++;
//        }
//
////        Delay.pause(1.5).join();
//
//        System.out.println("\n" +
//                "     .d8888b.  888                     888    \n" +
//                "    d88P  Y88b 888                     888    \n" +
//                "    Y88b.      888                     888    \n" +
//                "     \"Y888b.   888888  8888b.  888d888 888888 \n" +
//                "        \"Y88b. 888        \"88b 888P\"   888    \n" +
//                "          \"888 888    .d888888 888     888    \n" +
//                "    Y88b  d88P Y88b.  888  888 888     Y88b.  \n" +
//                "     \"Y8888P\"   \"Y888 \"Y888888 888      \"Y888 \n\n");
//
////        Delay.pause(1.5).join();
//    }

//    private static int findNumHumanPlayers() {
//        System.out.println("How many human players are there? (1-5)");
//        int numPlayers = input.nextInt();
//        while (numPlayers < 1 || numPlayers > 5) {
//            System.out.println("Please enter a number between 1 and 5.");
//            numPlayers = input.nextInt();
//        }
//        return numPlayers;
//    }

//    private static int findNumComPlayers(int numHumanPlayers) {
//        if (numHumanPlayers == MAX_PLAYERS) {
//            return 0;
//        }
//
//        int maxComPlayers = MAX_PLAYERS - numHumanPlayers;
//
//        System.out.println("How many computer players are there?" + " (0-" + maxComPlayers + ")");
//        int numPlayers = input.nextInt();
//        while (numPlayers < 0 || numPlayers > maxComPlayers) {
//            System.out.println("Please enter a number between 0 and " + maxComPlayers + ".");
//            numPlayers = input.nextInt();
//        }
//        return numPlayers;
//    }

}