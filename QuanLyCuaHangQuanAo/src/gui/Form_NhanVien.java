package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;

import dialog.SuaNhanVienDialog;
import dialog.ThemNhanVienDialog;
public class Form_NhanVien extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 192, 203);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Font TAB_FONT = new Font("Arial", Font.BOLD, 14);
    
    private JTabbedPane tabbedPane;
    private int selectedRow = -1;

    public Form_NhanVien() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(TAB_FONT);
        tabbedPane.setBackground(Color.WHITE);
        
        // Style cho tabbedPane
        styleTabbedPane();
        
        // Tạo các panel
        JPanel quanLyNVPanel = createTabPanel("Quản lý nhân viên", 
            new String[]{"Mã NV", "Họ và tên", "Giới tính", "Ngày sinh", "SDT", "Email"},
            createSampleEmployeeData());
        
        JPanel phanQuyenPanel = createTabPanel("Phân quyền", 
            new String[]{"Mã quyền", "Tên quyền truy cập"},
            createSamplePermissionData());
            
        JPanel taiKhoanPanel = createTabPanel("Tài khoản", 
            new String[]{"Mã NV", "Tên đăng nhập", "Nhóm quyền", "Trạng thái"},
            createSampleAccountData());
        
        // Thêm các tab
        tabbedPane.addTab("Quản lý nhân viên", quanLyNVPanel);
        tabbedPane.addTab("Phân quyền", phanQuyenPanel);
        tabbedPane.addTab("Tài khoản", taiKhoanPanel);
        
        // Set màu cho text của tab
        for(int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setForegroundAt(i, new Color(50, 50, 50));
        }
        
        add(tabbedPane, BorderLayout.CENTER);
        setOpaque(true);
    }

    private JPanel createTabPanel(String title, String[] columns, Object[][] data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Top Controls Panel
        JPanel topControls = new JPanel(new BorderLayout(20, 0));
        topControls.setBackground(Color.WHITE);
        topControls.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        
        JTextField searchField = new JTextField(20);
        styleTextField(searchField);
        
        JButton searchBtn = createIconButton("/icon/search.png");
        
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);
        
        String addButtonText = "Thêm " + (title.contains("quyền") ? "quyền" : 
                             title.contains("tài khoản") ? "tài khoản" : "nhân viên");
        
        JButton addBtn = createActionButton(addButtonText, "/icon/circle-plus.png", true);
        JButton editBtn = createActionButton("Sửa", "/icon/pencil.png", false);
        JButton deleteBtn = createActionButton("Xóa", "/icon/trash.png", false);
        JButton aboutBtn = createActionButton("About", "/icon/info.png", false);
        JButton exportBtn = createActionButton("Xuất Excel", "/icon/printer.png", false);
        
        actionPanel.add(addBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(aboutBtn);
        actionPanel.add(exportBtn);
        
        topControls.add(searchPanel, BorderLayout.WEST);
        topControls.add(actionPanel, BorderLayout.EAST);
        
        // Table
        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        styleScrollPane(scrollPane);
        
        // Add components to panel
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(0, 30, 30, 30));
        mainContent.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(topControls, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);
        
        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(CONTENT_FONT);
        table.setRowHeight(40);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(HOVER_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(new Color(255, 240, 245));
        header.setForeground(Color.BLACK);
        header.setBorder(new LineBorder(new Color(240, 240, 240)));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        
        // Center align for first column and status/gender columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        // Additional center alignment for specific columns based on table type
        if (table.getColumnCount() > 2) {
            if (table.getColumnName(2).equals("Giới tính")) {
                table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            }
            if (table.getColumnName(3).equals("Trạng thái")) {
                table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            }
        }
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(new LineBorder(new Color(240, 240, 240)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(Color.WHITE);
        
        // Custom scrollbar
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR;
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
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 38));
        field.setFont(CONTENT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(240, 240, 240)),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createIconButton(String iconPath) {
        JButton button = new JButton(new ImageIcon(getClass().getResource(iconPath)));
        button.setPreferredSize(new Dimension(38, 38));
        button.setBackground(Color.WHITE);
        button.setBorder(new LineBorder(new Color(240, 240, 240)));
        button.setFocusPainted(false);
        styleButtonHover(button);
        return button;
    }

    private JButton createActionButton(String text, String iconPath, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(CONTENT_FONT);
        button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        button.setIconTextGap(10);
        button.setPreferredSize(new Dimension(text.length() > 10 ? 150 : 120, 38));
        button.setFocusPainted(false);
        
        if (isPrimary) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setBorder(new LineBorder(PRIMARY_COLOR));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(new LineBorder(new Color(240, 240, 240)));
            styleButtonHover(button);
        }
        
        return button;
    }

    private void styleButtonHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getBackground().equals(PRIMARY_COLOR)) {
                    button.setBackground(HOVER_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getBackground().equals(PRIMARY_COLOR)) {
                    button.setBackground(Color.WHITE);
                }
            }
        });
    }

    // Sample data methods
    private Object[][] createSampleEmployeeData() {
        return new Object[][] {
            {"NV001", "Nguyễn Văn A", "Nam", "1990-01-01", "0123456789", "nva@email.com"},
            {"NV002", "Trần Thị B", "Nữ", "1992-02-02", "0987654321", "ttb@email.com"}
        };
    }

    private Object[][] createSamplePermissionData() {
        return new Object[][] {
            {"Q001", "Quản lí cửa hàng"},
            {"Q002", "Quản lí nhân viên"},
            {"Q003", "Quản lí kho"},
            {"Q004", "Quản lí bán hàng"},
            {"Q005", "Quản lí báo cáo"}
        };
    }

    private Object[][] createSampleAccountData() {
        return new Object[][] {
            {"NV001", "admin", "Quản lí", "Đang hoạt động"},
            {"NV002", "user", "Nhân viên", "Đang hoạt động"}
        };
    }
    private void styleTabbedPane() {
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            private final Insets borderInsets = new Insets(0, 0, 0, 0);
            private final Color selectedColor = PRIMARY_COLOR;
            private final int TAB_HEIGHT = 45;
            
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = Color.WHITE;
                lightHighlight = Color.WHITE;
                shadow = Color.WHITE;
                darkShadow = Color.WHITE;
                focus = PRIMARY_COLOR;
            }

            @Override
            protected Insets getContentBorderInsets(int tabPlacement) {
                return borderInsets;
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return TAB_HEIGHT;
            }
            
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                                           int tabIndex, int x, int y, int w, int h, 
                                           boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (!isSelected) {
                    GradientPaint gp = new GradientPaint(
                        x, y, new Color(252, 251, 251),
                        x, y + h, new Color(250, 250, 250));
                    g2d.setPaint(gp);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                        int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                                   
                if (isSelected) {
                    g2d.setColor(selectedColor);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawLine(x, y + h - 2, x + w, y + h - 2);
                } else {
                    g2d.setColor(new Color(240, 240, 240));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawLine(x, y + h - 1, x + w, y + h - 1);
                }
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // Không vẽ border cho content area
            }

            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement,
                                             Rectangle[] rects, int tabIndex,
                                             Rectangle iconRect, Rectangle textRect,
                                             boolean isSelected) {
                // Không vẽ focus indicator
            }
            
            @Override
            protected void layoutLabel(int tabPlacement, FontMetrics metrics,
                                     int tabIndex, String title, Icon icon,
                                     Rectangle tabRect, Rectangle iconRect,
                                     Rectangle textRect, boolean isSelected) {
                super.layoutLabel(tabPlacement, metrics, tabIndex, title, icon, 
                                tabRect, iconRect, textRect, isSelected);
                
                // Căn giữa text theo chiều ngang
                textRect.x = tabRect.x + (tabRect.width - textRect.width) / 2;
                
                // Căn giữa text theo chiều dọc
                textRect.y = tabRect.y + (tabRect.height - textRect.height) / 2 + 2;
            }

            @Override
            protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
                return 0;
            }

            @Override
            protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
                return 0;
            }
        });
        
        tabbedPane.setBorder(new EmptyBorder(10, 15, 0, 15));
    }

    // Event handlers
    private void handleSearch(String searchText, JTable table) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        
        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void handleAdd(String tabTitle) {
        String message = "Thêm mới " + 
            (tabTitle.contains("nhân viên") ? "nhân viên" : 
             tabTitle.contains("quyền") ? "quyền" : "tài khoản");
             
        JOptionPane.showMessageDialog(this, 
            "Chức năng " + message + " sẽ được thực hiện tại đây",
            message,
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleEdit(JTable table, String tabTitle) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một dòng để chỉnh sửa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String message = "Chỉnh sửa " + 
            (tabTitle.contains("nhân viên") ? "nhân viên" : 
             tabTitle.contains("quyền") ? "quyền" : "tài khoản");
             
        JOptionPane.showMessageDialog(this, 
            "Chức năng " + message + " sẽ được thực hiện tại đây",
            message,
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleDelete(JTable table, String tabTitle) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một dòng để xóa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemType = tabTitle.contains("nhân viên") ? "nhân viên" : 
                         tabTitle.contains("quyền") ? "quyền" : "tài khoản";
                         
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa " + itemType + " này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(table.convertRowIndexToModel(selectedRow));
            JOptionPane.showMessageDialog(this,
                "Xóa " + itemType + " thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleExport(String tabTitle) {
        String message = "Xuất Excel cho " + 
            (tabTitle.contains("nhân viên") ? "danh sách nhân viên" : 
             tabTitle.contains("quyền") ? "danh sách quyền" : "danh sách tài khoản");
             
        JOptionPane.showMessageDialog(this, 
            "Chức năng " + message + " sẽ được thực hiện tại đây",
            "Xuất Excel",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleAbout(String tabTitle) {
        String message = "Thông tin về quản lý " + 
            (tabTitle.contains("nhân viên") ? "nhân viên" : 
             tabTitle.contains("quyền") ? "quyền" : "tài khoản");
             
        JOptionPane.showMessageDialog(this, 
            message,
            "Thông tin",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Custom cell renderers
    private class StatusColumnRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                if (status.equals("Đang hoạt động")) {
                    setForeground(new Color(40, 167, 69));
                } else {
                    setForeground(new Color(220, 53, 69));
                }
            }
            
            setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    }

    private class CenterAlignRenderer extends DefaultTableCellRenderer {
        public CenterAlignRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }
    }

}