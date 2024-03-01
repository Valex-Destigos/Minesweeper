package Minesweeper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameGUI extends JPanel implements Runnable {

    public static final int GAME_WIDTH = 720;
    public static final int GAME_HEIGHT = 720;

    private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);

    private static GameGUI instance;

    private final Thread graphicsThread;
    private final GameBoard gameBoard = GameBoard.getInstance();
    private final int width = (int) SCREEN_SIZE.getWidth() / gameBoard.getBoardSize();
    private final int height = (int) SCREEN_SIZE.getHeight() / gameBoard.getBoardSize();

    private Image image;
    private Graphics graphics;

    private GameGUI() {
        this.setFocusable(true);
        this.addMouseListener(new ActionListener());
        this.setPreferredSize(SCREEN_SIZE);
        graphicsThread = new Thread(this);
        graphicsThread.start();
    }

    public static GameGUI getInstance() {
        if (instance == null) {
            instance = new GameGUI();
        }
        return instance;
    }

    public static Dimension getScreenSize() {
        return SCREEN_SIZE;
    }

    
    public void endGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(instance);
        frame.setContentPane(new MenuScreen(gameBoard.getGameState()));
        frame.revalidate();
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
                repaint();
                delta--;
            }
        }
    }

    @Override
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

        // draw cells
        for (int i = 0; i < gameBoard.getBoardSize(); i++) {
            for (int j = 0; j < gameBoard.getBoardSize(); j++) {
                drawCell(g, i, j);
            }
        }

        // draw grid
        g.setColor(Color.white);
        int heightRatio = (int) SCREEN_SIZE.getHeight() / gameBoard.getBoardSize();
        int widthRatio = (int) SCREEN_SIZE.getWidth() / gameBoard.getBoardSize();
        g2d.setStroke(new BasicStroke(3.0f));
        for (int i = 1; i < gameBoard.getBoardSize(); i++) {
            g.drawLine(heightRatio * i, 0, heightRatio * i, GAME_HEIGHT);
        }
        for (int j = 1; j < gameBoard.getBoardSize(); j++) {
            g.drawLine(0, widthRatio * j, GAME_WIDTH, widthRatio * j);
        }
    }

    public void mouseClicked(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        if (gameBoard.getGameState() != GameBoard.GameState.GAME_OVER) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                gameBoard.handleLeftClick(p);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                gameBoard.handleRightClick(p);
            }
        }
    }

    public class ActionListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            GameGUI.getInstance().mouseClicked(e);
        }
    }

    private void drawCell(Graphics g, int i, int j) {
        int x = i * width;
        int y = j * height;
        Cell cell = gameBoard.getBoard()[i][j];
        Font font = new Font("Monospaced", Font.BOLD, width / 2);
        g.setFont(font);
        String text = cell.getMineCount() + "";
        FontMetrics metrics = g.getFontMetrics(font);
        int xString = x + (width - metrics.stringWidth(text)) / 2;
        int yString = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        if (cell.getState() == Cell.State.UNCOVERED) {
            if (cell.isMine()) {
                g.setColor(Color.red);
                g.fillRect(x, y, width, height);
            } else {
                g.setColor(Color.lightGray);
                g.fillRect(x, y, width, height);
                g.drawRect(x, y, width, height);
                g.setColor(getMineCountColor(cell.getMineCount()));
                g.drawString(text, xString, yString);
            }
        } else {
            g.setColor(Color.gray);
            g.fillRect(x, y, width, height);
            g.drawRect(x, y, width, height);
            g.setColor(Color.black);
            if (cell.getState() == Cell.State.FLAGGED) {
                drawFlag(g, x, y);
            }
        }
    }

    private void drawFlag(Graphics g, int x, int y) {
        g.setColor(new Color(255, 69, 0));
        g.fillRect((int) (x + width * 0.44), (int) (y + height * 0.1), (int) (width * 0.12), (int) (height * 0.8));

        g.fillOval((int) (x + width * 0.2), (int) (y + height * 0.8), (int) (width * 0.6), (int) (height * 0.1));

        int[] xFlag = { (int) (x + width * 0.48), (int) (x + width * 0.48), (int) (x + width * 0.9) };
        int[] yFlag = { (int) (y + height * 0.1), (int) (y + height * 0.6), (int) (y + height * 0.35) };
        g.fillPolygon(xFlag, yFlag, 3);

    }

    private Color getMineCountColor(int mines) {
        switch (mines) {
            case 0:
                return Color.lightGray;
            case 1:
                return Color.blue;
            case 2:
                return Color.green;
            case 3:
                return Color.red;
            case 4:
                return Color.magenta;
            case 5:
                return Color.orange;
            case 6:
                return Color.cyan;
            case 7:
                return Color.yellow;
            case 8:
                return Color.pink;
            default:
                return Color.black;
        }
    }
}
