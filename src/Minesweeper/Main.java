package Minesweeper;

public class Main {

    public static void main(String[] args) {
        GameBoard gameBoard = GameBoard.getInstance();
        gameBoard.initializeGame();
        GameGraphics gameGraphics = GameGraphics.getInstance();
    }
}