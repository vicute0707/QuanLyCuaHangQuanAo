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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class QuanLyDonBan extends JPanel {
    // Constants
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 242, 242);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
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
    private JComboBox<String> employeeCombo;
    private JLabel totalRecordsValue;
    private JLabel totalAmountValue;

    public QuanLyDonBan() {
        initializeComponents();
        setupLayout();
        loadSampleData();
    }

    private void initializeComponents() {
        // Initialize labels
        totalRecordsValue = new JLabel("0");
        totalRecordsValue.setFont(HEADER_FONT);
        totalRecordsValue.setForeground(PRIMARY_COLOR);

        totalAmountValue = new JLabel("0 VND");
        totalAmountValue.setFont(HEADER_FONT);
        totalAmountValue.setForeground(PRIMARY_COLOR);

        // Initialize table model with auto-incrementing STT
        String[] columns = {"STT", "Mã đơn bán", "Nhân viên bán", "Thời gian", "Tổng tiền"};
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
        int[] columnWidths = {50, 150, 200, 150, 200};
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
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Mã đơn bán
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Thời gian

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Tổng tiền

        // Add popup menu
        table.setComponentPopupMenu(createTablePopupMenu());
    }

    private void initializeFilterComponents() {
        employeeCombo = new JComboBox<>(new String[]{
            "Tất cả",
            "Nguyễn Văn An",
            "Trần Thị Bình",
            "Lê Hoàng Nam"
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

        JButton addButton = createActionButton("Thêm đơn bán", "/icon/circle-plus.png", true, true);
        addButton.setPreferredSize(new Dimension(160, 38));
        
        JButton deleteButton = createActionButton("Xóa", "/icon/trash.png", true, false);
        JButton aboutButton = createActionButton("About", "/icon/info.png", true, false);
        
        JButton exportButton = createActionButton("Xuất Excel", "/icon/printer.png", true, false);
        exportButton.setPreferredSize(new Dimension(160, 38));

        actionPanel.add(addButton);
        actionPanel.add(deleteButton);
        actionPanel.add(aboutButton);
        actionPanel.add(exportButton);

        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.EAST);
        
        return panel;
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
        filterPanel.setPreferredSize(new Dimension(350, 200));

        filterPanel.add(createFilterTitle());
        filterPanel.add(Box.createVerticalStrut(20));
        
        // Nhân viên
        filterPanel.add(createFilterField("Nhân viên bán hàng", employeeCombo));
        filterPanel.add(Box.createVerticalStrut(15));

        // Thời gian
        filterPanel.add(createDateFilterPanel());
        filterPanel.add(Box.createVerticalStrut(15));

        // Khoảng tiền
        filterPanel.add(createAmountFilterPanel());
        filterPanel.add(Box.createVerticalStrut(25));

        // Buttons
        filterPanel.add(createFilterButtonsPanel());

        return filterPanel;
    }

    private void loadSampleData() {
        Object[][] data = {
            {"1", "DB001", "Nguyễn Văn An", "01/11/2023", "1,500,000 VND"},
            {"2", "DB002", "Trần Thị Bình", "02/11/2023", "2,800,000 VND"},
            {"3", "DB003", "Lê Hoàng Nam", "03/11/2023", "3,200,000 VND"},
            {"4", "DB004", "Nguyễn Văn An", "04/11/2023", "1,900,000 VND"},
            {"5", "DB005", "Trần Thị Bình", "05/11/2023", "2,100,000 VND"},
            {"6", "DB006", "Lê Hoàng Nam", "06/11/2023", "4,500,000 VND"},
            {"7", "DB007", "Nguyễn Văn An", "07/11/2023", "3,700,000 VND"},
            {"8", "DB008", "Trần Thị Bình", "08/11/2023", "2,900,000 VND"},
            {"9", "DB009", "Lê Hoàng Nam", "09/11/2023", "1,800,000 VND"},
            {"10", "DB010", "Nguyễn Văn An", "10/11/2023", "5,200,000 VND"}
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
        updateSummary();
    }

    // Rest of the utility methods (createDateChooserPanel, styleComboBox, etc.) remain the same
    // Only changing the context from "phiếu nhập" to "đơn bán" in messages and tooltips

    private JPopupMenu createTablePopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JMenuItem viewItem = createMenuItem("Xem chi tiết", e -> viewDetail());
        JMenuItem editItem = createMenuItem("Chỉnh sửa", e -> editRecord());
        JMenuItem deleteItem = createMenuItem("Xóa", e -> deleteRecord());
        JMenuItem printItem = createMenuItem("In đơn bán", e -> printRecord());

        popup.add(viewItem);
        popup.add(editItem);
        popup.addSeparator();
        popup.add(deleteItem);
        popup.addSeparator();
        popup.add(printItem);

        return popup;
    }

    // The rest of the methods remain similar to QuanLyNhapHang
    // Just changing context from "phiếu nhập" to "đơn bán" where appropriate
    
    private void viewDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 1).toString();
            JOptionPane.showMessageDialog(this,
                "Xem chi tiết đơn bán " + id,
                "Chi tiết đơn bán",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 1).toString();
            JOptionPane.showMessageDialog(this,
                "Chỉnh sửa đơn bán " + id,
                "Chỉnh sửa đơn bán",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 1).toString();
            int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa đơn bán " + id + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                updateSummary();
                // Cập nhật lại STT sau khi xóa
                updateSTTColumn();
            }
        }
    }

    private void updateSTTColumn() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(String.valueOf(i + 1), i, 0);
        }
    }

    private void printRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 1).toString();
            JOptionPane.showMessageDialog(this,
                "In đơn bán " + id,
                "In đơn bán",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private JPanel createMainPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));
        
        // Tùy chỉnh thanh cuộn
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        
        // Tạo UI tùy chỉnh cho thanh cuộn
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR;
                this.trackColor = new Color(245, 245, 245);
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
        
        horizontalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR;
                this.trackColor = new Color(245, 245, 245);
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

        JLabel totalRecordsLabel = new JLabel("Tổng số đơn: ");
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
        panel.add(Box.createVerticalStrut(8)); // Giảm khoảng cách

        // Tạo panel chứa cả 2 date chooser
        JPanel dateChoosersPanel = new JPanel();
        dateChoosersPanel.setLayout(new BoxLayout(dateChoosersPanel, BoxLayout.Y_AXIS));
        dateChoosersPanel.setBackground(CONTENT_COLOR);

        JPanel fromDatePanel = createDateChooserPanel("Từ ngày:", fromDateChooser);
        JPanel toDatePanel = createDateChooserPanel("Đến ngày:", toDateChooser);

        dateChoosersPanel.add(fromDatePanel);
        dateChoosersPanel.add(Box.createVerticalStrut(5)); // Giảm khoảng cách giữa 2 date chooser
        dateChoosersPanel.add(toDatePanel);

        panel.add(dateChoosersPanel);
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
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Thay đổi layout
        panel.setBackground(CONTENT_COLOR);
        
        JLabel dateLabel = new JLabel(label);
        dateLabel.setFont(new Font(CONTENT_FONT.getFamily(), Font.PLAIN, 12));
        dateLabel.setPreferredSize(new Dimension(70, 35));
        
        panel.add(dateLabel);
        panel.add(chooser);
        
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

    private JMenuItem createMenuItem(String text, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(CONTENT_FONT);
        item.addActionListener(listener);
        return item;
    }

    private void applyFilters() {
        // Thực hiện lọc dữ liệu theo các điều kiện
        tableModel.setRowCount(0); // Xóa dữ liệu hiện tại

        // Lấy giá trị từ các trường lọc
        String selectedEmployee = employeeCombo.getSelectedItem().toString();
        Date fromDate = fromDateChooser.getDate();
        Date toDate = toDateChooser.getDate();
        Number fromAmount = (Number) fromAmountField.getValue();
        Number toAmount = (Number) toAmountField.getValue();
        String searchText = searchField.getText().toLowerCase();

        // Tải lại dữ liệu mẫu (trong thực tế sẽ lấy từ database)
        Object[][] allData = {
            {"1", "DB001", "Nguyễn Văn An", "01/11/2023", "1,500,000 VND"},
            {"2", "DB002", "Trần Thị Bình", "02/11/2023", "2,800,000 VND"},
            // ... thêm dữ liệu mẫu khác
        };

        // Áp dụng bộ lọc
        for (Object[] row : allData) {
            boolean matchFilter = true;

            // Lọc theo nhân viên
            if (!"Tất cả".equals(selectedEmployee) && !row[2].toString().equals(selectedEmployee)) {
                matchFilter = false;
            }

            // Lọc theo thời gian
            // Thêm logic lọc theo thời gian ở đây

            // Lọc theo khoảng tiền
            // Thêm logic lọc theo khoảng tiền ở đây

            // Lọc theo từ khóa tìm kiếm
            if (!searchText.isEmpty()) {
                boolean matchSearch = false;
                for (Object cell : row) {
                    if (cell.toString().toLowerCase().contains(searchText)) {
                        matchSearch = true;
                        break;
                    }
                }
                if (!matchSearch) {
                    matchFilter = false;
                }
            }

            if (matchFilter) {
                tableModel.addRow(row);
            }
        }

        updateSummary();
        JOptionPane.showMessageDialog(this,
            "Đã áp dụng bộ lọc",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetFilters() {
        employeeCombo.setSelectedIndex(0);
        fromDateChooser.setDate(null);
        toDateChooser.setDate(null);
        fromAmountField.setValue(null);
        toAmountField.setValue(null);
        searchField.setText("");
        refreshTable();
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
            case "Nhân viên bán hàng":
                return "Chọn nhân viên bán hàng để lọc đơn bán";
            case "Khoảng tiền (VND)":
                return "Nhập khoảng tiền cần lọc";
            default:
                return null;
        }
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

    private JDateChooser createDateChooser() {
    	JDateChooser chooser = new JDateChooser();
        chooser.setPreferredSize(new Dimension(200, 30)); // Giảm chiều cao xuống 25
        chooser.setMaximumSize(new Dimension(200, 35));  // Thêm maxSize
        chooser.setMinimumSize(new Dimension(200, 30));  // Thêm minSize
        chooser.setFont(CONTENT_FONT);
        chooser.setDateFormatString("dd/MM/yyyy");
        chooser.setBackground(Color.WHITE);
        
        // Tùy chỉnh calendar button và textfield container
        for (Component comp : chooser.getComponents()) {
            if (comp instanceof JButton) {
                JButton calendarButton = (JButton) comp;
                calendarButton.setPreferredSize(new Dimension(20, 23));
                calendarButton.setBackground(Color.WHITE);
                calendarButton.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
                
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
        textField.setPreferredSize(new Dimension(160, 23));
        textField.setBackground(Color.WHITE);
        textField.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        textField.setFont(new Font(CONTENT_FONT.getFamily(), Font.PLAIN, 11)); // Giảm font size
        
        // Set size cho container của textfield
        Component dateEditor = (Component) chooser.getDateEditor();
        if (dateEditor instanceof JComponent) {
            ((JComponent) dateEditor).setPreferredSize(new Dimension(160, 23));
        }
        
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
                if (!comboBox.hasFocus()) {
                    comboBox.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(230, 230, 230), 1),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                    ));
                }
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

    // Bổ sung phương thức xử lý xuất Excel
    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                
                JOptionPane.showMessageDialog(this,
                    "Xuất Excel thành công: " + filePath,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi xuất file Excel: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Bổ sung phương thức xử lý thêm đơn bán mới
    private void addNewOrder() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm đơn bán mới");
        dialog.setModal(true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        // Thêm các components cho form thêm đơn bán
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID đơn bán
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Mã đơn bán:"), gbc);
        
        gbc.gridx = 1;
        JTextField idField = new JTextField(20);
        panel.add(idField, gbc);
        
        // Nhân viên bán
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nhân viên bán:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> employeeField = new JComboBox<>(new String[]{
            "Nguyễn Văn An",
            "Trần Thị Bình",
            "Lê Hoàng Nam"
        });
        panel.add(employeeField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        saveButton.addActionListener(e -> {
            // Xử lý lưu đơn bán
            dialog.dispose();
            refreshTable();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Thêm phương thức để xử lý double click vào dòng trong bảng
    private void addTableDoubleClickHandler() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewDetail();
                }
            }
        });
    
                }


    // Các phương thức tiện ích khác giữ nguyên như trong QuanLyNhapHang
    // Chỉ thay đổi context từ "phiếu nhập" sang "đơn bán" trong các message
}