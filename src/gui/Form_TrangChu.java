package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class Form_TrangChu extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CARD_1_COLOR = new Color(251, 207, 232);
    private static final Color CARD_2_COLOR = new Color(249, 168, 212);
    private static final Color CARD_3_COLOR = new Color(244, 114, 182);
    private static final Color CARD_4_COLOR = new Color(236, 72, 153);
    private static final Color CARD_TEXT_COLOR = new Color(131, 24, 67);
    
    private static final Font CARD_TITLE_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16);
    private static final Font CARD_NUMBER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24);
    private static final Font CARD_LABEL_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14);
    
    private static final int BANNER_WIDTH = 800;
    private static final int BANNER_HEIGHT = 300;
    
    private JPanel bannerPanel;
    private Timer bannerTimer;
    
    public Form_TrangChu() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        
        // Banner Section
        add(createBannerSection());
        
        // Dashboard Section
        add(createDashboardSection());
        
        // Start banner rotation
        startBannerRotation();
    }
    
    private JPanel createBannerSection() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Rounded banner panel
        bannerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                                   java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(253, 242, 248));
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        bannerPanel.setPreferredSize(new Dimension(BANNER_WIDTH, BANNER_HEIGHT));
        bannerPanel.setOpaque(false);

        // Center the banner
        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centeringPanel.setBackground(Color.WHITE);
        centeringPanel.add(bannerPanel);
        
        container.add(centeringPanel, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel createDashboardSection() {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        container.add(createDashboardCard("Nhân viên", "150", "Đang làm việc", CARD_1_COLOR, "/icon/users.png"));
        container.add(createDashboardCard("Sản phẩm", "1,234", "Trong kho", CARD_2_COLOR, "/icon/shirt.png"));
        container.add(createDashboardCard("Nhà cung cấp", "45", "Đang hợp tác", CARD_3_COLOR, "/icon/container.png"));
        container.add(createDashboardCard("Doanh thu", "5.6M", "Tháng này", CARD_4_COLOR, "/icon/chart-no-axes-combined.png"));
        
        return container;
    }
    
    private JPanel createDashboardCard(String title, String number, String label, Color backgroundColor, String iconPath) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                                   java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backgroundColor);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            }
        };
        card.setPreferredSize(new Dimension(250, 120));
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        card.setOpaque(false);

        // Icon and Title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setOpaque(false);
        
        if (iconPath != null) {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            topPanel.add(new JLabel(new ImageIcon(scaledImage)));
        }
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(CARD_TITLE_FONT);
        titleLabel.setForeground(CARD_TEXT_COLOR);
        topPanel.add(titleLabel);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(CARD_NUMBER_FONT);
        numberLabel.setForeground(CARD_TEXT_COLOR);
        numberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(label);
        descLabel.setFont(CARD_LABEL_FONT);
        descLabel.setForeground(CARD_TEXT_COLOR);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(numberLabel);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(descLabel);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }
    
    private void startBannerRotation() {
        String[] imagePaths = {"/icon/banner1.png", "/icon/banner2.png", "/icon/banner3.png"};
        JLabel[] bannerImages = new JLabel[imagePaths.length];
        
        // Create banner images
        for (int i = 0; i < imagePaths.length; i++) {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePaths[i]));
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image scaledImage = icon.getImage().getScaledInstance(
                    BANNER_WIDTH, BANNER_HEIGHT, Image.SCALE_SMOOTH);
                bannerImages[i] = new JLabel(new ImageIcon(scaledImage));
            } else {
                bannerImages[i] = new JLabel("Banner " + (i + 1));
                bannerImages[i].setHorizontalAlignment(SwingConstants.CENTER);
                bannerImages[i].setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 36));
                bannerImages[i].setForeground(PRIMARY_COLOR);
            }
            bannerImages[i].setPreferredSize(new Dimension(BANNER_WIDTH, BANNER_HEIGHT));
        }
        
        // Setup rotation timer
        final int[] currentIndex = {0};
        bannerTimer = new Timer(3000, e -> {
            bannerPanel.removeAll();
            bannerPanel.add(bannerImages[currentIndex[0]], BorderLayout.CENTER);
            bannerPanel.revalidate();
            bannerPanel.repaint();
            currentIndex[0] = (currentIndex[0] + 1) % bannerImages.length;
        });
        bannerTimer.start();
    }
    
    public void stopBannerRotation() {
        if (bannerTimer != null && bannerTimer.isRunning()) {
            bannerTimer.stop();
        }
    }
}