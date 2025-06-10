import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bird {
    private int x, y;
    private int width, height;
    private int velocity;
    private final int GRAVITY = 1;
    private BufferedImage birdImage;
    
    // Keep colors for fallback only if needed
    private Color bodyColor = new Color(255, 204, 0);
    private Color beakColor = new Color(255, 102, 0);
    private Color eyeColor = Color.WHITE;
    private Color pupilColor = Color.BLACK;
    private Color wingColor = new Color(255, 153, 0);
    
    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 30;
        this.velocity = 0;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            // Try multiple possible locations for the image
            File[] possiblePaths = {
                new File("flappybird.png"),
                new File("flappy_bird_game/flappybird.png"),
                new File("flappybird.jpg"),
                new File("flappy_bird_game/flappybird.jpg")
            };
            
            for (File file : possiblePaths) {
                if (file.exists()) {
                    birdImage = ImageIO.read(file);
                    System.out.println("Successfully loaded bird image from: " + file.getAbsolutePath());
                    break;
                }
            }
            
            if (birdImage == null) {
                System.out.println("Warning: Could not find bird image in any of the expected locations.");
            }
        } catch (IOException e) {
            System.out.println("Error loading bird image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void update() {
        velocity += GRAVITY;
        y += velocity;
    }
    
    public void jump(int force) {
        velocity = force;
    }
    
    public void draw(Graphics2D g) {
        if (birdImage != null) {
            // Use the exact image provided
            g.drawImage(birdImage, x, y, width, height, null);
        } else {
            // Only use custom drawing as fallback
            g.setColor(bodyColor);
            g.fillOval(x, y, width, height);
            
            // Wing
            g.setColor(wingColor);
            g.fillOval(x - 5, y + height/2 - 5, width/2, height/2);
            
            // Beak
            g.setColor(beakColor);
            int[] xPoints = {x + width - 5, x + width + 10, x + width - 5};
            int[] yPoints = {y + height/3, y + height/2, y + 2*height/3};
            g.fillPolygon(xPoints, yPoints, 3);
            
            // Eye
            g.setColor(eyeColor);
            g.fillOval(x + 2*width/3, y + height/3, width/4, height/4);
            
            // Pupil
            g.setColor(pupilColor);
            g.fillOval(x + 2*width/3 + 2, y + height/3 + 2, width/8, height/8);
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    // For collision detection
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
} 