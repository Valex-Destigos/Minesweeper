package Minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MenuScreen extends JPanel {

    private enum Difficulty {
        EASY, MEDIUM, HARD;
    }

    private Difficulty difficulty = Difficulty.EASY;
    private JButton startButton;
    private JButton difficultyButton;
    private GameBoard.GameState gameState;

    public MenuScreen(GameBoard.GameState gameState) {
        this.gameState = gameState;

        createStartOrEndScreen();

        this.setPreferredSize(GameGUI.getScreenSize());
    }

    private class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (gameState == GameBoard.GameState.GAME_INITIALIZED) {
                GameBoard.setBoardSize(difficulty == Difficulty.EASY ? 10 : difficulty == Difficulty.MEDIUM ? 15 : 20);
                GameBoard gameBoard = GameBoard.getInstance();
                gameBoard.initializeGame();

                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(MenuScreen.this);
                frame.setContentPane(GameGUI.getInstance());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.revalidate();
            } else if (gameState == GameBoard.GameState.GAME_OVER) {
                try {
                    Runtime.getRuntime().exec("java -cp bin Minesweeper.Main");
                } catch (IOException f) {
                    System.out.println("Error restarting game");
                }
                System.exit(0);
            }
        }
    }

    private class DifficultyButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            switch (difficulty) {
                case EASY:
                    difficulty = Difficulty.MEDIUM;
                    difficultyButton.setText("Difficulty: " + difficulty);
                    break;
                case MEDIUM:
                    difficulty = Difficulty.HARD;
                    difficultyButton.setText("Difficulty: " + difficulty);
                    break;
                case HARD:
                    difficulty = Difficulty.EASY;
                    difficultyButton.setText("Difficulty: " + difficulty);
                    break;
            }
        }
    }

    private void createStartOrEndScreen() {
        if (gameState == GameBoard.GameState.GAME_INITIALIZED) {
            startButton = new JButton("Start Game");
            startButton.addActionListener(new StartButtonListener());
            this.add(startButton);

            difficultyButton = new JButton("Difficulty: " + difficulty);
            difficultyButton.addActionListener(new DifficultyButtonListener());
            this.add(difficultyButton);
        } else if (gameState == GameBoard.GameState.GAME_OVER) {
            startButton = new JButton("Play Again");
            startButton.addActionListener(new StartButtonListener());
            this.add(startButton);
        }
    }
}