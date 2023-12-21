package com.BugHunt.model.cards;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import com.BugHunt.model.board.Coordinates;
import javafx.scene.image.Image;

public abstract class Card implements java.io.Serializable {

    protected String myImagePath;
    protected CardType myType;
    protected CardFamily myFamily;
    protected int myPoints;
    protected boolean isEmpty;
    private boolean hasNet;
    private boolean hasRevealedNet;
    private boolean isHighlighted;
    private Coordinates myCoordinates = null;

    public Card() {
        isHighlighted = false;
    }

    // NOTE
    // we could have a separate class called PointCounter or something
    // this would be a static class, acting as a utility class or database to store
    // all card point info
    // it would assign values to each card.
    // purpose: to avoid a lot of re-calculation of each card's points from other
    // card instances

    /**
     * Calculates and sets the points of the card based on the player's collection
     * 
     * @param playerCollection the player's collection
     */
    public abstract void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection);

    /**
     * Merely a getter method for the card's points.
     * DOES NOT calculate the points, so it could be outdated unless you call
     * calculatePoints() first.
     * 
     * @return the card's points
     */
    public int getPoints() {
        return myPoints;
    }

    public String findImagePath() {
        return "/images/" + myFamily.toString() + myType.toString() + ".png";
    }

    public Image getImage() {
        InputStream stream = getClass()
                .getResourceAsStream("/images/cards/" + myFamily.toString() + myType.toString() + ".png");

        if (stream == null) {
            stream = getClass().getResourceAsStream("/images/cards/error.png");
        }

        Image image = new Image(stream);

        // if (image == null) {
        // stream = getClass().getResourceAsStream("/images/error.png");
        // }

        return image;
    }

    public CardFamily getFamily() {
        return myFamily;
    }

    public CardType getType() {
        return myType;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public void placeNet() {
        hasNet = true;
    }

    public boolean hasNet() {
        return hasNet;
    }

    public boolean hasRevealedNet() {
        return hasRevealedNet;
    }

    public void revealNet() {
        hasRevealedNet = true;
    }

    public Coordinates getCoordinates() {
        return myCoordinates;
    }

    public void setCoordinates(int x, int y) {
        myCoordinates = new Coordinates(x, y);
    }

}
