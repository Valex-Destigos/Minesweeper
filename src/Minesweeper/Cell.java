package Minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.function.Predicate;

import Minesweeper.GameBoard.GameState;

public class Cell {
    private final List<Optional<Cell>> neighbors;
    private final int x;
    private final int y;
    private int mineCount;

    private final GameBoard gameBoard = GameBoard.getInstance();
    private boolean isMine = false;
    private State state = State.COVERED;

    public enum State {
        UNCOVERED, COVERED, FLAGGED;
    }

    public Cell(int x, int y) {
        neighbors = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            neighbors.add(Optional.empty());
        }
        this.x = x;
        this.y = y;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public void calculateNeighbors() {
        int index = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i != x || j != y) {
                    if (i >= 0 && i < gameBoard.getBoardSize() && j >= 0 && j < gameBoard.getBoardSize()) {
                        setNeighbor(index, Optional.of(gameBoard.getBoard()[i][j]));
                        if (gameBoard.getBoard()[i][j].isMine()) {
                            mineCount++;
                        }
                    }
                    index++;
                }
            }
        }
        countMines();
    }

    public void setMine() {
        isMine = true;
    }

    private void countMines() {
        mineCount = neighbors.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Cell::isMine)
                .toList()
                .size();
    }

    public boolean isMine() {
        return isMine;
    }

    public State getState() {
        return state;
    }

    public int getMineCount() {
        countMines();
        return mineCount;
    }

    public void revealMine(long sleepTime) {
        if (isMine()) {
            state = State.UNCOVERED;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }

    public int countNeighborFlags() {
        return (int) neighbors.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(cell -> cell.getState() == State.FLAGGED)
                .count();
    }

    public void toggleFlag() {
        if (state == State.COVERED) {
            state = State.FLAGGED;
        } else if (state == State.FLAGGED) {
            state = State.COVERED;
        }
    }

    public void uncover() {
        if (state == State.COVERED) {
            state = State.UNCOVERED;
            if (isMine()) {
                gameBoard.setGameState(GameState.GAME_OVER);
            }
            if (gameBoard.getGameState() == GameState.RUNNING && mineCount == 0) {
                neighbors.stream().filter(Optional::isPresent).map(Optional::get).forEach(Cell::uncover);
            } else if (gameBoard.getGameState() == GameState.GAME_INITIALIZED) {
                gameBoard.setGameState(GameState.RUNNING);
                gameBoard.startGame();
                neighbors.stream()
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(Predicate.not(Cell::isMine))
                        .forEach(Cell::uncover);
            }
        } else if (state == State.UNCOVERED) {
            if (getMineCount() <= countNeighborFlags()) {
                neighbors.stream()
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(cell -> cell.getState() == State.COVERED)
                        .forEach(Cell::uncover);
            }
        }
    }

    private void setNeighbor(int index, Optional<Cell> optionalCell) {
        neighbors.set(index, optionalCell);
    }
}
