package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class Form_DanhMuc extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 192, 203);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
    private static final Font TITLE_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14);

    private int selectedRow = -1;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JPanel rightPanel;
    private JPanel productsPanel;

    private class ImagePanel extends JPanel {
        private Image image;
        private int width;
        private int height;

        public ImagePanel(String imagePath, int width, int height) {
            this.width = width;
            this.height = height;
            try {
                image = new ImageIcon(getClass().getResource(imagePath))
                        .getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            } catch (Exception e) {
                image = createDefaultImage(width, height);
            }
            setPreferredSize(new Dimension(width, height));
        }

        private Image createDefaultImage(int width, int height) {
            BufferedImage defaultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = defaultImage.createGraphics();
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.GRAY);
            g2d.drawRect(0, 0, width-1, height-1);
            g2d.drawString("No Image", width/4, height/2);
            g2d.dispose();
            return defaultImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 2;
                g.drawImage(image, x, y, null);
            }
        }
    }

    public Form_DanhMuc() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 0));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Tạo panel chính chứa table
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(getWidth() * 7/10, getHeight()));

        // Panel bên trái chứa danh mục
        JPanel leftPanel = new JPanel(new BorderLayout(0, 20));
        leftPanel.setBackground(Color.WHITE);
        
        leftPanel.add(createTopPanel(), BorderLayout.NORTH);
        leftPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.CENTER);

        // Panel bên phải để hiển thị sản phẩm
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));
        rightPanel.setPreferredSize(new Dimension(320, getHeight()));

        // Panel chứa tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("Danh sách sản phẩm");
        titleLabel.setFont(TITLE_FONT);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Panel chứa danh sách sản phẩm
        productsPanel = new JPanel();
        productsPanel.setBackground(Color.WHITE);
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        rightPanel.add(titlePanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    private JPanel createProductCard(String name, int quantity, String imagePath) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(245, 245, 245), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(260, 100));
        card.setPreferredSize(new Dimension(260, 100));

        // Image panel
        ImagePanel imagePanel = new ImagePanel(imagePath, 80, 80);
        imagePanel.setBackground(Color.WHITE);
        JPanel imageContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imageContainer.setBackground(Color.WHITE);
        imageContainer.add(imagePanel);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel quantityLabel = new JLabel("Số lượng: " + quantity);
        quantityLabel.setFont(CONTENT_FONT);
        quantityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.setBorder(new EmptyBorder(5, 10, 5, 5));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(quantityLabel);

        card.add(imageContainer, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        // Hover effect
        MouseAdapter hoverAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(HOVER_COLOR);
                imageContainer.setBackground(HOVER_COLOR);
                infoPanel.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                imageContainer.setBackground(Color.WHITE);
                infoPanel.setBackground(Color.WHITE);
            }
        };
        
        card.addMouseListener(hoverAdapter);
        imageContainer.addMouseListener(hoverAdapter);
        infoPanel.addMouseListener(hoverAdapter);

        // Wrapper panel
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(card);
        wrapperPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return wrapperPanel;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(20, 0));
        topPanel.setBackground(Color.WHITE);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.setFont(CONTENT_FONT);
        
        JButton searchButton = createRoundedButton("", "/icon/search.png", false);
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionsPanel.setBackground(Color.WHITE);
        
        JButton addButton = createRoundedButton("Thêm danh mục", "/icon/circle-plus.png", true);
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(160, 38));
        
        addButton.addActionListener(e -> {
            String newCategory = JOptionPane.showInputDialog(Form_DanhMuc.this, 
                "Nhập tên danh mục mới:", 
                "Thêm danh mục", 
                JOptionPane.PLAIN_MESSAGE);
                
            if (newCategory != null && !newCategory.trim().isEmpty()) {
                Object[] rowData = {
                    "DM" + String.format("%03d", tableModel.getRowCount() + 1),
                    newCategory,
                    ""
                };
                tableModel.addRow(rowData);
            }
        });

        JButton editButton = createRoundedButton("Edit", "/icon/pencil.png", true);
        editButton.addActionListener(e -> {
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(Form_DanhMuc.this,
                    "Vui lòng chọn một danh mục để chỉnh sửa.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                String currentName = (String) tableModel.getValueAt(selectedRow, 1);
                String newName = JOptionPane.showInputDialog(Form_DanhMuc.this,
                    "Chỉnh sửa tên danh mục:",
                    currentName);
                
                if (newName != null && !newName.trim().isEmpty()) {
                    tableModel.setValueAt(newName, selectedRow, 1);
                }
            }
        });

        JButton deleteButton = createRoundedButton("Xóa", "/icon/trash.png", true);
        deleteButton.addActionListener(e -> {
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(Form_DanhMuc.this,
                    "Vui lòng chọn một danh mục để xóa.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                int confirm = JOptionPane.showConfirmDialog(Form_DanhMuc.this,
                    "Bạn có chắc muốn xóa danh mục này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                    selectedRow = -1;
                    productsPanel.removeAll();
                    productsPanel.revalidate();
                    productsPanel.repaint();
                }
            }
        });
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionsPanel, BorderLayout.EAST);
        
        return topPanel;
    }

    // ... (phần code còn lại giống như trước)
    
    private void updateProductsPanel(String category) {
        productsPanel.removeAll();
        
        // Thêm sản phẩm mẫu dựa trên danh mục
        if (category.equals("Váy")) {
            productsPanel.add(createProductCard("Váy caro cuti", 36, "/img/vay1.png"));
            productsPanel.add(createProductCard("Váy công sở", 25, "/img/vay1.png"));
            productsPanel.add(createProductCard("Váy dự tiệc", 15, "/img/vay1.png"));
            productsPanel.add(createProductCard("Váy maxi", 20, "/img/vay.png"));
            productsPanel.add(createProductCard("Váy body", 30, "/img/vay.png"));
        } else if (category.equals("Áo")) {
            productsPanel.add(createProductCard("Áo thun basic", 50, "/img/ao.png"));
            productsPanel.add(createProductCard("Áo sơ mi trắng", 30, "/img/ao.png"));
            productsPanel.add(createProductCard("Áo khoác jean", 20, "/img/ao.png"));
            productsPanel.add(createProductCard("Áo len", 25, "/img/ao.png"));
        } else if (category.equals("Quần")) {
            productsPanel.add(createProductCard("Quần jean slim", 45, "/img/quan.png"));
            productsPanel.add(createProductCard("Quần tây", 28, "/img/quan.png"));
            productsPanel.add(createProductCard("Quần short", 33, "/img/quan.png"));
        }
        
        productsPanel.revalidate();
        productsPanel.repaint();
    }
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBackground(Color.WHITE);
        
        String[] columns = {"Mã DM", "Tên danh mục", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(CONTENT_FONT);
        table.setRowHeight(32);
        table.setGridColor(new Color(245, 245, 245));
        table.setSelectionBackground(HOVER_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        
        // Căn giữa nội dung và điều chỉnh độ rộng cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(centerRenderer);
            
            switch (i) {
                case 0: // Mã DM
                    column.setPreferredWidth(80);
                    column.setMaxWidth(80);
                    column.setMinWidth(80);
                    break;
                case 1: // Tên danh mục
                    column.setPreferredWidth(150);
                    break;
                case 2: // Ghi chú
                    column.setPreferredWidth(200);
                    break;
            }
        }
        
        // Thêm sự kiện click để hiển thị sản phẩm
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String category = (String) table.getValueAt(selectedRow, 1);
                    updateProductsPanel(category);
                }
            }
        });

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add sample data
        addSampleData();
        
        return tablePanel;
    }

    private JButton createRoundedButton(String text, String iconPath, boolean isRounded) {
        JButton button = new JButton(text);
        button.setFont(CONTENT_FONT);
        if (iconPath != null && !iconPath.isEmpty()) {
            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        }
        
        if (isRounded) {
            button.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        } else {
            button.setBorder(BorderFactory.createEmptyBorder());
        }
        
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(text.isEmpty() ? 38 : 130, 38));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getForeground().equals(Color.WHITE)) {
                    button.setBackground(HOVER_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.getForeground().equals(Color.WHITE)) {
                    button.setBackground(PRIMARY_COLOR);
                } else {
                    button.setBackground(Color.WHITE);
                }
            }
        });
        
        return button;
    }

    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = CONTENT_COLOR;
            this.trackColor = Color.WHITE;
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y,
                           thumbBounds.width, thumbBounds.height,
                           10, 10);
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(trackColor);
            if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            } else {
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }
            g2.dispose();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }

    private void addSampleData() {
        Object[][] data = {
            {"DM001", "Váy", "Các loại váy ngắn, váy dài"},
            {"DM002", "Quần", "Quần jean, quần tây, quần short"},
            {"DM003", "Áo", "Áo thun, áo sơ mi, áo khoác"},
            {"DM004", "Phụ kiện", "Túi xách, ví, dây nịt"},
            {"DM005", "Giày dép", "Giày cao gót, giày thể thao"}
        };
        
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private void performSearch(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    // Các phương thức getter và utility
    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    // Phương thức refresh
    public void refreshProductsPanel() {
        if (selectedRow != -1) {
            String category = (String) table.getValueAt(selectedRow, 1);
            updateProductsPanel(category);
        }
    }




}