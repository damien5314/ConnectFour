package com.ddiehl.java.riffsyconnectfour.model;

public class Player {
    private final BoardSpace.Value mSymbol;

    public Player(BoardSpace.Value symbol) {
        mSymbol = symbol;
    }

    public BoardSpace.Value getSymbol() {
        return mSymbol;
    }
}
