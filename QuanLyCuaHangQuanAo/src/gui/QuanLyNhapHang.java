package gui;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class QuanLyNhapHang extends JPanel {
    // Constants
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 242, 242);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14);
    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
    private static final Font TITLE_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16);

    // Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JFormattedTextField fromAmountField;
    private JFormattedTextField toAmountField;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JComboBox<String> supplierCombo;
    private JComboBox<String> employeeCombo;
    private JLabel totalRecordsValue;
    private JLabel totalAmountValue;

    public QuanLyNhapHang() {
        initializeComponents();
        setupLayout();
        loadSampleData();
    }

    private void initializeComponents() {
        // Initialize labels first
        totalRecordsValue = new JLabel("0");
        totalRecordsValue.setFont(HEADER_FONT);
        totalRecordsValue.setForeground(PRIMARY_COLOR);

        totalAmountValue = new JLabel("0 VND");
        totalAmountValue.setFont(HEADER_FONT);
        totalAmountValue.setForeground(PRIMARY_COLOR);

        // Initialize table model
        String[] columns = {"Mã phiếu", "Nhà cung cấp", "Nhân viên nhập hàng", "Thời gian", "Tổng tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Initialize table
        table = new JTable(tableModel);
        setupTable();

        // Initialize search components
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 35));
        searchField.setFont(CONTENT_FONT);

        // Initialize filter components
        initializeFilterComponents();
    }
    private void setupTable() {
        table.setFont(CONTENT_FONT);
        table.setRowHeight(40);
        table.setGridColor(new Color(245, 245, 245));
        table.setSelectionBackground(HOVER_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        // Set column widths
        int[] columnWidths = {100, 250, 200, 150, 200};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Setup header
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));

        // Setup cell renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        // Add popup menu
        table.setComponentPopupMenu(createTablePopupMenu());
    }

    private void initializeFilterComponents() {
        // Initialize combo boxes
        supplierCombo = new JComboBox<>(new String[]{
            "Tất cả",
            "Xưởng may Đại Nam",
            "Xưởng may Hoàng Gia",
            "Công ty TNHH May Việt Tiến"
        });
        styleComboBox(supplierCombo);

        employeeCombo = new JComboBox<>(new String[]{
            "Tất cả",
            "Nguyễn Thị Tường Vi",
            "Trần Văn Nam",
            "Lê Thị Hoa"
        });
        styleComboBox(employeeCombo);

        // Initialize date choosers
        fromDateChooser = createDateChooser();
        toDateChooser = createDateChooser();

        // Initialize amount fields
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(true);
        fromAmountField = new JFormattedTextField(format);
        toAmountField = new JFormattedTextField(format);
        styleFormattedTextField(fromAmountField);
        styleFormattedTextField(toAmountField);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Add main panels
        add(createTopPanel(), BorderLayout.NORTH);
        add(createFilterPanel(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);
    }
    private JPanel createTopPanel() {

        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBackground(Color.WHITE);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(searchField);
        searchPanel.add(createSearchButton());

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton addButton = createActionButton("Thêm phiếu nhập", "/icon/circle-plus.png", true, true);
        addButton.setPreferredSize(new Dimension(160, 38));
        
        // Sửa lại phần xử lý sự kiện cho nút thêm
        addButton.addActionListener(e -> {
            // Tìm panel cha chứa nội dung chính
            Container mainContent = QuanLyNhapHang.this.getParent();
            
            // Xóa nội dung hiện tại của panel chính
            mainContent.removeAll();
            
            // Thêm panel ThemDonNhap mới vào panel chính với BorderLayout.CENTER
            ThemDonNhap addPanel = new ThemDonNhap();
            mainContent.add(addPanel, BorderLayout.CENTER);
            
            // Cập nhật và vẽ lại giao diện
            mainContent.revalidate();
            mainContent.repaint();
        });
        
        JButton deleteButton = createActionButton("Xóa", "/icon/trash.png", true, false);
        JButton aboutButton = createActionButton("About", "/icon/info.png", true, false);
        JButton exportButton = createActionButton("Xuất Excel", "/icon/printer.png", true, false);
        exportButton.setPreferredSize(new Dimension(160, 38));

        actionPanel.add(addButton);
        actionPanel.add(deleteButton);
        deleteButton.addActionListener(e -> {
            int selectedRows[] = table.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phiếu nhập cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int option = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa " + selectedRows.length + " phiếu nhập đã chọn?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                // Xóa từ dưới lên trên để tránh lỗi index
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    tableModel.removeRow(selectedRows[i]);
                }
                updateSummary();
                JOptionPane.showMessageDialog(
                    this,
                    "Đã xóa thành công " + selectedRows.length + " phiếu nhập!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        actionPanel.add(aboutButton);
        aboutButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phiếu nhập cần xem chi tiết!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Lấy thông tin phiếu nhập từ table
            String maPhieu = table.getValueAt(selectedRow, 0).toString();
            String nhaCungCap = table.getValueAt(selectedRow, 1).toString();
            String nhanVien = table.getValueAt(selectedRow, 2).toString();
            String thoiGian = table.getValueAt(selectedRow, 3).toString();
            String tongTien = table.getValueAt(selectedRow, 4).toString();

            // Tạo dialog chi tiết
            JDialog detailDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết phiếu nhập", true);
            detailDialog.setLayout(new BorderLayout());
            
            // Panel chứa nội dung
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

            // Panel thông tin phiếu
            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 15);

            // Thêm thông tin chung của phiếu
            addDetailField(infoPanel, "Mã phiếu:", maPhieu, gbc, 0);
            addDetailField(infoPanel, "Nhà cung cấp:", nhaCungCap, gbc, 1);
            addDetailField(infoPanel, "Nhân viên:", nhanVien, gbc, 2);
            addDetailField(infoPanel, "Thời gian:", thoiGian, gbc, 3);
            addDetailField(infoPanel, "Tổng tiền:", tongTien, gbc, 4);

            // Tạo bảng chi tiết sản phẩm
            String[] columns = {"Mã SP", "Tên sản phẩm", "Phân loại", "Đơn giá", "Số lượng", "Thành tiền"};
            DefaultTableModel detailModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable detailTable = new JTable(detailModel);
            setupDetailTable(detailTable);

            // Thêm dữ liệu mẫu cho bảng chi tiết
            // (Thực tế sẽ lấy từ database)
            Object[][] sampleData = {
                {"SP001", "Áo sơ mi nữ", "Trắng - M", "250,000", "10", "2,500,000"},
                {"SP002", "Váy đầm dự tiệc", "Đen - L", "450,000", "5", "2,250,000"},
                {"SP003", "Quần jean nữ", "Xanh - 29", "320,000", "8", "2,560,000"}
            };
            for (Object[] row : sampleData) {
                detailModel.addRow(row);
            }

            // Tạo scroll pane cho bảng
            JScrollPane scrollPane = new JScrollPane(detailTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            
            // Style scroll pane
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = PRIMARY_COLOR;
                    this.trackColor = new Color(245, 245, 245);
                }

               
            });

            // Thêm các components vào panel chính
            contentPanel.add(infoPanel);
            contentPanel.add(Box.createVerticalStrut(20));
            
            // Label cho bảng chi tiết
            JLabel detailLabel = new JLabel("Chi tiết sản phẩm");
            detailLabel.setFont(HEADER_FONT);
            
            detailLabel.setForeground(PRIMARY_COLOR);
            contentPanel.add(detailLabel);
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(scrollPane);

            // Panel chứa các nút
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setBackground(Color.WHITE);

            // Nút in phiếu
            JButton printButton = new JButton("In phiếu");
            styleButton(printButton, true);
            printButton.addActionListener(evt -> {
                // Xử lý in phiếu
                JOptionPane.showMessageDialog(detailDialog, 
                    "Đang in phiếu " + maPhieu,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            });

            // Nút đóng
            JButton closeButton = new JButton("Đóng");
            styleButton(closeButton, false);
            closeButton.addActionListener(evt -> detailDialog.dispose());

            buttonPanel.add(printButton);
            buttonPanel.add(closeButton);

            // Thêm vào dialog
            detailDialog.add(contentPanel, BorderLayout.CENTER);
            detailDialog.add(buttonPanel, BorderLayout.SOUTH);

            // Cài đặt dialog
            detailDialog.setSize(800, 600);
            detailDialog.setLocationRelativeTo(this);
            detailDialog.setVisible(true);
        });


        actionPanel.add(exportButton);

        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.EAST);
        
        return panel;
    }

    private void addDetailField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        
        // Label
        gbc.gridx = 0;
        JLabel lblField = new JLabel(label);
        lblField.setFont(CONTENT_FONT);
        panel.add(lblField, gbc);
        
        // Value
        gbc.gridx = 1;
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(CONTENT_FONT);
        panel.add(lblValue, gbc);
    }

    private void setupDetailTable(JTable table) {
        table.setFont(CONTENT_FONT);
        table.setRowHeight(35);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        // Setup header
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Setup cell renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        // Áp dụng cell renderer
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã SP
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Đơn giá
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Số lượng
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);  // Thành tiền
    }

    private void styleButton(JButton button, boolean isPrimary) {
        button.setFont(CONTENT_FONT);
        button.setPreferredSize(new Dimension(100, 35));
        
        if (isPrimary) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setBorder(new LineBorder(PRIMARY_COLOR));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(new LineBorder(new Color(230, 230, 230)));
        }
        
        button.setFocusPainted(false);

        // Thêm hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isPrimary) {
                    button.setBackground(PRIMARY_COLOR.darker());
                } else {
                    button.setBackground(HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isPrimary) {
                    button.setBackground(PRIMARY_COLOR);
                } else {
                    button.setBackground(Color.WHITE);
                }
            }
        });
    }

	private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(CONTENT_COLOR);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                new LineBorder(PRIMARY_COLOR, 0, true),
                new EmptyBorder(20, 20, 20, 20)
            )
        ));
        filterPanel.setPreferredSize(new Dimension(350, 0));

        // Tiêu đề panel
        filterPanel.add(createFilterTitle());
        

        filterPanel.add(Box.createVerticalStrut(20));

        // Nhà cung cấp
        filterPanel.add(createFilterField("Nhà cung cấp", supplierCombo));
        filterPanel.add(Box.createVerticalStrut(15));
        
        // Nhân viên
        filterPanel.add(createFilterField("Nhân viên tiếp nhận", employeeCombo));
        filterPanel.add(Box.createVerticalStrut(15));

        // Thời gian
        filterPanel.add(createDateFilterPanel());
        filterPanel.add(Box.createVerticalStrut(15));

        // Khoảng tiền
        filterPanel.add(createAmountFilterPanel());
        filterPanel.add(Box.createVerticalStrut(25));
        filterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Buttons
        filterPanel.add(createFilterButtonsPanel());

        return filterPanel;
    }

    private JPanel createMainPanel() {
    	JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));
        
        // Tùy chỉnh thanh cuộn dọc
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR; // Màu hồng cho phần thumb
                this.trackColor = new Color(245, 245, 245); // Màu track
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
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        // Tùy chỉnh thanh cuộn ngang
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR; // Màu hồng cho phần thumb
                this.trackColor = new Color(245, 245, 245); // Màu track
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
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        // Add components
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        JLabel totalRecordsLabel = new JLabel("Tổng số phiếu: ");
        totalRecordsLabel.setFont(CONTENT_FONT);

        JLabel totalAmountLabel = new JLabel("Tổng tiền: ");
        totalAmountLabel.setFont(CONTENT_FONT);

        panel.add(totalRecordsLabel);
        panel.add(totalRecordsValue);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(totalAmountLabel);
        panel.add(totalAmountValue);

        return panel;
    }

    private JLabel createFilterTitle() {
        JLabel titleLabel = new JLabel("Lọc kết quả");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titleLabel;
    }

    private JPanel createFilterField(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CONTENT_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(CONTENT_FONT);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String tooltipText = getTooltipText(label);
        if (tooltipText != null) {
            component.setToolTipText(tooltipText);
        }

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(component);

        return panel;
    }

    private JPanel createDateFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CONTENT_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Thời gian");
        titleLabel.setFont(CONTENT_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));

        panel.add(createDateChooserPanel("Từ ngày:", fromDateChooser));
        panel.add(Box.createVerticalStrut(12));
        panel.add(createDateChooserPanel("Đến ngày:", toDateChooser));

        return panel;
    }

    private JPanel createAmountFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CONTENT_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Khoảng tiền (VND)");
        titleLabel.setFont(CONTENT_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));

        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        fieldsPanel.setBackground(CONTENT_COLOR);

        fromAmountField.setPreferredSize(new Dimension(120, 35));
        toAmountField.setPreferredSize(new Dimension(120, 35));

        fieldsPanel.add(fromAmountField);
        fieldsPanel.add(new JLabel("—"));
        fieldsPanel.add(toAmountField);

        panel.add(fieldsPanel);
        return panel;
    }
    private JPanel createFilterButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(CONTENT_COLOR);

        JButton applyButton = createFilterButton("Áp dụng", true);
        JButton resetButton = createFilterButton("Đặt lại", false);

        applyButton.setPreferredSize(new Dimension(70, 38));
        resetButton.setPreferredSize(new Dimension(70, 38));

        panel.add(applyButton);
        panel.add(resetButton);

        return panel;
    }

    private JButton createFilterButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(CONTENT_FONT);
        
        if (isPrimary) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        }
        
        button.setFocusPainted(false);
        addButtonHoverEffect(button);
        
        if (isPrimary) {
            button.addActionListener(e -> applyFilters());
        } else {
            button.addActionListener(e -> resetFilters());
        }
        
        return button;
    }

    private JPanel createDateChooserPanel(String label, JDateChooser chooser) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(CONTENT_COLOR);
        
        JLabel dateLabel = new JLabel(label);
        dateLabel.setFont(CONTENT_FONT);
        dateLabel.setPreferredSize(new Dimension(65, 35));
        panel.add(dateLabel, BorderLayout.WEST);
        panel.add(chooser, BorderLayout.CENTER);
        
        return panel;
    }

    private JButton createSearchButton() {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource("/icon/search.png")));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(38, 38));
        addButtonHoverEffect(button);
        button.addActionListener(e -> applyFilters());
        return button;
    }

    private JButton createActionButton(String text, String iconPath, boolean isRounded, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(CONTENT_FONT);
        button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        button.setFocusPainted(false);

        if (isPrimary) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
        }

        if (isRounded) {
            button.setBorder(new LineBorder(isPrimary ? PRIMARY_COLOR : new Color(230, 230, 230), 1, true));
        } else {
            button.setBorder(BorderFactory.createEmptyBorder());
        }

        button.setPreferredSize(new Dimension(130, 38));
        addButtonHoverEffect(button);
        
        return button;
    }

    private JDateChooser createDateChooser() {
        JDateChooser chooser = new JDateChooser();
        chooser.setPreferredSize(new Dimension(100, 30));
        chooser.setFont(CONTENT_FONT);
        chooser.setDateFormatString("dd/MM/yyyy");
        chooser.setBackground(Color.WHITE);
        
        // Tùy chỉnh calendar button
        for (Component comp : chooser.getComponents()) {
            if (comp instanceof JButton) {
                JButton calendarButton = (JButton) comp;
                calendarButton.setBackground(Color.WHITE);
                calendarButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                
                calendarButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        calendarButton.setBackground(HOVER_COLOR);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        calendarButton.setBackground(Color.WHITE);
                    }
                });
            }
        }
        
        // Tùy chỉnh text field
        JTextField textField = ((JTextField)chooser.getDateEditor().getUiComponent());
        textField.setBackground(Color.WHITE);
        textField.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        textField.setPreferredSize(new Dimension(100,30));
        textField.setFont(CONTENT_FONT);
        
        // Thêm hiệu ứng hover và focus
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (textField.isEnabled()) {
                    textField.setBorder(new LineBorder(PRIMARY_COLOR, 1));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!textField.hasFocus()) {
                    textField.setBorder(new LineBorder(new Color(230, 230, 230), 1));
                }
            }
        });
        
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(new LineBorder(PRIMARY_COLOR, 1));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(new LineBorder(new Color(230, 230, 230), 1));
            }
        });
        
        return chooser;
    }
    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setPreferredSize(new Dimension(300, 35));
        comboBox.setMaximumSize(new Dimension(300, 35));
        comboBox.setFont(CONTENT_FONT);
        comboBox.setBackground(Color.WHITE);
        
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return button;
            }
        });
        
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(CONTENT_FONT);
                setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                
                if (isSelected) {
                    setBackground(HOVER_COLOR);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });

        // Add hover effect
        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (comboBox.isEnabled()) {
                    comboBox.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY_COLOR, 1),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                    ));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                comboBox.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(0, 5, 0, 5)
                ));
            }
        });
    }

    private void styleFormattedTextField(JFormattedTextField textField) {
        textField.setPreferredSize(new Dimension(70, 30));
        textField.setFont(CONTENT_FONT);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));

        // Add hover and focus effects
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (textField.isEnabled()) {
                    textField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY_COLOR, 1),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
                    ));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!textField.hasFocus()) {
                    textField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(230, 230, 230), 1),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
                    ));
                }
            }
        });

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 1),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)
                ));
            }
        });
    }

    private void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.getBackground().equals(PRIMARY_COLOR)) {
                    button.setBackground(PRIMARY_COLOR.darker());
                } else {
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
    }
 // Table popup menu methods
    private JPopupMenu createTablePopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JMenuItem viewItem = createMenuItem("Xem chi tiết", e -> viewDetail());
        JMenuItem editItem = createMenuItem("Chỉnh sửa", e -> editRecord());
        JMenuItem deleteItem = createMenuItem("Xóa", e -> deleteRecord());
        JMenuItem printItem = createMenuItem("In phiếu nhập", e -> printRecord());

        popup.add(viewItem);
        popup.add(editItem);
        popup.addSeparator();
        popup.add(deleteItem);
        popup.addSeparator();
        popup.add(printItem);

        return popup;
    }

    private JMenuItem createMenuItem(String text, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.setIcon(new ImageIcon(getClass().getResource("/icon/info.png")));
        item.setFont(CONTENT_FONT);
        item.addActionListener(listener);
        return item;
    }

    // Action methods
    private void viewDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 0).toString();
            JOptionPane.showMessageDialog(this,
                "Xem chi tiết phiếu nhập " + id,
                "Chi tiết phiếu nhập",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 0).toString();
            JOptionPane.showMessageDialog(this,
                "Chỉnh sửa phiếu nhập " + id,
                "Chỉnh sửa phiếu nhập",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 0).toString();
            int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa phiếu nhập " + id + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                updateSummary();
            }
        }
    }

    private void printRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 0).toString();
            JOptionPane.showMessageDialog(this,
                "In phiếu nhập " + id,
                "In phiếu nhập",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void applyFilters() {
        JOptionPane.showMessageDialog(this,
            "Đang áp dụng bộ lọc...",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }
    private void resetFilters() {
        supplierCombo.setSelectedIndex(0);
        employeeCombo.setSelectedIndex(0);
        fromDateChooser.setDate(null);
        toDateChooser.setDate(null);
        fromAmountField.setValue(null);
        toAmountField.setValue(null);
        searchField.setText("");
        refreshTable();
    }

    private void loadSampleData() {
        Object[][] data = {
            {"P01", "Xưởng may Đại Nam", "Nguyễn Thị Tường Vi", "01/11/2023", "15,000,000 VND"},
            {"P02", "Xưởng may Hoàng Gia", "Trần Văn Nam", "02/11/2023", "12,500,000 VND"},
            {"P03", "Công ty TNHH May Việt Tiến", "Lê Thị Hoa", "03/11/2023", "18,750,000 VND"},
            {"P04", "Xưởng may Đại Nam", "Nguyễn Thị Tường Vi", "05/11/2023", "22,000,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P05", "Xưởng may Hoàng Gia", "Trần Văn Nam", "07/11/2023", "9,800,000 VND"},
            {"P06", "Công ty TNHH May Việt Tiến", "Lê Thị Hoa", "10/11/2023", "16,300,000 VND"}
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
        updateSummary();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        loadSampleData();
    }

    private void updateSummary() {
        double totalAmount = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String amountStr = tableModel.getValueAt(i, 4).toString()
                .replace(" VND", "")
                .replace(",", "");
            totalAmount += Double.parseDouble(amountStr);
        }

        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        String formattedAmount = currencyFormat.format(totalAmount) + " VND";

        totalRecordsValue.setText(String.valueOf(tableModel.getRowCount()));
        totalAmountValue.setText(formattedAmount);
    }

    private String getTooltipText(String label) {
        switch (label) {
            case "Nhà cung cấp":
                return "Chọn nhà cung cấp để lọc phiếu nhập";
            case "Nhân viên tiếp nhận":
                return "Chọn nhân viên tiếp nhận để lọc phiếu nhập";
            case "Khoảng tiền (VND)":
                return "Nhập khoảng tiền cần lọc";
            default:
                return null;
        }
    }
    
}