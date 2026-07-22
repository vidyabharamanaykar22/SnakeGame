import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;

    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];

    private int bodyParts = 3;
    private int applesEaten = 0;

    private int appleX;
    private int appleY;

    private char direction = 'R';
    private boolean running = false;

    private Timer timer;
    private Random random;

    public SnakeGame() {

        random = new Random();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        startGame();
    }

    public void startGame() {

        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';

        x[0] = 100;
        y[0] = 100;

        newApple();

        running = true;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newApple() {
        appleX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            // Apple (Red)
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Snake (Red)
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(Color.RED);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Score : " + applesEaten, 15, 30);

        } else {

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 170, 250);

            g.setFont(new Font("Arial", Font.PLAIN, 22));
            g.drawString("Score : " + applesEaten, 240, 300);
            g.drawString("Press SPACE to Restart", 150, 340);
        }
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] -= UNIT_SIZE;
                break;

            case 'D':
                y[0] += UNIT_SIZE;
                break;

            case 'L':
                x[0] -= UNIT_SIZE;
                break;

            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {

        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {

        for (int i = bodyParts; i > 0; i--) {

            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0)
            running = false;

        if (x[0] >= WIDTH)
            running = false;

        if (y[0] < 0)
            running = false;

        if (y[0] >= HEIGHT)
            running = false;

        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                if (direction != 'R')
                    direction = 'L';
                break;

            case KeyEvent.VK_RIGHT:
                if (direction != 'L')
                    direction = 'R';
                break;

            case KeyEvent.VK_UP:
                if (direction != 'D')
                    direction = 'U';
                break;

            case KeyEvent.VK_DOWN:
                if (direction != 'U')
                    direction = 'D';
                break;

            case KeyEvent.VK_SPACE:
                if (!running)
                    startGame();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake Game");

        SnakeGame game = new SnakeGame();

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}