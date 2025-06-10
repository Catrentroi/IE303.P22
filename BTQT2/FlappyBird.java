import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private final int WIDTH = 360;
    private final int HEIGHT = 640;
    private final int GRAVITY = 1;
    private final int JUMP_FORCE = -15;
    private final int PIPE_SPEED = 5;
    private final int PIPE_WIDTH = 80;
    private final int PIPE_GAP = 200;
    private final int PIPE_DELAY = 100;
    
    // Game variables
    private boolean gameRunning = false;
    private boolean gameOver = false;
    private int score = 0;
    private int pipeDelayCounter = 0;
    
    // Game objects
    private Bird bird;
    private ArrayList<PipeClass> pipes;
    private Random random;
    private Timer timer;
    
    // Images
    private BufferedImage backgroundImage;
    
    // Background colors
    private Color skyColor = new Color(135, 206, 235); // Sky blue
    private Color cloudColor = new Color(255, 255, 255); // White
    private Color grassColor = new Color(124, 252, 0); // Lawn green
    private Color groundColor = new Color(210, 180, 140); // Tan
    private Color buildingColor = new Color(169, 169, 169); // Dark gray
    private Color buildingWindowColor = new Color(255, 255, 224); // Light yellow
    
    // Cloud positions
    private ArrayList<Point> cloudPositions = new ArrayList<>();
    private ArrayList<Integer> cloudSizes = new ArrayList<>();
    
    // Building positions
    private ArrayList<Rectangle> buildings = new ArrayList<>();
    
    public FlappyBird() {
        // Set up the panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        
        // Load images
        loadImages();
        
        // Generate background elements
        generateBackgroundElements();
        
        // Initialize game objects
        bird = new Bird(WIDTH / 3, HEIGHT / 2);
        pipes = new ArrayList<>();
        random = new Random();
        
        // Set up the timer for game loop (60 FPS)
        timer = new Timer(16, this);
        timer.start();
    }
    
    private void loadImages() {
        try {
            // Try multiple possible locations for the background image
            File[] possiblePaths = {
                new File("flappybirdbg.png"),
                new File("flappy_bird_game/flappybirdbg.png"),
                new File("flappybirdbg.jpg"),
                new File("flappy_bird_game/flappybirdbg.jpg"),
                new File("background.png"),
                new File("flappy_bird_game/background.png")
            };
            
            for (File file : possiblePaths) {
                if (file.exists()) {
                    backgroundImage = ImageIO.read(file);
                    System.out.println("Successfully loaded background image from: " + file.getAbsolutePath());
                    break;
                }
            }
            
            if (backgroundImage == null) {
                System.out.println("Warning: Could not find background image in any of the expected locations.");
            }
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void generateBackgroundElements() {
        // Generate random clouds
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            cloudPositions.add(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT / 3)));
            cloudSizes.add(rand.nextInt(30) + 20);
        }
        
        // Generate buildings
        int buildingCount = WIDTH / 40;
        for (int i = 0; i < buildingCount; i++) {
            int buildingHeight = rand.nextInt(100) + 50;
            int buildingWidth = rand.nextInt(20) + 30;
            int x = i * 40;
            int y = HEIGHT - buildingHeight - 40; // 40 is ground height
            buildings.add(new Rectangle(x, y, buildingWidth, buildingHeight));
        }
    }
    
    private void resetGame() {
        gameRunning = false;
        gameOver = false;
        score = 0;
        bird = new Bird(WIDTH / 3, HEIGHT / 2);
        pipes.clear();
        pipeDelayCounter = 0;
        repaint();
    }
    
    private void startGame() {
        if (!gameRunning && !gameOver) {
            gameRunning = true;
        }
    }
    
    private void checkCollision() {
        // Check if bird hit the ground or ceiling
        if (bird.getY() <= 0 || bird.getY() >= HEIGHT - bird.getHeight()) {
            gameOver = true;
            gameRunning = false;
        }
        
        // Check if bird hit any pipe
        for (PipeClass pipe : pipes) {
            if (pipe.collidesWith(bird)) {
                gameOver = true;
                gameRunning = false;
                break;
            }
        }
    }
    
    private void updateGame() {
        if (!gameRunning) return;
        
        // Update bird position
        bird.update();
        
        // Generate new pipes
        pipeDelayCounter++;
        if (pipeDelayCounter >= PIPE_DELAY) {
            pipeDelayCounter = 0;
            
            int pipeHeight = 100 + random.nextInt(HEIGHT - PIPE_GAP - 200);
            pipes.add(new PipeClass(WIDTH, 0, PIPE_WIDTH, pipeHeight, true));
            pipes.add(new PipeClass(WIDTH, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_GAP, false));
        }
        
        // Update pipes and check for score
        for (int i = 0; i < pipes.size(); i++) {
            PipeClass pipe = pipes.get(i);
            pipe.update(PIPE_SPEED);
            
            // If pipe is no longer visible, remove it
            if (pipe.getX() + pipe.getWidth() < 0) {
                pipes.remove(i);
                i--;
                continue;
            }
            
            // Check if bird passed a pipe for scoring
            if (!pipe.isScored() && pipe.isTop() && bird.getX() > pipe.getX() + pipe.getWidth()) {
                pipe.setScored(true);
                score++;
            }
        }
        
        // Check for collisions
        checkCollision();
    }
    
    private void drawCustomBackground(Graphics2D g2d) {
        // Draw sky gradient
        GradientPaint skyGradient = new GradientPaint(
            0, 0, skyColor, 
            0, HEIGHT, new Color(173, 216, 230)
        );
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Draw clouds
        g2d.setColor(cloudColor);
        for (int i = 0; i < cloudPositions.size(); i++) {
            Point p = cloudPositions.get(i);
            int size = cloudSizes.get(i);
            g2d.fillOval(p.x, p.y, size, size/2);
            g2d.fillOval(p.x + size/4, p.y - size/4, size/2, size/2);
            g2d.fillOval(p.x + size/2, p.y, size/2, size/3);
        }
        
        // Draw buildings in background
        for (Rectangle building : buildings) {
            // Building body
            g2d.setColor(buildingColor);
            g2d.fillRect(building.x, building.y, building.width, building.height);
            
            // Windows
            g2d.setColor(buildingWindowColor);
            int windowSize = 5;
            int windowGap = 8;
            for (int y = building.y + 10; y < building.y + building.height - 10; y += windowGap) {
                for (int x = building.x + 5; x < building.x + building.width - 5; x += windowGap) {
                    g2d.fillRect(x, y, windowSize, windowSize);
                }
            }
        }
        
        // Draw ground
        g2d.setColor(groundColor);
        g2d.fillRect(0, HEIGHT - 40, WIDTH, 40);
        
        // Draw grass
        g2d.setColor(grassColor);
        g2d.fillRect(0, HEIGHT - 40, WIDTH, 10);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw background
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        } else {
            drawCustomBackground(g2d);
        }
        
        // Draw pipes
        for (PipeClass pipe : pipes) {
            pipe.draw(g2d);
        }
        
        // Draw bird
        bird.draw(g2d);
        
        // Draw score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("Score: " + score, 10, 40);
        
        // Draw game instructions or game over message
        if (!gameRunning && !gameOver) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Press Space or Enter to start", 50, HEIGHT / 2);
        } else if (gameOver) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("Game Over!", WIDTH / 2 - 80, HEIGHT / 2 - 30);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Score: " + score, WIDTH / 2 - 40, HEIGHT / 2 + 10);
            g2d.drawString("Press R to restart", WIDTH / 2 - 70, HEIGHT / 2 + 50);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
            if (!gameRunning && !gameOver) {
                startGame();
            } else if (gameRunning) {
                bird.jump(JUMP_FORCE);
            }
        } else if (key == KeyEvent.VK_R && gameOver) {
            resetGame();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
} 