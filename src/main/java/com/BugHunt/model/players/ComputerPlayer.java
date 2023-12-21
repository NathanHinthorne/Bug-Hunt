package com.BugHunt.model.players;

import com.BugHunt.model.board.Board;

import java.util.ArrayList;
import java.util.Collections;

import com.BugHunt.model.cards.*;

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
