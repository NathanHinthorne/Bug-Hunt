package com.Butterfly.model.board;

@FunctionalInterface
public interface BoardObserver {
    void onBoardStateChanged(boolean readyToMove);
}
