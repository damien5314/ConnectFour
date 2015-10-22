package com.ddiehl.java.riffsyconnectfour.model;

public class BoardSpace {
    public enum Value {
        Empty, X, O
    }
    private Value mValue = Value.Empty; // Board spaces are empty by default

    public Value getValue() {
        return mValue;
    }

    public void setValue(Value value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue == Value.Empty ? "* " : mValue.name() + " ";
    }
}
