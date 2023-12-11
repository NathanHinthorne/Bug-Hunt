package com.Butterfly.model.cards;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;

public abstract class Card {


    protected String myImagePath;
    protected CardType myType;
    protected CardFamily myFamily;
    protected boolean isEmpty;
    private boolean hasNet;
    private boolean isHighlighted;

    public Card() {
        isHighlighted = false;
    }


    //! BIG SIDENOTE
    // Preferably, we would have a separate class called PointCounter or something
    // this would be a static class, acting as a utility class or database to store all card point info
    // it would assign values to each card.
    //      purpose: to avoid a lot of re-calculation of each card's points from other card instances
    public abstract int getPoints(Map<CardFamily, List<Card>> playerCollection);

    public String findImagePath() {
        return "/images/" + myFamily.toString() + myType.toString() + ".png";
    }

    public Image getImage() {
        InputStream stream = getClass().getResourceAsStream("/images/" + myFamily.toString() + myType.toString() + ".png");

        if (stream == null) {
            stream = getClass().getResourceAsStream("/images/error.png");
        }

        Image image = new Image(stream);

//        if (image == null) {
//            stream = getClass().getResourceAsStream("/images/error.png");
//        }

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

}
