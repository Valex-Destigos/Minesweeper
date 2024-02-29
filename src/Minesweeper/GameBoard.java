package Minesweeper;

import java.awt.Point;

public class GameBoard {
    private static final int BOARD_SIZE = 15;
    private static final double MINE_RATIO = 0.2;
    private static final int MINIMUM_MINES = Double.valueOf(BOARD_SIZE * MINE_RATIO).intValue();
    private static GameBoard instance;

    private Cell[][] board;
    private GameState gameState = GameState.GAME_INITIALIZED;

    public enum GameState {
        GAME_INITIALIZED, RUNNING, GAME_OVER;
    }

    private GameBoard(int size) {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
    }

    public void initializeGame() {
        initializeBoard();
    }

    public void startGame() {
        placeMines();
        calculateNeighbors();
    }

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard(BOARD_SIZE);
        }
        return instance;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void handleLeftClick(Point p) {
        int x = p.x / (GameGUI.GAME_WIDTH / BOARD_SIZE);
        int y = p.y / (GameGUI.GAME_HEIGHT / BOARD_SIZE);
        if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
            board[x][y].uncover();
            if (board[x][y].isMine()) {
                gameState = GameState.GAME_OVER;
            }
        }
    }

    public void handleRightClick(Point p) {
        int x = p.x / (GameGUI.GAME_WIDTH / BOARD_SIZE);
        int y = p.y / (GameGUI.GAME_HEIGHT / BOARD_SIZE);
        if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
            board[x][y].toggleFlag();
        }
    }

    public void showSolutionIfGameOver() {
        if (gameState == GameState.GAME_OVER) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j].isMine()) {
                        board[i][j].revealMine();
                    }
                }
            }
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
    }

    private void calculateNeighbors() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j].calculateNeighbors();
            }
        }
    }

    private void placeMines() {
        while (countMines() < MINIMUM_MINES) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (Math.random() < MINE_RATIO && board[i][j].getState() == Cell.State.COVERED) {
                        board[i][j].setMine();
                    }
                }
            }
        }

    }

    private int countMines() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }
}
