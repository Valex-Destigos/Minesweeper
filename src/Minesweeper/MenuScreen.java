package Minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import Minesweeper.GameBoard.GameState;

public class MenuScreen extends JPanel {

    private final int gameWidth = GameGUI.GAME_WIDTH;
    private final int gameHeight = GameGUI.GAME_HEIGHT;

    private Difficulty difficulty = Difficulty.EASY;
    private JTextPane title;
    private JTextPane subTitle;
    private JTextPane gameOverText;
    private JTextPane gameWonText;
    private JButton startButton;
    private JButton difficultyButton;
    private JButton playAgainButton;
    private GameBoard.GameState gameState;

    private enum Difficulty {
        EASY, MEDIUM, HARD;
    }

    public MenuScreen(GameBoard.GameState gameState) {
        this.gameState = gameState;

        createStartOrEndScreen();

        setBackground(Color.darkGray);
        setPreferredSize(GameGUI.getScreenSize());
        setLayout(null);
        addTitle();
        addSubTitle();
    }

    private void createStartOrEndScreen() {
        if (gameState == GameState.GAME_INITIALIZED) {
            addStartButton();
            addDifficultyButton();
        } else if (gameState == GameState.GAME_OVER) {
            addGameOverText();
            addPlayAgainButton();
        } else if (gameState == GameState.GAME_WON) {
            addGameWonText();
            addPlayAgainButton();
        }
    }

    private void addTitle() {
        title = new JTextPane();
        title.setEditable(false);
        title.setText("Minesweeper");
        title.setFont(new Font("Impact", Font.BOLD, 50));
        title.setBackground(Color.darkGray);
        title.setForeground(Color.white);
        title.setBounds(gameWidth / 2 - 150, (int) (gameHeight * 0.15), 300, 70);
        add(title);
    }

    private void addSubTitle() {
        subTitle = new JTextPane();
        subTitle.setEditable(false);
        subTitle.setText("~ Viorel Tsigos ~");
        subTitle.setFont(new Font("Impact", Font.PLAIN, 20));
        subTitle.setBackground(Color.darkGray);
        subTitle.setForeground(Color.white);
        subTitle.setBounds((gameWidth - 150) / 2, (int) (gameHeight * 0.25), 150, 50);
        add(subTitle);
    }

    private void addGameOverText() {
        gameOverText = new JTextPane();
        gameOverText.setEditable(false);
        gameOverText.setText("GAME OVER");
        gameOverText.setFont(new Font("Impact", Font.BOLD, 45));
        gameOverText.setBackground(Color.darkGray);
        gameOverText.setForeground(Color.red);
        gameOverText.setBounds((gameWidth - 220) / 2, (int) (gameHeight * 0.40), 220, 50);
        add(gameOverText);
    }

    private void addGameWonText() {
        gameWonText = new JTextPane();
        gameWonText.setEditable(false);
        gameWonText.setText("GAME WON");
        gameWonText.setFont(new Font("Impact", Font.BOLD, 45));
        gameWonText.setBackground(Color.darkGray);
        gameWonText.setForeground(Color.green);
        gameWonText.setBounds((gameWidth - 210) / 2, (int) (gameHeight * 0.40), 210, 50);
        add(gameWonText);
    }

    private void addStartButton() {
        startButton = new JButton("Start Game");
        startButton.addActionListener(new StartButtonListener());
        startButton.setBounds((gameWidth - 160) / 2, (int) (gameHeight * 0.55), 160, 50);
        this.add(startButton);
    }

    private void addDifficultyButton() {
        difficultyButton = new JButton("Difficulty: " + difficulty);
        difficultyButton.addActionListener(new DifficultyButtonListener());
        difficultyButton.setBounds((gameWidth - 160) / 2, (int) (gameHeight * 0.65), 160, 50);
        this.add(difficultyButton);
    }

    private void addPlayAgainButton() {
        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(new StartButtonListener());
        playAgainButton.setBounds((gameWidth - 160) / 2, (int) (gameHeight * 0.65), 160, 50);
        this.add(playAgainButton);
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
            } else if (gameState == GameState.GAME_OVER || gameState == GameState.GAME_WON) {
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
}