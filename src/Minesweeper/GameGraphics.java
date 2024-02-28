package Minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameGraphics extends JPanel implements Runnable {

    public static final int GAME_WIDTH = 750;
    public static final int GAME_HEIGHT = 750;
    public static int[][] coordinates = new int[10][10];

    private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    private static GameGraphics instance;

    private Thread gameThread;
    private Image image;
    private Graphics graphics;
    private GameBoard gameBoard = GameBoard.getInstance();

    private GameGraphics() {
        this.setFocusable(true);
        this.addMouseListener(new ActionListener());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public static GameGraphics getInstance() {
        if (instance == null) {
            instance = new GameGraphics();
        }
        return instance;
    }

    @Override
    public void run() {
        // game loop
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000 / 60; // 60 fps
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            if (delta >= 1) {
                // TODO: update game
                repaint();
                delta--;
            }
        }
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);

    }

    public void draw(Graphics g) {
        // anti-aliasing
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw grid
        g2d.setColor(Color.white);
        for (int i = 1; i < 10; i++) {
            g2d.drawLine(75 * i, 0, 75 * i, GAME_HEIGHT);
        }
        for (int j = 1; j < 10; j++) {
            g2d.drawLine(0, 75 * j, GAME_WIDTH, 75 * j);
        }
    }

    public void mouseClicked(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());

        if (SwingUtilities.isLeftMouseButton(e)) {
           gameBoard.handleLeftClick(p);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            gameBoard.handleRightClick(p);
        }
    }

    public class ActionListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            GameGraphics.getInstance().mouseClicked(e);
        }
    }
}
