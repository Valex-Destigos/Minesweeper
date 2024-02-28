package Minesweeper;

import java.awt.Point;

public class GameBoard {
    private static final int BOARD_SIZE = 10;
    private static final double MINE_RATIO = 0.1;
    private static GameBoard instance;

    private Cell[][] board;

    private GameBoard(int size) {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
    }
    
    public void initializeGame() {
        initializeBoard();
        calculateNeighbors();
        placeMines();
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
    
    public void handleLeftClick(Point p) {
        int x = p.x / (GameGraphics.GAME_WIDTH / BOARD_SIZE);
        int y = p.y / (GameGraphics.GAME_HEIGHT / BOARD_SIZE);
        board[x][y].uncover();
    }

    public void handleRightClick(Point p) {
        int x = p.x / (GameGraphics.GAME_WIDTH / BOARD_SIZE);
        int y = p.y / (GameGraphics.GAME_HEIGHT / BOARD_SIZE);
        board[x][y].toggleFlag();
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
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (Math.random() < MINE_RATIO) {
                    board[i][j].setMine();
                }
            }
        }

    }
}
