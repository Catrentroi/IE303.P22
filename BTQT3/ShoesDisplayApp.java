import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.geom.RoundRectangle2D;

public class ShoesDisplayApp extends JFrame {
    private JPanel mainProductPanel;
    private JLabel mainProductImage;
    private JLabel mainProductName;
    private JLabel mainProductPrice;
    private JLabel mainProductBrand;
    private JLabel mainProductDescription;
    private JPanel productGridPanel;
    private List<Product> products;
    private Product currentProduct;
    private Timer fadeTimer;
    private float alpha = 1.0f;
    private JPanel selectedItem = null;

    public ShoesDisplayApp() {
        setTitle("Adidas Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        initializeProducts();
        initializeComponents();
        layoutComponents();
        setResizable(false);
        displayProduct(products.get(0));
    }

    private void initializeProducts() {
        products = new ArrayList<>();
        products.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas",
                "This product is excluded from all promotional discounts and offers.",
                loadImageIcon("img1.png")));
        products.add(new Product("FORUM MID SHOES", "$100.00", "Adidas",
                "This product is excluded from all promotional discounts and offers.",
                loadImageIcon("img2.png")));
        products.add(new Product("SUPERNOVA SHOES", "$150.00", "Adidas",
                "NMD City Stock 2",
                loadImageIcon("img3.png")));
        products.add(new Product("ULTRABOOST 22", "$160.00", "Adidas",
                "NMD City Stock 2",
                loadImageIcon("img4.png")));
        products.add(new Product("ADIZERO PRIME X", "$120.00", "Adidas",
                "This product is excluded from all promotional discounts and offers.",
                loadImageIcon("img5.png")));
        products.add(new Product("GAZELLE VINTAGE", "$160.00", "Adidas",
                "This product is excluded from all promotional discounts and offers.",
                loadImageIcon("img6.png")));
        products.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas",
                "This product is excluded from all promotional discounts and offers.",
                loadImageIcon("img1.png")));
        products.add(new Product("FORUM MID SHOES", "$100.00", "Adidas",
                "This product is excluded from all promotional discounts and offers.",
                loadImageIcon("img2.png")));
    }    private ImageIcon loadImageIcon(String filename) {
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.File(filename));
            if (img != null) {
                Image scaledImg = img.getScaledInstance(300, 240, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            System.out.println("Không thể load ảnh: " + filename + " - " + e.getMessage());
        }
        return createDefaultIcon(filename);
    }

    private ImageIcon createDefaultIcon(String filename) {
        Color[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN };
        int index = Math.abs(filename.hashCode()) % colors.length;
        return createColorIcon(colors[index], 350, 280);
    }

    private ImageIcon createColorIcon(Color color, int width, int height) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(0, 0, color.brighter(),
                width, height, color.darker());
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, width, height, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "DEMO IMAGE";
        int x = (width - fm.stringWidth(text)) / 2;
        int y = height / 2;
        g2d.drawString(text, x, y);

        g2d.dispose();
        return new ImageIcon(img);
    }

    private void initializeComponents() {       
        mainProductPanel = new JPanel(new BorderLayout());
        mainProductPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        mainProductPanel.setBackground(Color.WHITE);

        mainProductImage = new JLabel();
        mainProductImage.setHorizontalAlignment(JLabel.CENTER);
        mainProductImage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));mainProductName = new JLabel();
        mainProductName.setFont(new Font("Arial", Font.BOLD, 24));
        mainProductName.setForeground(new Color(33, 33, 33));
        mainProductName.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainProductPrice = new JLabel();
        mainProductPrice.setFont(new Font("Arial", Font.BOLD, 22));
        mainProductPrice.setForeground(new Color(33, 33, 33));
        mainProductPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainProductPrice.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));        mainProductBrand = new JLabel();
        mainProductBrand.setFont(new Font("Arial", Font.PLAIN, 14));
        mainProductBrand.setForeground(new Color(117, 117, 117));
        mainProductBrand.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainProductDescription = new JLabel();
        mainProductDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        mainProductDescription.setForeground(new Color(117, 117, 117));
        mainProductDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainProductDescription.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        infoPanel.add(mainProductName);
        infoPanel.add(mainProductPrice);
        infoPanel.add(mainProductBrand);
        infoPanel.add(mainProductDescription);

        mainProductPanel.add(mainProductImage, BorderLayout.CENTER);
        mainProductPanel.add(infoPanel, BorderLayout.SOUTH);

        productGridPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        productGridPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 30));
        productGridPanel.setBackground(Color.WHITE);

        createProductGrid();
    }

    private void createProductGrid() {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            JPanel productItem = createProductGridItem(product, i);
            productGridPanel.add(productItem);
        }
    }

    private JPanel createProductGridItem(Product product, int index) {
        RoundedPanel item = new RoundedPanel(new BorderLayout(), 12, new Color(248, 248, 248));
        item.setBorder(new RoundedBorder(new Color(248, 248, 248), 2, 12));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Product image
        ImageIcon originalIcon = product.getImage();
        Image scaledImage = originalIcon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        imageLabel.setOpaque(false); 

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false); 
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 12, 12, 12));


        JLabel nameLabel = new JLabel(
                "<html><div style='width: 130px; font-weight: bold;'>" + product.getName() + "</div></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 11));
        nameLabel.setForeground(new Color(33, 33, 33));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel brandLabel = new JLabel(product.getBrand());
        brandLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        brandLabel.setForeground(new Color(117, 117, 117));
        brandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel descLabel = new JLabel("<html><div style='width: 130px; color: #999999; font-size: 9px;'>" +
                product.getDescription() + "</div></html>");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel priceLabel = new JLabel(product.getPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 13));
        priceLabel.setForeground(new Color(33, 33, 33));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(brandLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalGlue()); 
        infoPanel.add(priceLabel);

        item.add(imageLabel, BorderLayout.CENTER);
        item.add(infoPanel, BorderLayout.SOUTH);

        // Set first item as selected
        if (index == 0) {
            item.setBorder(new RoundedBorder(new Color(0, 102, 204), 2, 12));
            selectedItem = item;
        }

        // Add mouse listeners
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (item != selectedItem) {
                    item.setBorder(new RoundedBorder(new Color(200, 200, 200), 2, 12));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (item != selectedItem) {
                    item.setBorder(new RoundedBorder(new Color(248, 248, 248), 2, 12));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Remove selection from previous item
                if (selectedItem != null) {
                    selectedItem.setBorder(new RoundedBorder(new Color(248, 248, 248), 2, 12));
                }

                // Set new selection
                item.setBorder(new RoundedBorder(new Color(0, 102, 204), 2, 12));
                selectedItem = item;

                displayProductWithAnimation(product);
            }
        });

        return item;
    }

    private void displayProduct(Product product) {
        currentProduct = product;
        mainProductImage.setIcon(product.getImage());
        mainProductName.setText(product.getName());
        mainProductPrice.setText(product.getPrice());
        mainProductBrand.setText(product.getBrand());
        mainProductDescription.setText(product.getDescription());
    }

    private void displayProductWithAnimation(Product product) {
        if (product.equals(currentProduct))
            return;

        // Fade out effect
        fadeTimer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.15f;
                if (alpha <= 0) {
                    alpha = 0;
                    fadeTimer.stop();

                    // Change product
                    displayProduct(product);

                    // Fade in effect
                    Timer fadeInTimer = new Timer(15, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            alpha += 0.15f;
                            if (alpha >= 1.0f) {
                                alpha = 1.0f;
                                ((Timer) e.getSource()).stop();
                            }
                            repaintMainProduct();
                        }
                    });
                    fadeInTimer.start();
                }
                repaintMainProduct();
            }
        });
        fadeTimer.start();
    }

    private void repaintMainProduct() {
        mainProductPanel.repaint();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Main content
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(mainProductPanel);
        splitPane.setRightComponent(new JScrollPane(productGridPanel));
        splitPane.setDividerLocation(400); 
        splitPane.setResizeWeight(0.4); 
        splitPane.setBorder(null);
        splitPane.setBackground(Color.WHITE);

        mainContent.add(splitPane, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new ShoesDisplayApp().setVisible(true);
        });
    }
}


class Product {
    private String name;
    private String price;
    private String brand;
    private String description;
    private ImageIcon image;

    public Product(String name, String price, String brand, String description, ImageIcon image) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public ImageIcon getImage() {
        return image;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product product = (Product) obj;
        return name.equals(product.name) && price.equals(product.price);
    }
}


class RoundedBorder extends AbstractBorder {
    private Color color;
    private int thickness;
    private int radius;

    public RoundedBorder(Color color, int thickness, int radius) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRoundRect(x + thickness / 2, y + thickness / 2,
                width - thickness, height - thickness, radius, radius);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
    }
}

// Custom JPanel with rounded background
class RoundedPanel extends JPanel {
    private int radius;
    private Color backgroundColor;

    public RoundedPanel(LayoutManager layout, int radius, Color backgroundColor) {
        super(layout);
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2d.dispose();
        super.paintComponent(g);
    }
}