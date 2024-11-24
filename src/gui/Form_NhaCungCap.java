package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;


import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


import dao.Dao_NhaCungCap;
import entity.NhaCC;

public class Form_NhaCungCap extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 192, 203);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);

    private int selectedRow = -1;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private Dao_NhaCungCap daoNCC = new Dao_NhaCungCap();
 
    public Form_NhaCungCap() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Thêm các components
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(20, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Left components with search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(Color.WHITE);

        // Combo box "Tất cả"
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Theo Mã", "Theo Tên", "Theo SĐT", "Theo Địa Chỉ", "Theo Email"});
        filterCombo.setPreferredSize(new Dimension(120, 35));
        filterCombo.setFont(CONTENT_FONT);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.setFont(CONTENT_FONT);

        JButton searchButton = createRoundedButton("", "/icon/search.png", false);
        searchButton.addActionListener(e -> {
            String selectedFilter = (String) filterCombo.getSelectedItem();
            String keyword = searchField.getText().trim();

            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Dao_NhaCungCap daoNhaCungCap = new Dao_NhaCungCap();
            ArrayList<NhaCC> results = new ArrayList<>();

            switch (selectedFilter) {
                case "Theo Mã":
                    results = daoNhaCungCap.timNhaCungCapTheoMa(keyword);
                    break;
                case "Theo Tên":
                    results = daoNhaCungCap.timNhaCungCapTheoTen(keyword);
                    break;
                case "Theo SĐT":
                    results = daoNhaCungCap.timNhaCungCapTheoSoDienThoai(keyword);
                    break;
                case "Theo Địa Chỉ":
                    results = daoNhaCungCap.timNhaCungTheoDiaCi(keyword);
                    break;
                
                case "Theo Email":
                    results = daoNhaCungCap.timNhaCungCapTheoEmail(keyword);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Lựa chọn tìm kiếm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            // Kiểm tra nếu danh sách rỗng
            if (results == null || results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }else {
            	TimNhaCungCap dialog = new TimNhaCungCap();
                dialog.hienThiKetQuaTimkiem(results);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
            

            
        });



        searchPanel.add(filterCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Right components with buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionsPanel.setBackground(Color.WHITE);

        JButton addButton = createRoundedButton("Thêm NCC", "/icon/circle-plus.png", true);
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(160, 38));

        JButton editButton = createRoundedButton("Sửa", "/icon/pencil.png", true);
        JButton deleteButton = createRoundedButton("Xóa", "/icon/trash.png", true);
        JButton aboutButton = createRoundedButton("Giới thiệu", "/icon/info.png", true);
        JButton exportButton = createRoundedButton("Xuất Excel", "/icon/printer.png", true);
        exportButton.setPreferredSize(new Dimension(160, 38));

        // Add action listeners
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSupplier());
        aboutButton.addActionListener(e -> showAboutDialog());
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file"); // Tiêu đề hộp thoại
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Chỉ chọn file

            // Đặt tên file mặc định và phần mở rộng
            fileChooser.setSelectedFile(new File("DanhSachNhaCC.xlsx"));

            // Mở hộp thoại chọn file
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Lấy đường dẫn file được chọn
                File fileToSave = fileChooser.getSelectedFile();

                // Kiểm tra và thêm phần mở rộng .html nếu người dùng không nhập
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                // Gọi phương thức xuất file
                exportToHTMLWithFont(table, filePath);
            }
        });

        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        actionsPanel.add(aboutButton);
        actionsPanel.add(exportButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionsPanel, BorderLayout.EAST);

        return topPanel;
    }

    
    public void exportToHTMLWithFont(JTable table, String filePath) {
        try (FileWriter htmlWriter = new FileWriter(filePath)) {
            // Bắt đầu file HTML và thêm CSS cho kiểu chữ
            htmlWriter.write("<html><head><meta charset='UTF-8'>\n");
            htmlWriter.write("<style>\n");
            htmlWriter.write("body { font-family: 'Times New Roman', serif; }\n"); // Thiết lập font Times New Roman
            htmlWriter.write("table { border-collapse: collapse; width: 100%; }\n");
            htmlWriter.write("th, td { border: 0.5px solid black; padding: 16px; text-align: left; }\n");
            htmlWriter.write("th { background-color: #f2f2f2; }\n");
            htmlWriter.write("</style>\n</head><body>\n");

            // Bắt đầu bảng
            htmlWriter.write("<table>\n");

            // Lấy mô hình bảng
            TableModel model = table.getModel();

            // Tạo tiêu đề bảng (header)
            htmlWriter.write("<tr>\n");
            for (int col = 0; col < model.getColumnCount(); col++) {
                htmlWriter.write("<th>" + model.getColumnName(col) + "</th>");
            }
            htmlWriter.write("</tr>\n");

            // Ghi dữ liệu từng hàng
            for (int row = 0; row < model.getRowCount(); row++) {
                htmlWriter.write("<tr>\n");
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    if (value != null) {
                        htmlWriter.write("<td>" + value.toString() + "</td>");
                    } else {
                        htmlWriter.write("<td></td>");
                    }
                }
                htmlWriter.write("</tr>\n");
            }

            // Kết thúc bảng và file HTML
            htmlWriter.write("</table>\n</body></html>");

            JOptionPane.showMessageDialog(null, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
   
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại"};
       
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(CONTENT_FONT);
        table.setRowHeight(40);
        table.setGridColor(new Color(245, 245, 245));
        table.setSelectionBackground(HOVER_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        // Căn giữa cho cột mã và số điện thoại
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Thiết lập renderer cho từng cột
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(centerRenderer); // Mã NCC
        columnModel.getColumn(4).setCellRenderer(centerRenderer); // Số điện thoại
        
        // Điều chỉnh độ rộng cột
        columnModel.getColumn(0).setPreferredWidth(80);  // Mã NCC
        columnModel.getColumn(1).setPreferredWidth(200); // Tên
        columnModel.getColumn(2).setPreferredWidth(300); // Địa chỉ
        columnModel.getColumn(3).setPreferredWidth(200); // Email
        columnModel.getColumn(4).setPreferredWidth(120); // SĐT

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Add selection listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {	
                selectedRow = table.getSelectedRow();
                searchField.setText((String) tableModel.getValueAt(selectedRow, 0));
                
            }
        });

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        // Add sample data
        addSampleData();

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void showAddDialog() {
        // Tạo dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm nhà cung cấp", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);

        // Panel chứa form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Thêm các trường nhập liệu
        JTextField nameField = createFormField("Tên nhà cung cấp:", formPanel, gbc, 0);
        JTextField addressField = createFormField("Địa chỉ:", formPanel, gbc, 1);
        JTextField emailField = createFormField("Email:", formPanel, gbc, 2);
        JTextField phoneField = createFormField("Số điện thoại:", formPanel, gbc, 3);

        // Panel chứa buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveButton = new JButton("Lưu");
        saveButton.setBackground(PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            // Tạo mã nhà cung cấp mới
            String maNhaCC = "NCC" + String.format("%03d", tableModel.getRowCount() + 1);
            
            // Lấy thông tin từ các trường nhập liệu
            String tenNcc = nameField.getText();
            String diaChi = addressField.getText();
            String email = emailField.getText();
            String sdt = phoneField.getText();
            
            // Tạo đối tượng NhaCC với các thông tin đã lấy
            NhaCC nhacc = new NhaCC(maNhaCC, tenNcc, diaChi, email, sdt);
            
            // Khởi tạo Dao_NhaCungCap và gọi phương thức insert
            Dao_NhaCungCap daoNCC = new Dao_NhaCungCap();
            if (daoNCC.insertNhaCungCap(nhacc)) {
                // Nếu thêm thành công vào cơ sở dữ liệu
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
                
                // Cập nhật lại dữ liệu vào bảng
                Object[] rowData = {
                    maNhaCC,  // Mã nhà cung cấp
                    tenNcc,   // Tên nhà cung cấp
                    diaChi,   // Địa chỉ nhà cung cấp
                    email,    // Email nhà cung cấp
                    sdt       // Số điện thoại nhà cung cấp
                };
                
                tableModel.addRow(rowData);  // Thêm dòng mới vào table
                
            } else {
                // Nếu thêm thất bại
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thất bại!");
            }
            
            // Đóng dialog sau khi hoàn tất
            dialog.dispose();
        });


        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JTextField createFormField(String label, JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        
        // Label
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);

        // TextField
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        panel.add(field, gbc);

        return field;
    }

    private void showEditDialog() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một nhà cung cấp để chỉnh sửa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo dialog tương tự như add dialog nhưng fill sẵn dữ liệu
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chỉnh sửa nhà cung cấp", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Tạo các trường và điền dữ liệu hiện tại
        JTextField nameField = createFormField("Tên nhà cung cấp:", formPanel, gbc, 0);
        nameField.setText((String) table.getValueAt(selectedRow, 1));
        
        JTextField addressField = createFormField("Địa chỉ:", formPanel, gbc, 1);
        addressField.setText((String) table.getValueAt(selectedRow, 2));
        
        JTextField emailField = createFormField("Email:", formPanel, gbc, 2);
        emailField.setText((String) table.getValueAt(selectedRow, 3));
        
        JTextField phoneField = createFormField("Số điện thoại:", formPanel, gbc, 3);
        phoneField.setText((String) table.getValueAt(selectedRow, 4));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveButton = new JButton("Lưu");
        saveButton.setBackground(PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
        	
        	String maNhacc = (String) tableModel.getValueAt(selectedRow, 0);
        	String tenNcc = nameField.getText();
        	String diaChi = addressField.getText();
        	String email = emailField.getText();
        	String sdt = phoneField.getText();
        	if(daoNCC.capnhatNhaCungCap(maNhacc, tenNcc, diaChi, email, sdt)) {
        	     JOptionPane.showMessageDialog(this, "Cập nhật Nhà cung cấp thành công!");
        	     //addSampleData();
        	    } else {
        	        JOptionPane.showMessageDialog(this, "Cập nhật Nhà cung cấp thất bại!");
        	    }
        	
        
    
            table.setValueAt(nameField.getText(), selectedRow, 1);
            table.setValueAt(addressField.getText(), selectedRow, 2);
            table.setValueAt(emailField.getText(), selectedRow, 3);
            table.setValueAt(phoneField.getText(), selectedRow, 4);
    
            dialog.dispose();
        });

        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSupplier() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một nhà cung cấp để xóa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa nhà cung cấp này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
        	String maNhacc = (String) tableModel.getValueAt(selectedRow, 0);
        	if(daoNCC.xoaNhaCungCapTheoMa(maNhacc)) {
        	JOptionPane.showMessageDialog(this, "Xóa thành công");
	          tableModel.removeRow(selectedRow);
	          selectedRow = -1; 
        	}else {
        		JOptionPane.showMessageDialog(this, "Xóa Thất Bại. Vì đang có sản phẩm của nhà cung cấp này trong kho");
        	}
        }
    }

    private void showAboutDialog() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một nhà cung cấp để xem thông tin",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String info = String.format("""
            Mã NCC: %s
            Tên nhà cung cấp: %s
            Địa chỉ: %s
            Email: %s
            Số điện thoại: %s
            """,
            table.getValueAt(selectedRow, 0),
            table.getValueAt(selectedRow, 1),
            table.getValueAt(selectedRow, 2),
            table.getValueAt(selectedRow, 3),
            table.getValueAt(selectedRow, 4)
        );

        JOptionPane.showMessageDialog(this,
            info,
            "Thông tin nhà cung cấp",
            JOptionPane.INFORMATION_MESSAGE);
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
    	table.removeAll();
    	 ArrayList<NhaCC> danhSachNCC = daoNCC.layTatCaNhaCungCap();        
        for (NhaCC s : danhSachNCC) {
        	String [] row = {
        		s.getMaNCC(),
        		s.getTenNCC(),
        		s.getDiaChi(),
        		s.getEmail(),
        		s.getSdt()
        	};
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


    // Phương thức validations
    private boolean validateFields(String name, String address, String email, String phone) {
        if (name.trim().isEmpty() || address.trim().isEmpty() || 
            email.trim().isEmpty() || phone.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng điền đầy đủ thông tin",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                "Email không hợp lệ",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate phone number
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                "Số điện thoại không hợp lệ (phải có 10 chữ số)",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    public class TimNhaCungCap extends JDialog {
        private DefaultTableModel tableModel;
        private static final long serialVersionUID = 1L;
        
        // Constants for styling
        private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
        private static final Color CONTENT_COLOR = new Color(255, 192, 203);
        private static final Color HOVER_COLOR = new Color(252, 231, 243);
        private static final Font HEADER_FONT = new Font("Roboto", Font.BOLD, 12);
        private static final Font CONTENT_FONT = new Font("Roboto", Font.PLAIN, 12);

        private final JPanel contentPanel = new JPanel();
        private JTable table;

        /**
         * Constructor mặc định
         */
        public TimNhaCungCap() {
            setTitle("Tìm Nhà Cung Cấp");
            setBounds(100, 100, 700, 400);
            getContentPane().setLayout(new BorderLayout());
            
            contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            getContentPane().add(contentPanel, BorderLayout.CENTER);
            contentPanel.setLayout(new BorderLayout(0, 0));

            contentPanel.add(createTablePanel(), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            // Example button for closing the dialog
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            buttonPanel.add(closeButton);
        }

        /**
         * Tạo bảng để hiển thị kết quả.
         */
        private JPanel createTablePanel() {
            JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
            tablePanel.setBackground(Color.WHITE);

            String[] columns = {"Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại"};
            
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table = new JTable(tableModel);
            table.setFont(CONTENT_FONT);
            table.setRowHeight(40);
            table.setGridColor(new Color(245, 245, 245));
            table.setSelectionBackground(HOVER_COLOR);
            table.setSelectionForeground(Color.BLACK);
            table.setShowVerticalLines(true);
            table.setShowHorizontalLines(true);

            // Center-align columns for "Mã NCC" and "Số điện thoại"
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            
            // Set renderer for specific columns
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setCellRenderer(centerRenderer); // Mã NCC
            columnModel.getColumn(4).setCellRenderer(centerRenderer); // Số điện thoại
            
            // Adjust column widths
            columnModel.getColumn(0).setPreferredWidth(80);  // Mã NCC
            columnModel.getColumn(1).setPreferredWidth(200); // Tên nhà cung cấp
            columnModel.getColumn(2).setPreferredWidth(300); // Địa chỉ
            columnModel.getColumn(3).setPreferredWidth(200); // Email
            columnModel.getColumn(4).setPreferredWidth(120); // Số điện thoại

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

            tablePanel.add(scrollPane, BorderLayout.CENTER);
            return tablePanel;
        }

        /**
         * Hiển thị kết quả tìm kiếm lên bảng.
         * 
         * @param danhSach Danh sách nhà cung cấp kết quả tìm kiếm.
         */
        public void hienThiKetQuaTimkiem(ArrayList<NhaCC> danhSach) {
            // Xóa dữ liệu cũ trong bảng
            tableModel.setRowCount(0);
            // Thêm dữ liệu mới vào bảng
            for (NhaCC nhaCC : danhSach) {
                Object[] rowData = {
                    nhaCC.getMaNCC(),
                    nhaCC.getTenNCC(),
                    nhaCC.getDiaChi(),
                    nhaCC.getEmail(),
                    nhaCC.getSdt()
                };
                tableModel.addRow(rowData);
            }
        }
    }    
}
