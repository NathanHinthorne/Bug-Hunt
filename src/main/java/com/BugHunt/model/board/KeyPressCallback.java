package com.BugHunt.model.board;

import javafx.scene.input.KeyCode;

@FunctionalInterface
public interface KeyPressCallback {

    void onKeyPressed(KeyCode keyCode);
}