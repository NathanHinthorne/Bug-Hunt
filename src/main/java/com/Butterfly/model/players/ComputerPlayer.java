package com.Butterfly.model.players;

import com.Butterfly.model.board.Board;

import java.util.ArrayList;
import java.util.Collections;

import com.Butterfly.model.board.Coordinates;
import com.Butterfly.model.cards.*;
import javafx.geometry.Point2D;

public class ComputerPlayer extends Player {

    private Board board;

    public ComputerPlayer(Board board) {
        this.board = board;
    }

    @Override
    public Card cardToMoveTo() {
        ArrayList<Card> cards = board.getHighlightedCards();
        Collections.shuffle(cards);

        return cards.get(0);
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
