package gui;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;

import component.CreateDateChooser;
import component.ShowSuccessDialog;
import dialog.ChiTietPhieuBan;
import dialog.ChiTietPhieuNhap;
import style.CreateActionButton;
import style.CreateFilter;
import style.CreateRoundedButton;
import style.CustomScrollBarUI;
import style.StyleComboBox;

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

public class QuanLyDonBan extends JPanel {
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
    private JComboBox<String> employeeCombo;
    private JLabel totalRecordsValue;
    private JLabel totalAmountValue;
    CreateFilter createFilter ;
    public QuanLyDonBan() {
    	createFilter = new CreateFilter();
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

    }

    private void initializeFilterComponents() {
        employeeCombo = new JComboBox<>(new String[]{
            "Tất cả",
            "Nguyễn Văn An",
            "Trần Thị Bình",
            "Lê Hoàng Nam"
        });
        StyleComboBox styleComboBox = new StyleComboBox();
        styleComboBox.styleComboBox(employeeCombo);

        // Initialize date choosers
        CreateDateChooser dateStyle = new CreateDateChooser();
        fromDateChooser = dateStyle.createDateChooser();
        toDateChooser = dateStyle.createDateChooser();

        // Initialize amount fields
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(true);
        fromAmountField = new JFormattedTextField(format);
        toAmountField = new JFormattedTextField(format);
        createFilter.styleFormattedTextField(fromAmountField);
        createFilter.styleFormattedTextField(toAmountField);
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
        
        searchPanel.add(createFilter.createSearchButton());

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(Color.WHITE);
        CreateActionButton createActionButton = new CreateActionButton();
        JButton addButton = createActionButton.createActionButton("Thêm đơn bán", "/icon/circle-plus.png", true, true);
        addButton.setPreferredSize(new Dimension(160, 38));
        
        JButton deleteButton = createActionButton.createActionButton("Xóa", "/icon/trash.png", true, false);
        JButton aboutButton = createActionButton.createActionButton("About", "/icon/info.png", true, false);
        
        JButton exportButton = createActionButton.createActionButton("Xuất Excel", "/icon/printer.png", true, false);
        exportButton.setPreferredSize(new Dimension(160, 38));

        actionPanel.add(addButton);
        addButton.addActionListener(e -> {
            // Tìm panel cha chứa nội dung chính
            Container mainContent = QuanLyDonBan.this.getParent();
            
            // Xóa nội dung hiện tại của panel chính
            mainContent.removeAll();
            
            // Thêm panel ThemDonNhap mới vào panel chính với BorderLayout.CENTER
            ThemDonBan addPanel = new ThemDonBan();
            mainContent.add(addPanel, BorderLayout.CENTER);
            
            // Cập nhật và vẽ lại giao diện
            mainContent.revalidate();
            mainContent.repaint();
        });
        actionPanel.add(deleteButton);
        deleteButton.addActionListener(e -> {deleteRecord();});
        actionPanel.add(aboutButton);
        ChiTietPhieuBan chiTietPhieuBan = new ChiTietPhieuBan();
        aboutButton.addActionListener(e->{chiTietPhieuBan.viewDetail(table);});
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
        filterPanel.add(createFilter.createFilterTitle());
        filterPanel.add(Box.createVerticalStrut(20));
        // Nhân viên
        filterPanel.add(createFilter.createFilterField("Nhân viên bán hàng", employeeCombo));
        filterPanel.add(Box.createVerticalStrut(15));
        // Thời gian
        filterPanel.add(createDateFilterPanel());
        filterPanel.add(Box.createVerticalStrut(15));
        // Khoảng tiền
        filterPanel.add(createAmountFilterPanel());
        filterPanel.add(Box.createVerticalStrut(25));
        // Buttons
        filterPanel.add(createFilter.createFilterButtonsPanel());
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

    private void updateSTTColumn() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(String.valueOf(i + 1), i, 0);
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
        CustomScrollBarUI customScrollBarUI = new CustomScrollBarUI();	
        // Tạo UI tùy chỉnh cho thanh cuộn
        verticalScrollBar.setUI(customScrollBarUI);
        
        horizontalScrollBar.setUI(customScrollBarUI);

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
        JPanel fromDatePanel = createFilter.createDateChooserPanel("Từ ngày:", fromDateChooser);
        JPanel toDatePanel = createFilter.createDateChooserPanel("Đến ngày:", toDateChooser);
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
                	ChiTietPhieuBan chiTietPhieuBan = new ChiTietPhieuBan();
                	chiTietPhieuBan.viewDetail(table);
                }
            }
        });}
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String maDonBan = table.getValueAt(selectedRow, 1).toString();
            
            // Tạo custom dialog xác nhận
            JDialog confirmDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Xác nhận xóa", true);
            confirmDialog.setLayout(new BorderLayout());
            confirmDialog.setBackground(Color.WHITE);
            
            // Panel chứa nội dung
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // Icon cảnh báo
            JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/circle-alert.png")));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Message
            JLabel messageLabel = new JLabel("Bạn có chắc muốn xóa đơn bán " + maDonBan + "?");
            messageLabel.setFont(new Font(CONTENT_FONT.getFamily(), Font.BOLD, 14));
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel subMessageLabel = new JLabel("Hành động này không thể hoàn tác!");
            subMessageLabel.setFont(CONTENT_FONT);
            subMessageLabel.setForeground(Color.RED);
            subMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Panel chứa buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton deleteButton = createFilter.createDetailButton("Xóa", null, true);
            JButton cancelButton = createFilter.createDetailButton("Hủy", null, false);
            
            // Thêm components vào panel
            contentPanel.add(iconLabel);
            contentPanel.add(Box.createVerticalStrut(15));
            contentPanel.add(messageLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(subMessageLabel);
            contentPanel.add(Box.createVerticalStrut(20));
            
            buttonPanel.add(deleteButton);
            buttonPanel.add(cancelButton);
            
            // Xử lý sự kiện buttons
            deleteButton.addActionListener(e -> {
                tableModel.removeRow(selectedRow);
                updateSTTColumn();
                updateSummary();
                confirmDialog.dispose();
                
                // Hiển thị thông báo thành công
                ShowSuccessDialog showSuccessDialog = new ShowSuccessDialog();
                showSuccessDialog.showSuccessDialog("Đã xóa đơn bán " + maDonBan + " thành công!");
            });
            
            cancelButton.addActionListener(e -> confirmDialog.dispose());
            
            // Thêm panels vào dialog
            confirmDialog.add(contentPanel, BorderLayout.CENTER);
            confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            // Hiển thị dialog
            confirmDialog.pack();
            confirmDialog.setLocationRelativeTo(this);
            confirmDialog.setVisible(true);
        }
    }
}