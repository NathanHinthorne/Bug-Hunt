package com.Butterfly.model.board;

import com.Butterfly.model.cards.*;
import com.Butterfly.model.players.ComputerPlayer;
import com.Butterfly.model.players.HumanPlayer;
import com.Butterfly.model.players.Player;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Board {

    private final Card[][] grid;
    private int gridWidth;
    private int gridHeight;
    public static final double NET_CHANCE = 0.14;
    public static final Random RANDOM = new Random();
    private int numHighlightedCards = 0;
    List<Card> drawPile;

    private final Hedgehog hedgehog;

    private final List<Player> players;
    private Player currentPlayer;
    private boolean readyToMove = false;
    private final List<BoardObserver> observers = new ArrayList<>();

    public Board(int numberOfHumanPlayers, int numberOfComputerPlayers, ArrayList<String> playerNames) {

        players = new ArrayList<>();

        for (int i = 0; i < numberOfHumanPlayers; i++) {
            players.add(new HumanPlayer());
            players.get(i).setName(playerNames.get(i));
        }

        for (int i = 0; i < numberOfComputerPlayers; i++) {
            players.add(new ComputerPlayer());
            players.get(numberOfHumanPlayers + i).setName("Com " + (i + 1));
        }

        currentPlayer = players.get(0);

        // setup board
        grid = createGrid(players.size());
        drawPile = createDrawPile();
        shuffle();
        placeStartingCards();
        hedgehog = createHedgehog();
        highlightCards();
    }

    private Hedgehog createHedgehog() {
        int x = 1 + RANDOM.nextInt(gridWidth-2);
        int y = 1 + RANDOM.nextInt(gridHeight-2);
        return Hedgehog.create(x, y, GlobalDir.WEST);
    }

    private Card[][] createGrid(int numberOfPlayers) {

        if (numberOfPlayers == 2){
            gridHeight = 6;
            gridWidth = 6;
            return new Card[6][6];
        }
        else if (numberOfPlayers == 3) {
            gridHeight = 7;
            gridWidth = 7;
            return new Card[7][7];
        }
        else if (numberOfPlayers == 4) {
            gridHeight = 8;
            gridWidth = 8;
            return new Card[8][8];
        }
        else if (numberOfPlayers == 5) {
            gridHeight = 9;
            gridWidth = 9;
            return new Card[9][9];
        }

        else if (numberOfPlayers == 1) {
            gridHeight = 4;
            gridWidth = 4;
            return new Card[4][4]; // funny/error case
        }

        else throw new IllegalArgumentException("Number of players must be between 2 and 5");
    }

    private List<Card> createDrawPile() {
        List<Card> drawPile = new ArrayList<>();

        // Add butterflies
        for (int i = 0; i < 2; i++) {
            drawPile.add(CardFactory.createCard(CardFamily.RED_BUTTERFLY, CardType.ONE));
            drawPile.add(CardFactory.createCard(CardFamily.RED_BUTTERFLY, CardType.TWO));
            drawPile.add(CardFactory.createCard(CardFamily.RED_BUTTERFLY, CardType.THREE));
            drawPile.add(CardFactory.createCard(CardFamily.RED_BUTTERFLY, CardType.FOUR));
            drawPile.add(CardFactory.createCard(CardFamily.RED_BUTTERFLY, CardType.FIVE));

            drawPile.add(CardFactory.createCard(CardFamily.BLUE_BUTTERFLY, CardType.ONE));
            drawPile.add(CardFactory.createCard(CardFamily.BLUE_BUTTERFLY, CardType.TWO));
            drawPile.add(CardFactory.createCard(CardFamily.BLUE_BUTTERFLY, CardType.THREE));
            drawPile.add(CardFactory.createCard(CardFamily.BLUE_BUTTERFLY, CardType.FOUR));
            drawPile.add(CardFactory.createCard(CardFamily.BLUE_BUTTERFLY, CardType.FIVE));

            drawPile.add(CardFactory.createCard(CardFamily.YELLOW_BUTTERFLY, CardType.ONE));
            drawPile.add(CardFactory.createCard(CardFamily.YELLOW_BUTTERFLY, CardType.TWO));
            drawPile.add(CardFactory.createCard(CardFamily.YELLOW_BUTTERFLY, CardType.THREE));
            drawPile.add(CardFactory.createCard(CardFamily.YELLOW_BUTTERFLY, CardType.FOUR));
            drawPile.add(CardFactory.createCard(CardFamily.YELLOW_BUTTERFLY, CardType.FIVE));

            drawPile.add(CardFactory.createCard(CardFamily.GREEN_BUTTERFLY, CardType.ONE));
            drawPile.add(CardFactory.createCard(CardFamily.GREEN_BUTTERFLY, CardType.TWO));
            drawPile.add(CardFactory.createCard(CardFamily.GREEN_BUTTERFLY, CardType.THREE));
            drawPile.add(CardFactory.createCard(CardFamily.GREEN_BUTTERFLY, CardType.FOUR));
            drawPile.add(CardFactory.createCard(CardFamily.GREEN_BUTTERFLY, CardType.FIVE));
        }

        drawPile.add(CardFactory.createCard(CardFamily.RED_BUTTERFLY, CardType.DOUBLE));
        drawPile.add(CardFactory.createCard(CardFamily.BLUE_BUTTERFLY, CardType.DOUBLE));
        drawPile.add(CardFactory.createCard(CardFamily.YELLOW_BUTTERFLY, CardType.DOUBLE));
        drawPile.add(CardFactory.createCard(CardFamily.GREEN_BUTTERFLY, CardType.DOUBLE));

        // Add grasshoppers
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.ONE));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.TWO));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.THREE));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.FOUR));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.FIVE));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.SIX));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.SEVEN));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.EIGHT));
        drawPile.add(CardFactory.createCard(CardFamily.GRASSHOPPER, CardType.NINE));

        // Add fireflies
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.ONE));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.TWO));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.THREE));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.FOUR));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.FIVE));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.SIX));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.SEVEN));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.EIGHT));
        drawPile.add(CardFactory.createCard(CardFamily.FIREFLY, CardType.NINE));

        // Add dragonflies
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.ONE));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.TWO));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.THREE));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.FOUR));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.FIVE));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.SIX));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.SEVEN));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.EIGHT));
        drawPile.add(CardFactory.createCard(CardFamily.DRAGONFLY, CardType.NINE));

        // Add flowers
        for (int i = 0; i < 13; i++) {
            drawPile.add(CardFactory.createCard(CardFamily.FLOWER, CardType.SQUARE));
        }

        // Add honeycombs
        drawPile.add(CardFactory.createCard(CardFamily.HONEYCOMB, CardType.TEN));
        drawPile.add(CardFactory.createCard(CardFamily.HONEYCOMB, CardType.ELEVEN));
        drawPile.add(CardFactory.createCard(CardFamily.HONEYCOMB, CardType.TWELVE));
        drawPile.add(CardFactory.createCard(CardFamily.HONEYCOMB, CardType.THIRTEEN));
        drawPile.add(CardFactory.createCard(CardFamily.HONEYCOMB, CardType.FOURTEEN));
        drawPile.add(CardFactory.createCard(CardFamily.HONEYCOMB, CardType.FIFTEEN));

        // Add bees
        drawPile.add(CardFactory.createCard(CardFamily.BEE, CardType.NEGATIVE_THREE));
        drawPile.add(CardFactory.createCard(CardFamily.BEE, CardType.NEGATIVE_THREE));
        drawPile.add(CardFactory.createCard(CardFamily.BEE, CardType.NEGATIVE_THREE));
        drawPile.add(CardFactory.createCard(CardFamily.BEE, CardType.NEGATIVE_THREE));
        drawPile.add(CardFactory.createCard(CardFamily.BEE, CardType.NEGATIVE_THREE));
        drawPile.add(CardFactory.createCard(CardFamily.BEE, CardType.NEGATIVE_THREE));

        // Add wasps
        drawPile.add(CardFactory.createCard(CardFamily.WASP, CardType.NEGATIVE_FOUR));
        drawPile.add(CardFactory.createCard(CardFamily.WASP, CardType.NEGATIVE_FIVE));
        drawPile.add(CardFactory.createCard(CardFamily.WASP, CardType.NEGATIVE_SIX));
        drawPile.add(CardFactory.createCard(CardFamily.WASP, CardType.NEGATIVE_SEVEN));

        return drawPile;
    }

    private void shuffle() {
        Collections.shuffle(drawPile);
    }

    private void placeStartingCards() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {

                Card card = drawPile.remove(0); //? need to randomize drawPile first?
                placeCard(card, x, y);
//                System.out.println("Placing card " + card.toString() + " at (" + x + ", " + y  + ")");

                if (RANDOM.nextDouble() < NET_CHANCE) { // chance of adding net to square
                    getCard(x, y).placeNet();
                }
            }
        }
    }

    public void move(GlobalDir dir, int spaces) {
        if (dir == GlobalDir.NORTH && hedgehog.getDir()
                == GlobalDir.SOUTH ||
                dir == GlobalDir.EAST && hedgehog.getDir()
                        == GlobalDir.WEST ||
                dir == GlobalDir.WEST && hedgehog.getDir()
                        == GlobalDir.EAST ||
                dir == GlobalDir.SOUTH && hedgehog.getDir()
                        == GlobalDir.NORTH) {

            System.out.println("You can't move the hedgehog backwards!\n");
            return;
        }
        hedgehog.setDir(dir);

        if (dir == GlobalDir.NORTH) {
//            if (hedgehog.getY() - spaces < 0) {
//                move(dir, spaces - 1);
//                return;
//            }
            hedgehog.setLocation(hedgehog.getX(), hedgehog.getY() - spaces);
            System.out.println("Moving hedgehog " + spaces + " spaces north");

        } else if (dir == GlobalDir.EAST) {
//            if (hedgehog.getX() + spaces > gridWidth - 1) {
//                move(dir, spaces - 1);
//                return;
//            }
            hedgehog.setLocation(hedgehog.getX() + spaces, hedgehog.getY());
            System.out.println("Moving hedgehog " + spaces + " spaces east");

        } else if (dir == GlobalDir.WEST) {
//            if (hedgehog.getX() - spaces < 0) {
//                move(dir, spaces - 1);
//                return;
//            }
            hedgehog.setLocation(hedgehog.getX() - spaces, hedgehog.getY());
            System.out.println("Moving hedgehog " + spaces + " spaces west");

        } else if (dir == GlobalDir.SOUTH) {
//            if (hedgehog.getY() + spaces > gridHeight - 1) {
//                move(dir, spaces - 1);
//                return;
//            }
            hedgehog.setLocation(hedgehog.getX(), hedgehog.getY() + spaces);
            System.out.println("Moving hedgehog " + spaces + " spaces south");
        }

        highlightCards();
    }

    public void move(int x, int y) {
        hedgehog.setMarker();
        hedgehog.setLocation(x, y);
        System.out.println("Moving hedgehog to (" + x + ", " + y + ")");

        hedgehog.updateDir();
        System.out.println("Facing " + hedgehog.getDir().toString());

        System.out.println("On top of " + getCard(x, y).getFamily() + " " + getCard(x, y).getType() + "\n");

        highlightCards();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {

                if (hedgehog.getX() == x && hedgehog.getY() == y) {
                    if (hedgehog.getDir()
                            == GlobalDir.NORTH)
                        sb.append("█↑  ");
                    else if (hedgehog.getDir()
                            == GlobalDir.EAST)
                        sb.append("█→  ");
                    else if (hedgehog.getDir()
                            == GlobalDir.SOUTH)
                        sb.append("█↓  ");
                    else if (hedgehog.getDir()
                            == GlobalDir.WEST)
                        sb.append("█←  ");
                } else {
                    sb.append(getCard(x, y).toString() + "  ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getNumPlayers() {
        return players.size();
    }

    public int drawPileCount() {
        return drawPile.size();
    }

    public void highlightCards() {

        unhighlightAllCards();
        numHighlightedCards = 0;

        switch(hedgehog.getDir()) {
            case NORTH:
                scanNorth();
                break;
            case EAST:
                scanEast();
                break;
            case WEST:
                scanWest();
                break;
            case SOUTH:
                scanSouth();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + hedgehog.getDir());
        }
    }

    private void unhighlightAllCards() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++)
                getCard(x, y).setHighlighted(false);
        }
    }

    // approach #2 - bad performance??
//    private void scanNorth2() {
//        int hedgehogX = hedgehog.getX();
//        int hedgehogY = hedgehog.getY();
//
//        for (int y = 0; y < gridHeight; y++) {
//            for (int x = 0; x < gridWidth; x++) {
//
//                Card card = getCard(x, y);
//                if ((hedgehogX == x || hedgehogY == y) && y < hedgehogY && !card.isEmpty()) {
//                    card.setHighlighted(true);
//
//                } else {
//                    card.setHighlighted(false);
//                }
//            }
//        }
//    }
    private void scanNorth() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = y-1; scanDist >= 0; scanDist--) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
        for (int scanDist = 0; scanDist < gridWidth; scanDist++) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
    }

    private void scanEast() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = x+1; scanDist < gridWidth; scanDist++) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
        for (int scanDist = 0; scanDist < gridHeight; scanDist++) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
    }

    private void scanWest() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = x-1; scanDist >= 0; scanDist--) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
        for (int scanDist = 0; scanDist < gridHeight; scanDist++) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
    }

    private void scanSouth() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = y+1; scanDist < gridHeight; scanDist++) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
        for (int scanDist = 0; scanDist < gridWidth; scanDist++) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                numHighlightedCards++;
            }
        }
    }


    public boolean hasNet(int x, int y) {
        return getCard(x, y).hasNet();
    }

    public boolean hasHedgehog(int x, int y) {
        return hedgehog.getX() == x && hedgehog.getY() == y;
    }

    public void placeCard(Card card, int x, int y) {
        grid[x][y] = card;
    }


    public Card takeCard(int x, int y) {
        Card temp = grid[x][y];
        grid[x][y] = CardFactory.createCard(); // leave an empty card

        return temp;
    }

    public Card getCard(int x, int y) {
        return grid[x][y];
    }

    public int getWidth() {
        return gridWidth;
    }

    public int getHeight() {
        return gridHeight;
    }

    public Hedgehog getHedgehog() {
        return hedgehog; // preferably return a copy
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }


    /**
     * Registers an observer to be notified when the board state changes
     * @param observer the observer to register
     */
    public void addObserver(BoardObserver observer) {
        observers.add(observer);
    }

    /**
     * Sets the state of the board and notifies observers
     * @param ready true if the hedgehog is ready to move, false otherwise
     */
    public void setReadyToMove(boolean ready) {
        readyToMove = ready;
        notifyObservers(ready);
    }

    /**
     * Notifies observers that the board state has changed
     * @param readyToMove true if the hedgehog is ready to move, false otherwise
     */
    private void notifyObservers(boolean readyToMove) {
        for (BoardObserver observer : observers) {
            observer.onBoardStateChanged(readyToMove);
        }
    }

    public boolean canMove() {
        return numHighlightedCards == 0;
    }
}