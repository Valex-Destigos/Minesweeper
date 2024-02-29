package Minesweeper;

public class Main {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        GameBoard gameBoard = GameBoard.getInstance();
        gameBoard.initializeGame();
        GameGUI gameGraphics = GameGUI.getInstance();
    }
}