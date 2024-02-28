package Minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cell {
    private final List<Optional<Cell>> neighbors;
    private final int x;
    private final int y;
    private int mineCount;

    private final GameBoard gameBoard = GameBoard.getInstance();
    private boolean isMine = false;
    private State state = State.COVERED;

    private enum State {
        UNCOVERED, COVERED, FLAGGED;
    }

    public Cell(int x, int y) {
        neighbors = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            neighbors.add(Optional.empty());
        }
        this.x = x;
        this.y = y;
        int i = 0;
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
                        addNeighbor(index, Optional.of(gameBoard.getBoard()[i][j]));
                    }
                    index++;
                }
            }
        }
    }

    public void setMine() {
        isMine = true;
    }

    public void countMines() {
        mineCount = neighbors.stream().filter(Optional::isPresent).map(Optional::get).filter(Cell::isMine).toList()
                .size();
    }

    public boolean isMine() {
        return isMine;
    }

    public State getState() {
        return state;
    }

    private void addNeighbor(int index, Optional<Cell> optionalCell) {
        neighbors.set(index, optionalCell);
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
            if (mineCount == 0) {
                for (Optional<Cell> neighbor : neighbors) {
                    if (neighbor.isPresent()) {
                        neighbor.get().uncover();
                    }
                }
            }
        }
    }
}
