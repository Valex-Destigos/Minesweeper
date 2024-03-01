package Minesweeper;

import javax.swing.JFrame;

import Minesweeper.GameBoard.GameState;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MenuScreen(GameState.GAME_INITIALIZED));
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Minesweeper");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}