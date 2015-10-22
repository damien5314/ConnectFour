package com.ddiehl.java.riffsyconnectfour.model;

public class Board {
    private BoardSpace[][] mSpaces;

    /**
     * Default constructor initializes board 6 spaces high and 7 spaces wide
     */
    public Board() {
        this(7, 6);
    }

    /**
     * Constructor which initializes a board of given width and height
     * @param width Number of spaces horizontally
     * @param height Number of spaces vertically
     */
    public Board(int width, int height) {
        mSpaces = new BoardSpace[height][width];
        // Initialize each space
        for (int row = 0; row < mSpaces.length; row++) {
            for (int col = 0; col < mSpaces[0].length; col++) {
                mSpaces[row][col] = new BoardSpace();
            }
        }
    }

    public BoardSpace get(int row, int column) {
        return mSpaces[row][column];
    }

    public int getWidth() {
        return mSpaces[0].length;
    }

    public int getHeight() {
        return mSpaces.length;
    }

    /**
     * Place a game piece at the bottom-most empty space in the specified column
     * @param column Column in which to place the game piece
     * @param value Value of the game piece to place
     * @return True if operation was successful, false otherwise (no available spaces)
     */
    public boolean addPiece(int column, BoardSpace.Value value) {
        // Iterate up through the rows of the specified column until we find the first empty space
        for (int row = mSpaces.length-1; row >= 0; row--) {
            if (mSpaces[row][column].getValue() == BoardSpace.Value.Empty) {
                // Place the piece at that space
                mSpaces[row][column].setValue(value);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if board is completely full with game pieces
     * @return True if board if completely full, false if there exists at least one empty space
     */
    public boolean isFull() {
        // Iterate through the board
        for (int row = 0; row < mSpaces.length; row++) {
            for (int col = 0; col < mSpaces[0].length; col++) {
                // If we find a space that is empty, return false
                if (mSpaces[row][col].getValue() == BoardSpace.Value.Empty) return false;
            }
        }
        return true;
    }

    /**
     * Prints game board in a grid
     * @return Game board in String format
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int row = 0; row < mSpaces.length; row++) {
            for (int col = 0; col < mSpaces[0].length; col++) {
                s.append(mSpaces[row][col]);
            }
            s.append("\n"); // New line after each row
        }
        return s.toString();
    }
}
