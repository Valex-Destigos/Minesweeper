package Minesweeper;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class GameBoard implements Runnable {
    private static int BOARD_SIZE = 10; // default value, later changed by user
    private static final double MINE_RATIO = 0.2;
    private static final int MINIMUM_MINES = Double.valueOf(BOARD_SIZE * MINE_RATIO).intValue();
    private static GameBoard instance;
    private static GameGUI gameGUI;

    private Cell[][] board;
    private Thread logicThread;
    private GameState gameState = GameState.GAME_INITIALIZED;
    private long sleepTime = 200;

    public enum GameState {
        GAME_INITIALIZED, RUNNING, GAME_OVER;
    }

    private GameBoard() {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        logicThread = new Thread(this);
        logicThread.start();
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
            instance = new GameBoard();
        }
        return instance;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public static void setBoardSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be greater than 0.");
        }
        BOARD_SIZE = size;
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

    public void showSolution() {
        Set<Cell> mines = new HashSet<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].isMine()) {
                    mines.add(board[i][j]);
                }
            }
        }
        mines.forEach(cell -> {
            cell.revealMine(sleepTime);
            if (sleepTime > 20) {
                sleepTime -= 15;
            } else {
                sleepTime = 10;
            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }
        gameGUI = GameGUI.getInstance();
        gameGUI.endGame();
    }

    @Override
    public void run() {
        // game loop
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000 / 30; // 30 fps
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            if (delta >= 1) {
                if (gameState == GameState.GAME_OVER) {
                    showSolution();
                }
                delta--;
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
