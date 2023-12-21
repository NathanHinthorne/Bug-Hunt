package com.BugHunt.model.board;

@FunctionalInterface
public interface MoveCallback {
    void onMoveCompleted();
}