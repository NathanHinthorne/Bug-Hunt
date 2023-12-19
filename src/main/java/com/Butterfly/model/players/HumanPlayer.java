package com.Butterfly.model.players;

import com.Butterfly.model.board.Coordinates;
import com.Butterfly.model.cards.*;
import javafx.geometry.Point2D;

public class HumanPlayer extends Player {

    public HumanPlayer() {

    }

    @Override
    public Card cardToMoveTo() {
        return null;
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
