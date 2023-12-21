package com.BugHunt.model.board;

@FunctionalInterface
public interface BoardObserver {
    void onBoardStateChanged(boolean readyToMove);
}
