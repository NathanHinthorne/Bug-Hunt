package com.Butterfly.model.board;

import javafx.scene.image.Image;

import java.io.InputStream;

public class Hedgehog implements java.io.Serializable {

    private static Hedgehog instance = null;
    private int currX;
    private int currY;
    private int oldX;
    private int oldY;
    private GlobalDir myDir;

    private Hedgehog(int x, int y, GlobalDir dir) {
        currX = x;
        currY = y;
        myDir = dir;
    }

    public static Hedgehog getInstance() {
        if (instance == null) {
            throw new NullPointerException("Hedgehog instance is null");
        }
        return instance;
    }

    public static Hedgehog create(int x, int y, GlobalDir dir) {
        instance = new Hedgehog(x, y, dir);
        return instance;
    }

    public int getX() {
        return currX;
    }

    public int getY() {
        return currY;
    }

    public void setLocation(int x, int y) {
        currX = x;
        currY = y;
    }

    public void setOldLocation(int x, int y) {
        oldX = x;
        oldY = y;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public GlobalDir getDir() {
        return myDir;
    }

    public void setDir(GlobalDir dir) {
        myDir = dir;
    }

    public void updateDir() {
        myDir = findNewDir();
    }

    private GlobalDir findNewDir() {
        if (currX == oldX) {
            if (currY > oldY) {
                return GlobalDir.SOUTH;
            } else {
                return GlobalDir.NORTH;
            }
        } else {
            if (currX > oldX) {
                return GlobalDir.EAST;
            } else {
                return GlobalDir.WEST;
            }
        }
    }

    public Image getImage() {

        InputStream stream = getClass().getResourceAsStream("/images/hedgehog.png");

        if (stream == null) {
            throw new NullPointerException("Can't find hedgehog image");
        }

        Image image = new Image(stream);

        return image;
    }
}
