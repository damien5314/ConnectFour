package com.ddiehl.java.riffsyconnectfour.logging;

public class ConsoleLogger implements Logger {
    @Override
    public void print(String s) {
        System.out.print(s);
    }

    @Override
    public void error(String s) {
        System.err.print(s);
    }
}
