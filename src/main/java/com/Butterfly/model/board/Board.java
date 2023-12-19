package com.Butterfly.model.board;

import com.Butterfly.controller.App;
import com.Butterfly.controller.GameState;
import com.Butterfly.model.cards.*;
import com.Butterfly.model.players.ComputerPlayer;
import com.Butterfly.model.players.HumanPlayer;
import com.Butterfly.model.players.Player;

import javafx.scene.input.KeyCode;

import java.util.*;

public class Board implements java.io.Serializable {

    private final Card[][] grid;
    private int gridWidth;
    private int gridHeight;
    public static final double NET_CHANCE = 0.14;
    public static final Random RANDOM = new Random();
    private int numHighlightedCards = 0;
    private ArrayList<Card> highlightedCards;
    List<Card> drawPile;

    private final Hedgehog hedgehog;

    private final ArrayList<Player> players;
    private Player currentPlayer;
    private boolean readyToMove = false;
    private boolean passedOverNet = false;
    private transient final List<BoardObserver> observers = new ArrayList<>();
    private transient MoveCallback moveCallback;
    private transient KeyPressCallback keyPressCallback;
    private int numCardsInPlay;

    public Board(int numberOfHumanPlayers, int numberOfComputerPlayers, ArrayList<String> playerNames) {

        players = new ArrayList<>();
        highlightedCards = new ArrayList<>();

        for (int i = 0; i < numberOfHumanPlayers; i++) {
            players.add(new HumanPlayer());
            players.get(i).setName(playerNames.get(i));
        }

        for (int i = 0; i < numberOfComputerPlayers; i++) {
            players.add(new ComputerPlayer(this)); // is it acceptable to add 2-way dependencies like this?
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
        int x = 1 + RANDOM.nextInt(gridWidth - 2);
        int y = 1 + RANDOM.nextInt(gridHeight - 2);
        return Hedgehog.create(x, y, GlobalDir.WEST);
    }

    private Card[][] createGrid(int numberOfPlayers) {

        if (numberOfPlayers == 2) {
            gridHeight = 6;
            gridWidth = 6;
            return new Card[6][6];
        } else if (numberOfPlayers == 3) {
            gridHeight = 7;
            gridWidth = 7;
            return new Card[7][7];
        } else if (numberOfPlayers == 4) {
            gridHeight = 8;
            gridWidth = 8;
            return new Card[8][8];
        } else if (numberOfPlayers == 5) {
            gridHeight = 9;
            gridWidth = 9;
            return new Card[9][9];
        }

        else if (numberOfPlayers == 1) {
            gridHeight = 4;
            gridWidth = 4;
            return new Card[4][4]; // funny/error case
        }

        else
            throw new IllegalArgumentException("Number of players must be between 2 and 5");
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
        // for (int y = 0; y < gridHeight; y++) {
        // for (int x = 0; x < gridWidth; x++) {
        //
        // Card card = drawPile.remove(0);
        // placeCard(card, x, y);
        // // System.out.println("Placing card " + card.toString() + " at (" + x + ", "
        // + y
        // // + ")");
        //
        // if (RANDOM.nextDouble() < NET_CHANCE) { // chance of adding net to square
        // getCard(x, y).placeNet();
        // }
        // }
        // }

        // Calculate the number of nets to be placed
        int numNets = (players.size() * 2) + 2;
        int totalCells = gridWidth * gridHeight;

        // Create an array of booleans representing whether each cell has a net
        boolean[] nets = new boolean[totalCells];
        Arrays.fill(nets, false);

        // Place the nets while ensuring they are spaced out
        for (int i = 0; i < numNets; i++) {
            int x, y;
            do {
                x = RANDOM.nextInt(gridWidth);
                y = RANDOM.nextInt(gridHeight);
            } while (nets[y * gridWidth + x] || !isSafeToPlaceNet(nets, x, y));

            nets[y * gridWidth + x] = true;
        }

        // Place the cards and nets on the grid
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                Card card = drawPile.remove(0);
                placeCard(card, x, y);

                if (nets[y * gridWidth + x]) {
                    getCard(x, y).placeNet();
                }
            }
        }
    }

    private boolean isSafeToPlaceNet(boolean[] nets, int x, int y) {
        // Return false if the cell is a corner cell
        if ((x == 0 || x == gridWidth - 1) && (y == 0 || y == gridHeight - 1)) {
            return false;
        }

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = x + dx, ny = y + dy;
                if (nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight && nets[ny * gridWidth + nx]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void move(int x, int y) {
        if (cannotMove()) {
            throw new IllegalStateException("Hedgehog cannot move but you're trying to call move()");
        }

        hedgehog.setOldLocation(hedgehog.getX(), hedgehog.getY());
        hedgehog.setLocation(x, y);
        System.out.println("Moving hedgehog to (" + x + ", " + y + ")");

        hedgehog.updateDir();
        System.out.println("Facing " + hedgehog.getDir().toString());

        System.out.println("On top of " + getCard(x, y).getFamily() + " " + getCard(x, y).getType() + "\n");

        // check if hedgehog passed over net
        List<Card> cardsPassedOver = getCardsPassedOver(hedgehog.getOldX(), hedgehog.getOldY(), x, y);
        for (Card card : cardsPassedOver) {
            // System.out.println("Passed over " + card.getFamily() + " " + card.getType());
            if (card.hasRevealedNet()) {
                passedOverNet = true;
                System.out.println("Passed over net!");
            }
        }

    }

    private List<Card> getCardsPassedOver(int oldX, int oldY, int newX, int newY) {
        List<Card> cards = new ArrayList<>();

        // Calculate the direction of the move
        int dx = (newX > oldX) ? 1 : (newX < oldX) ? -1 : 0;
        int dy = (newY > oldY) ? 1 : (newY < oldY) ? -1 : 0;

        // Start at the old position and move towards the new position
        for (int x = oldX, y = oldY; x != newX || y != newY; x += dx, y += dy) {
            cards.add(getCard(x, y));
        }

        cards.remove(0); // remove the card the hedgehog is on

        return cards;
    }

    public boolean playerPassedOverNet() {
        return passedOverNet;
    }

    public Card drawCard() {
        return drawPile.remove(0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {

                if (hedgehog.getX() == x && hedgehog.getY() == y) {
                    if (hedgehog.getDir() == GlobalDir.NORTH)
                        sb.append("█↑  ");
                    else if (hedgehog.getDir() == GlobalDir.EAST)
                        sb.append("█→  ");
                    else if (hedgehog.getDir() == GlobalDir.SOUTH)
                        sb.append("█↓  ");
                    else if (hedgehog.getDir() == GlobalDir.WEST)
                        sb.append("█←  ");
                } else {
                    sb.append(getCard(x, y).toString() + "  ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void nextPlayer() {
        // clear last player's move data
        passedOverNet = false;

        // skip to next player
        if (players.indexOf(currentPlayer) == players.size() - 1) {
            currentPlayer = null;
        } else {
            currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
        }
    }

    public void newRound() {
        currentPlayer = players.get(0);
        // other stuff...?
    }

    public int getNumPlayers() {
        return players.size();
    }

    public int drawPileCount() {
        return drawPile.size();
    }

    public void highlightCards() {

        switch (hedgehog.getDir()) {
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

    public void unhighlightAllCards() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++)
                getCard(x, y).setHighlighted(false);
        }
        highlightedCards.clear();
    }

    // approach #2 - bad performance??
    // private void scanNorth2() {
    // int hedgehogX = hedgehog.getX();
    // int hedgehogY = hedgehog.getY();
    //
    // for (int y = 0; y < gridHeight; y++) {
    // for (int x = 0; x < gridWidth; x++) {
    //
    // Card card = getCard(x, y);
    // if ((hedgehogX == x || hedgehogY == y) && y < hedgehogY && !card.isEmpty()) {
    // card.setHighlighted(true);
    //
    // } else {
    // card.setHighlighted(false);
    // }
    // }
    // }
    // }
    private void scanNorth() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = y - 1; scanDist >= 0; scanDist--) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
            }
        }
        for (int scanDist = 0; scanDist < gridWidth; scanDist++) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
            }
        }
    }

    private void scanEast() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = x + 1; scanDist < gridWidth; scanDist++) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
            }
        }
        for (int scanDist = 0; scanDist < gridHeight; scanDist++) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
            }
        }
    }

    private void scanWest() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = x - 1; scanDist >= 0; scanDist--) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
            }
        }
        for (int scanDist = 0; scanDist < gridHeight; scanDist++) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
            }
        }
    }

    private void scanSouth() {
        int x = hedgehog.getX();
        int y = hedgehog.getY();

        for (int scanDist = y + 1; scanDist < gridHeight; scanDist++) {
            Card card = getCard(x, scanDist);
            if (!card.isEmpty() && !hasHedgehog(x, scanDist)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
            }
        }
        for (int scanDist = 0; scanDist < gridWidth; scanDist++) {
            Card card = getCard(scanDist, y);
            if (!card.isEmpty() && !hasHedgehog(scanDist, y)) {
                card.setHighlighted(true);
                highlightedCards.add(card);
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
        card.setCoordinates(x, y);
        numCardsInPlay++;
    }

    public Card takeCard(int x, int y) {
        numCardsInPlay--;
        Card temp = grid[x][y];
        grid[x][y] = CardFactory.createCard(); // leave an empty card

        if (temp.hasNet()) {
            grid[x][y].placeNet();
            grid[x][y].revealNet();
        }

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

    public Player getPreviousPlayer() {
        Player previousPlayer;
        if (players.indexOf(currentPlayer) == 0) {
            previousPlayer = players.get(players.size() - 1);
        } else {
            previousPlayer = players.get(players.indexOf(currentPlayer) - 1);
        }
        return previousPlayer;
    }

    public Player getNextPlayer() {
        Player nextPlayer;
        if (players.indexOf(currentPlayer) == players.size() - 1) {
            nextPlayer = players.get(0);
        } else {
            nextPlayer = players.get(players.indexOf(currentPlayer) + 1);
        }
        return nextPlayer;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    /**
     * Registers an observer to be notified when the board state changes
     * 
     * @param observer the observer to register
     */
    public void addObserver(BoardObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies observers that the board state has changed
     * 
     * @param readyToMove true if the hedgehog is ready to move, false otherwise
     */
    private void notifyObservers(boolean readyToMove) {
        for (BoardObserver observer : observers) {
            observer.onBoardStateChanged(readyToMove);
        }
    }

    /**
     * Sets the state of the board and notifies observers
     * 
     * @param ready true if the hedgehog is ready to move, false otherwise
     */
    public void setReadyToMove(boolean ready) {
        readyToMove = ready;
        notifyObservers(ready);
    }

    public void setMoveCallback(MoveCallback callback) {
        this.moveCallback = callback;
    }

    public void playerMoveCompleted() {
        if (moveCallback != null) {
            moveCallback.onMoveCompleted();
        }
    }

    public void setKeyCallback(KeyPressCallback callback) {
        this.keyPressCallback = callback;
    }

    public void keyPressed(KeyCode keyCode) {
        if (keyPressCallback != null) {
            keyPressCallback.onKeyPressed(keyCode);
        }
    }

    public boolean cannotMove() {
        return highlightedCards.isEmpty();
    }

    public ArrayList<Card> getHighlightedCards() {
        return new ArrayList<>(highlightedCards);
    }

    public boolean containsMaxCards() {
        return numCardsInPlay == gridWidth * gridHeight;
    }
}