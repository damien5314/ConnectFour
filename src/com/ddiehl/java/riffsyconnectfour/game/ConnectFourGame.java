package com.ddiehl.java.riffsyconnectfour.game;

import com.ddiehl.java.riffsyconnectfour.logging.ConsoleLogger;
import com.ddiehl.java.riffsyconnectfour.logging.Logger;
import com.ddiehl.java.riffsyconnectfour.model.Board;
import com.ddiehl.java.riffsyconnectfour.model.BoardSpace;
import com.ddiehl.java.riffsyconnectfour.model.Player;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Random;

public class ConnectFourGame {
    private static final int TURN_DELAY = 1000; // Delay between turns in milliseconds, set to 0 for instant output
    private Logger mLogger = new ConsoleLogger();
    private GameState mGameState = GameState.NOT_STARTED;
    // TODO We could make this a circular linked list to facilitate keeping the order of the game
    private ArrayList<Player> mPlayers = new ArrayList<>();
    private int mCurrentPlayer = 0;
    private int mNumberOfPlayers = 0;
    private BoardSpace.Value mWinner = null;
    private Board mBoard;

    // Private constructor, should get instance via a static method
    private ConnectFourGame() { }

    public static ConnectFourGame initializeStandardGame() {
        return initializeGame(7, 6);
    }

    public static ConnectFourGame initializeGame(int width, int height) {
        ConnectFourGame game = new ConnectFourGame();

        // Add two players in a traditional game of Connect Four
        game.addPlayer(new Player(BoardSpace.Value.X));
        game.addPlayer(new Player(BoardSpace.Value.O));

        // Initialize and print the board
        game.addBoard(width, height);

        return game;
    }

    public void addBoard(int width, int height) {
        mBoard = new Board(width, height);
    }

    public void addPlayer(Player p) {
        if (mGameState != GameState.NOT_STARTED) {
            throw new InvalidStateException("You cannot add players in the middle of a game");
        }
        mPlayers.add(p);
        mNumberOfPlayers++;
    }

    public void start() {
        Logger logger = new ConsoleLogger();
        logger.print("Welcome to Connect 4\n");
        logger.print("\n");

        // Set game state to STARTED
        mGameState = GameState.STARTED;
        mCurrentPlayer = 0;
        printBoard();

        // Continuously take turns while the game is incomplete
        while (mGameState != GameState.COMPLETE) {
            // If no more moves are available, set game state to complete and break out of loop
            if (mBoard.isFull()) {
                mGameState = GameState.COMPLETE;
                continue;
            }

            // Otherwise, take turn for current player
            Player currentPlayer = mPlayers.get(mCurrentPlayer);
            takeTurnForPlayer(currentPlayer);

            // Print the current state of the board
            printBoard();

            // If someone won, set the game state to complete
            mWinner = getWinner();
            if (mWinner != null) {
                mGameState = GameState.COMPLETE;
                continue;
            }

            // Otherwise, set the next player in line
            mCurrentPlayer++;
            if (mCurrentPlayer >= mNumberOfPlayers) mCurrentPlayer = 0;

            // Amateur technique for pausing application, so we can more easily see the game progress
            try {
                Thread.sleep(TURN_DELAY);
            } catch (InterruptedException e) {
                mLogger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        showResult();
    }

    private void takeTurnForPlayer(Player player) {
        ArrayList<Integer> validMoves = new ArrayList<>(mBoard.getWidth());
        // Iterate through each column
        for (int i = 0; i < mBoard.getWidth(); i++) {
            // Check if column has empty spaces available
            if (mBoard.get(0, i).getValue() == BoardSpace.Value.Empty) {
                // If so, add it to valid moves
                validMoves.add(i);
            }
        }

        // Choose a random available column
        Random random = new Random();
        int moveToTake = validMoves.get(random.nextInt(validMoves.size()));
        mBoard.addPiece(moveToTake, player.getSymbol());
    }

    /**
     * @return The value of spaces with at least four matching in a row, horizontally, vertically, or diagonally
     */
    private BoardSpace.Value getWinner() {
        // Check every column
        for (int column = 0; column < mBoard.getWidth(); column++) {
            BoardSpace.Value previousValue = BoardSpace.Value.Empty;
            BoardSpace.Value currentValue;
            int consecutivePiecesOfSameValue = 1;
            for (int row = 0; row < mBoard.getHeight(); row++) {
                currentValue = mBoard.get(row, column).getValue();
                if (currentValue == BoardSpace.Value.Empty || currentValue != previousValue) {
                    consecutivePiecesOfSameValue = 1;
                } else { // We have a streak
                    consecutivePiecesOfSameValue++;
                    // Check if there are at least four in a row
                    if (consecutivePiecesOfSameValue >= 4) {
                        // Found a winner
                        return currentValue;
                    }
                }
                previousValue = currentValue;
            }
        }

        // Check every row
        for (int row = 0; row < mBoard.getHeight(); row++) {
            BoardSpace.Value previousValue = BoardSpace.Value.Empty;
            BoardSpace.Value currentValue;
            int consecutivePiecesOfSameValue = 1;
            for (int column = 0; column < mBoard.getWidth(); column++) {
                currentValue = mBoard.get(row, column).getValue();
                if (currentValue == BoardSpace.Value.Empty || currentValue != previousValue) {
                    consecutivePiecesOfSameValue = 1;
                } else { // We have a streak
                    consecutivePiecesOfSameValue++;
                    // Check if there are at least four in a row
                    if (consecutivePiecesOfSameValue >= 4) {
                        // Found a winner
                        return currentValue;
                    }
                }
                previousValue = currentValue;
            }
        }

        // Check diagonals from top-left to bottom-right
        // Check for each diagonal from the left column
        for (int row = 0; row < mBoard.getHeight(); row++) {
            BoardSpace.Value previousValue = BoardSpace.Value.Empty;
            BoardSpace.Value currentValue;
            int consecutivePiecesOfSameValue = 1;
            int checkSpaceRow = row;
            int checkSpaceColumn = 0;
            while (checkSpaceRow < mBoard.getHeight() && checkSpaceColumn < mBoard.getWidth()) {
                currentValue = mBoard.get(checkSpaceRow, checkSpaceColumn).getValue();
                if (currentValue == BoardSpace.Value.Empty || currentValue != previousValue) {
                    consecutivePiecesOfSameValue = 1;
                } else { // We have a streak
                    consecutivePiecesOfSameValue++;
                    // Check if there are at least four in a row
                    if (consecutivePiecesOfSameValue >= 4) {
                        // Found a winner
                        return currentValue;
                    }
                }
                previousValue = currentValue;
                checkSpaceRow++;
                checkSpaceColumn++;
            }
        }
        // Check for each diagonal from the top row
        for (int column = 0; column < mBoard.getHeight(); column++) {
            BoardSpace.Value previousValue = BoardSpace.Value.Empty;
            BoardSpace.Value currentValue;
            int consecutivePiecesOfSameValue = 1;
            int checkSpaceRow = 0;
            int checkSpaceColumn = column;
            while (checkSpaceRow < mBoard.getHeight() && checkSpaceColumn < mBoard.getWidth()) {
                currentValue = mBoard.get(checkSpaceRow, checkSpaceColumn).getValue();
                if (currentValue == BoardSpace.Value.Empty || currentValue != previousValue) {
                    consecutivePiecesOfSameValue = 1;
                } else { // We have a streak
                    consecutivePiecesOfSameValue++;
                    // Check if there are at least four in a row
                    if (consecutivePiecesOfSameValue >= 4) {
                        // Found a winner
                        return currentValue;
                    }
                }
                previousValue = currentValue;
                checkSpaceRow++;
                checkSpaceColumn++;
            }
        }

        // Check diagonals from top-right to bottom-left
        // Check for each diagonal from the right column
        for (int row = 0; row < mBoard.getHeight(); row++) {
            BoardSpace.Value previousValue = BoardSpace.Value.Empty;
            BoardSpace.Value currentValue;
            int consecutivePiecesOfSameValue = 1;
            int checkSpaceRow = row;
            int checkSpaceColumn = mBoard.getWidth()-1;
            while (checkSpaceRow < mBoard.getHeight() && checkSpaceColumn >= 0) {
                currentValue = mBoard.get(checkSpaceRow, checkSpaceColumn).getValue();
                if (currentValue == BoardSpace.Value.Empty || currentValue != previousValue) {
                    consecutivePiecesOfSameValue = 1;
                } else { // We have a streak
                    consecutivePiecesOfSameValue++;
                    // Check if there are at least four in a row
                    if (consecutivePiecesOfSameValue >= 4) {
                        // Found a winner
                        return currentValue;
                    }
                }
                previousValue = currentValue;
                checkSpaceRow++;
                checkSpaceColumn--;
            }
        }
        // Check for each diagonal from the top row
        for (int column = 0; column < mBoard.getWidth(); column++) {
            BoardSpace.Value previousValue = BoardSpace.Value.Empty;
            BoardSpace.Value currentValue;
            int consecutivePiecesOfSameValue = 1;
            int checkSpaceRow = 0;
            int checkSpaceColumn = column;
            while (checkSpaceRow < mBoard.getHeight() && checkSpaceColumn >= 0) {
                currentValue = mBoard.get(checkSpaceRow, checkSpaceColumn).getValue();
                if (currentValue == BoardSpace.Value.Empty || currentValue != previousValue) {
                    consecutivePiecesOfSameValue = 1;
                } else { // We have a streak
                    consecutivePiecesOfSameValue++;
                    // Check if there are at least four in a row
                    if (consecutivePiecesOfSameValue >= 4) {
                        // Found a winner
                        return currentValue;
                    }
                }
                previousValue = currentValue;
                checkSpaceRow++;
                checkSpaceColumn--;
            }
        }

        // If we didn't find a winner, return null
        return null;
    }

    private void showResult() {
        if (mWinner == null) {
            // No one won
            mLogger.print("This match ended in a draw.");
        } else {
            mLogger.print("Player " + mWinner.name() + " won the game!");
        }
    }

    private void printBoard() {
        mLogger.print(mBoard.toString());
        mLogger.print("\n");
    }

    enum GameState {
        NOT_STARTED, STARTED, COMPLETE
    }
}
