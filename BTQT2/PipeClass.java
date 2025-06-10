import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PipeClass {
    private int x, y;
    private int width, height;
    private boolean isTop;
    private boolean scored;
    private BufferedImage pipeImage;
    
    // Colors for custom pipe graphics as fallback
    private Color pipeMainColor = new Color(0, 204, 0); // Bright green
    private Color pipeEdgeColor = new Color(0, 153, 0); // Darker green
    private Color pipeRimColor = new Color(0, 102, 0); // Even darker green
    
    public PipeClass(int x, int y, int width, int height, boolean isTop) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isTop = isTop;
        this.scored = false;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            String baseFileName = isTop ? "toppipe" : "bottompipe";
            
            // Try multiple possible locations for the image
            File[] possiblePaths = {
                new File(baseFileName + ".png"),
                new File("flappy_bird_game/" + baseFileName + ".png"),
                new File(baseFileName + ".jpg"),
                new File("flappy_bird_game/" + baseFileName + ".jpg")
            };
            
            for (File file : possiblePaths) {
                if (file.exists()) {
                    pipeImage = ImageIO.read(file);
                    System.out.println("Successfully loaded pipe image from: " + file.getAbsolutePath());
                    break;
                }
            }
            
            if (pipeImage == null) {
                System.out.println("Warning: Could not find pipe image in any of the expected locations.");
            }
        } catch (IOException e) {
            System.out.println("Error loading pipe image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void update(int speed) {
        x -= speed;
    }
    
    public void draw(Graphics2D g) {
        if (pipeImage != null) {
            g.drawImage(pipeImage, x, y, width, height, null);
        } else {
            // Fallback to custom drawing
            // Draw the main pipe
            g.setColor(pipeMainColor);
            g.fillRect(x, y, width, height);
            
            // Draw pipe rim (at the opening)
            g.setColor(pipeRimColor);
            int rimHeight = 20;
            if (isTop) {
                g.fillRect(x - 5, height + y - rimHeight, width + 10, rimHeight);
            } else {
                g.fillRect(x - 5, y, width + 10, rimHeight);
            }
            
            // Draw pipe edges
            g.setColor(pipeEdgeColor);
            g.fillRect(x, y, 10, height); // Left edge
            g.fillRect(x + width - 10, y, 10, height); // Right edge
            
            // Add pipe pattern/texture (checkerboard effect)
            g.setColor(pipeEdgeColor);
            int squareSize = 10;
            for (int i = 0; i < width/squareSize; i += 2) {
                for (int j = 0; j < height/squareSize; j += 2) {
                    g.fillRect(x + i*squareSize, y + j*squareSize, squareSize, squareSize);
                }
            }
            for (int i = 1; i < width/squareSize; i += 2) {
                for (int j = 1; j < height/squareSize; j += 2) {
                    g.fillRect(x + i*squareSize, y + j*squareSize, squareSize, squareSize);
                }
            }
        }
    }
    
    public boolean collidesWith(Bird bird) {
        Rectangle pipeRect = new Rectangle(x, y, width, height);
        return pipeRect.intersects(bird.getBounds());
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
    
    public boolean isTop() {
        return isTop;
    }
    
    public boolean isScored() {
        return scored;
    }
    
    public void setScored(boolean scored) {
        this.scored = scored;
    }
} 