package com.ddiehl.java.riffsyconnectfour;

import com.ddiehl.java.riffsyconnectfour.game.ConnectFourGame;

public class GameRunner {
    public static void main(String[] args) {
        // Start a standard game
        ConnectFourGame game = ConnectFourGame.initializeStandardGame();
        game.start();
    }
}
