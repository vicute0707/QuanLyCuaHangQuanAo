package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dao.Dao_SanPham;
import dialog.ChiTietSanPham;
import dialog.EditSanPham;
import dialog.ThemSanPham;
import dialog.TimSanPham;

import entity.NhaCC;
import entity.SanPham;
import gui.Form_NhaCungCap.TimNhaCungCap;

public class Form_SanPham extends JPanel {
    
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 192, 203);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
    private int selectedRow = -1; // Thêm biến để lưu hàng được chọn

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private Dao_SanPham daoSanPham = new Dao_SanPham();
    public Form_SanPham() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Tăng padding cho toàn bộ form
        
        // Top Panel
        add(createTopPanel(), BorderLayout.NORTH);
        
        // Table Panel
        add(createTablePanel(), BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(20, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Left components
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);
        
        // Custom ComboBox
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Theo Mã", "Tên SP", "Tên DM", "Thương Hiệu"});
        filterCombo.setPreferredSize(new Dimension(120, 35));
        filterCombo.setFont(CONTENT_FONT);
        filterCombo.setBorder(BorderFactory.createEmptyBorder());
        filterCombo.setBackground(Color.WHITE);
        
        filterCombo.setUI(new BasicComboBoxUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                LookAndFeel.installProperty(comboBox, "opaque", false);
            }
            
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton();
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setIcon(new ImageIcon(getClass().getResource("/icon/arrow-down.png")));
                return button;
            }
            
            @Override
            protected ComboPopup createPopup() {
                return new BasicComboPopup(comboBox) {
                    @Override
                    protected void configurePopup() {
                        super.configurePopup();
                        setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
                    }
                    
                    @Override
                    protected void configureList() {
                        super.configureList();
                        list.setSelectionBackground(HOVER_COLOR);
                        list.setSelectionForeground(Color.BLACK);
                    }
                };
            }
        });
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 35));
        searchField.setFont(CONTENT_FONT);
        
        JButton searchButton = createRoundedButton("", "/icon/search.png", false);
        
        leftPanel.add(filterCombo);
        leftPanel.add(searchField);
        leftPanel.add(searchButton);
        searchButton.addActionListener(e -> {
            String selectedFilter = (String) filterCombo.getSelectedItem(); // Lấy lựa chọn từ combobox
            String keyword = searchField.getText().trim(); // Lấy từ khóa tìm kiếm

            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Dao_SanPham daoSanPham = new Dao_SanPham(); // Tạo đối tượng DAO
            ArrayList<SanPham> results = new ArrayList<>(); // Danh sách kết quả tìm kiếm

            switch (selectedFilter) {
                case "Theo Mã":
                    results = daoSanPham.timSanPhamTheoMa(keyword);
                    break;
                case "Tên SP":
                    results = daoSanPham.timSanPhamTheoTenSP(keyword);
                    break;
                case "Tên DM":
                    results = daoSanPham.timSanPhamTheoTenDM(keyword);
                    break;
//                case "Theo Giá Nhập":
//                    try {
//                        double giaNhapMin = Double.parseDouble(keyword.split("-")[0].trim());
//                        double giaNhapMax = Double.parseDouble(keyword.split("-")[1].trim());
//                        results = daoSanPham.timSanPhamTheoGiaNhap(giaNhapMin, giaNhapMax);
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(this, "Vui lòng nhập khoảng giá nhập hợp lệ (ví dụ: 1000-5000)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//                    break;
//                case "Theo Giá Bán":
//                    try {
//                        double giaBanMin = Double.parseDouble(keyword.split("-")[0].trim());
//                        double giaBanMax = Double.parseDouble(keyword.split("-")[1].trim());
//                        results = daoSanPham.timSanPhamTheoGiaBan(giaBanMin, giaBanMax);
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(this, "Vui lòng nhập khoảng giá bán hợp lệ (ví dụ: 1000-5000)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//                    break;
                case "Thương Hiệu":
                    results = daoSanPham.timSanPhamTheoThuongHieu(keyword);
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
            	TimSanPham dialog = new TimSanPham();
                dialog.hienThiKetQuaTimkiem(results);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        });

        
        // Right components
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);
        
        JButton addButton = createRoundedButton("Thêm sản phẩm", "/icon/circle-plus.png", true);
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        
        JButton editButton = createRoundedButton("Edit", "/icon/pencil.png", true);
        JButton deleteButton = createRoundedButton("Xóa", "/icon/trash.png", true);
        JButton infoButton = createRoundedButton("About", "/icon/info.png", true);
        JButton exportButton = createRoundedButton("Xuất Excel", "/icon/printer.png", true);
        
        
//        exportButton.addActionListener(e->{
//        	String save = "D:\\DanhSachSanPham.xlsx";
//        	exportToHTMLWithFont(table, save);
//        });
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file"); // Tiêu đề hộp thoại
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Chỉ chọn file

            // Đặt tên file mặc định và phần mở rộng
            fileChooser.setSelectedFile(new File("DanhSachSanPham.xlsx"));

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

        
        deleteButton.addActionListener(e->{
        	selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(Form_SanPham.this,
                    "Vui lòng chọn một danh mục để xóa.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                int confirm = JOptionPane.showConfirmDialog(Form_SanPham.this,
                    "Bạn có chắc muốn xóa danh mục này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                	String maSP = (String) tableModel.getValueAt(selectedRow, 0);
                	if(daoSanPham.XoaSanPhamTheoMa(maSP)) {
                		JOptionPane.showMessageDialog(this, "Xóa Thành công");
                		  tableModel.removeRow(selectedRow);
                          selectedRow = -1;
                          
                	}else {
                		JOptionPane.showMessageDialog(this, "Xóa thất bại");
                	}
                  
                }
            }
        });
        
        
        
        
        rightPanel.add(addButton);
        addButton.setPreferredSize(new Dimension(160, 38));
        addButton.addActionListener(e -> {
            ThemSanPham dialog = new ThemSanPham((Frame) SwingUtilities.getWindowAncestor(Form_SanPham.this));
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                // Xử lý thêm sản phẩm mới vào table
                Object[] rowData = {
                    "SP" + (tableModel.getRowCount() + 1),
                    dialog.getTenSP(),
                    dialog.getDanhMuc(),
                    dialog.getTonKho(),
                    dialog.getGiaNhap(),
                    dialog.getGiaBan(),
                    dialog.getThuongHieu(),
                    "Còn hàng"
                };
                tableModel.addRow(rowData);
            }
        });
        rightPanel.add(editButton);
        editButton.addActionListener(e -> { 
        	selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {  
                // Không có hàng nào được chọn  
                JOptionPane.showMessageDialog(Form_SanPham.this,  
                        "Vui lòng chọn một sản phẩm để chỉnh sửa.",  
                        "Thông báo",  
                        JOptionPane.WARNING_MESSAGE);  
            } else {  
                // Mở hộp thoại chỉnh sửa với hàng đã chọn  
                EditSanPham dialog = new EditSanPham((Frame) SwingUtilities.getWindowAncestor(Form_SanPham.this), tableModel, selectedRow);  
                dialog.setVisible(true);        
            }  
        });  
        rightPanel.add(deleteButton);
        rightPanel.add(infoButton);
        infoButton.addActionListener(e -> {
        	selectedRow = table.getSelectedRow();
       
        	 if (selectedRow == -1) {  
                 // Không có hàng nào được chọn  
                 JOptionPane.showMessageDialog(Form_SanPham.this,  
                         "Vui lòng chọn một sản phẩm để xem chi tiết.",  
                         "Thông báo",  
                         JOptionPane.WARNING_MESSAGE);  
             } else {  
                 // Mở hộp thoại chỉnh sửa với hàng đã chọn  
                 ChiTietSanPham dialog = new ChiTietSanPham((Frame) SwingUtilities.getWindowAncestor(Form_SanPham.this), tableModel, selectedRow);  
                 dialog.setVisible(true);        
             } 
        });
        rightPanel.add(exportButton);
        exportButton.setPreferredSize(new Dimension(160, 38));
        
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        String[] columns = {
            "Mã SP", "Tên sản phẩm", "Danh mục", "Số lượng tồn", 
            "Giá nhập", "Giá bán", "Thương hiệu", "Tình trạng"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(CONTENT_FONT);
        table.setRowHeight(32);
        table.setGridColor(new Color(245, 245, 245));
        table.setSelectionBackground(HOVER_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setIntercellSpacing(new Dimension(10, 10));
        
        // Set preferred size for auto scrolling
        table.setPreferredScrollableViewportSize(new Dimension(800, 400));
        table.setFillsViewportHeight(true);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the selected row index
                selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    if (e.getClickCount() == 2) {
                        // Create and show edit dialog
                        EditSanPham dialog = new EditSanPham((Frame) SwingUtilities.getWindowAncestor(Form_SanPham.this), tableModel, selectedRow);
                        dialog.setVisible(true);
                        
                    }
                }
            }
        });
        
        // Setup table headerCustomScrollBarUI
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        // Add sample data
        addSampleData();
        
        // Create scroll pane with custom ScrollBarUI
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(245, 245, 245))
        ));
        
        // Customize scroll bars
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        
        // Set scroll bar policies
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
       
        return tablePanel;
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
                           10, 10); // Bo tròn góc
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y,
                       trackBounds.width, trackBounds.height);
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
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(text.isEmpty() ? 38 : 130, 38));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
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
    
    private void addSampleData() {
   	 ArrayList<SanPham> danhsachSanPham = daoSanPham.layTatCaSanPham();        
     for (SanPham s : danhsachSanPham) {
         String[] row = {
             s.getMaSP(),
             s.getTenSP(),
             s.getDanhmuc().getTenDM(),
             String.valueOf(s.getSoLuongTonKho()),  // Chuyển đổi sang String
             String.valueOf(s.getGiaNhap()),        // Chuyển đổi sang String
             String.valueOf(s.getGiaBan()),         // Chuyển đổi sang String
             s.getThuongHieu(),
             s.getTinhtrang()
         };
         tableModel.addRow(row);
     }
        
        // Set custom cell renderer for status column
        table.getColumnModel().getColumn(7).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(CONTENT_FONT);
            label.setBorder(new EmptyBorder(0, 5, 0, 5)); // Thêm padding cho cell
            
            switch (value.toString()) {
                case "Còn hàng":
                    label.setForeground(new Color(0, 128, 0));
                    break;
                case "Sắp hết":
                    label.setForeground(new Color(255, 140, 0));
                    break;
                case "Hết hàng":
                    label.setForeground(Color.RED);
                    break;
            }
            
            if (isSelected) {
                label.setBackground(HOVER_COLOR);
            } else {
                label.setBackground(table.getBackground());
            }
            
            return label;
        });
    }
    // phương thức xuất file Excel
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
    
    public class TimSanPham extends JDialog {
        private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
        private static final Color CONTENT_COLOR = new Color(255, 192, 203);
        private static final Color HOVER_COLOR = new Color(252, 231, 243);
        private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
        private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
        private int selectedRow = -1; // Variable to hold the selected row index

        private static final long serialVersionUID = 1L;
        private final JPanel contentPanel = new JPanel();
        private TableModel tableModel;
        private JTable table;
        private Dao_SanPham daoSanPham = new Dao_SanPham();

        /**
         * Launch the application.
         */

        /**
         * Create the dialog.
         */
        public TimSanPham() {
            setTitle("Tìm Sản Phẩm");
            setBounds(100, 100, 800, 400);
            getContentPane().setLayout(new BorderLayout());
            
            contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            getContentPane().add(contentPanel, BorderLayout.CENTER);
            contentPanel.setLayout(new BorderLayout(0, 0));

            contentPanel.add(createTablePanel(), BorderLayout.CENTER);
            
            // Optional: Add a button panel (for example, a Close button)
            JPanel buttonPanel = new JPanel();
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            buttonPanel.add(closeButton);
        }

        private JPanel createTablePanel() {
            JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            
            String[] columns = {
                "Mã SP", "Tên sản phẩm", "Danh mục", "Số lượng tồn", 
                "Giá nhập", "Giá bán", "Thương hiệu", "Tình trạng"
            };
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;  // All columns are non-editable
                }
            };
            
            table = new JTable(tableModel);
            table.setFont(CONTENT_FONT);
            table.setRowHeight(32);
            table.setGridColor(new Color(245, 245, 245));
            table.setSelectionBackground(HOVER_COLOR);
            table.setSelectionForeground(Color.BLACK);
            table.setIntercellSpacing(new Dimension(10, 10));
            
            // Set preferred size for auto scrolling
            table.setPreferredScrollableViewportSize(new Dimension(800, 400));
            table.setFillsViewportHeight(true);
            
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	
                }
            });
            
            // Setup table header
            JTableHeader header = table.getTableHeader();
            header.setFont(HEADER_FONT);
            header.setBackground(Color.WHITE);
            header.setForeground(Color.BLACK);
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
           
            
            // Create scroll pane with custom ScrollBarUI
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(Color.WHITE);
            scrollPane.setBackground(Color.white);
            scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(new Color(245, 245, 245))
            ));
            
            // Customize scroll bars
            scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
            scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
            
            // Set scroll bar policies
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            tablePanel.add(scrollPane, BorderLayout.CENTER);
           
            return tablePanel;
        }
        
        public void hienThiKetQuaTimkiem(ArrayList<SanPham> danhsachSanPham ) {
            // Xóa dữ liệu cũ trong bảng
            ((DefaultTableModel) tableModel).setRowCount(0);

           

            for (SanPham s : danhsachSanPham) {
                String[] row = {
                    s.getMaSP(),
                    s.getTenSP(),
                    s.getDanhmuc().getTenDM(),
                    String.valueOf(s.getSoLuongTonKho()),  // Chuyển đổi sang String
                    String.valueOf(s.getGiaNhap()),        // Chuyển đổi sang String
                    String.valueOf(s.getGiaBan()),         // Chuyển đổi sang String
                    s.getThuongHieu(),
                    s.getTinhtrang()
                };
                ((DefaultTableModel) tableModel).addRow(row);
            }
            // Set custom cell renderer for status column
            table.getColumnModel().getColumn(7).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(CONTENT_FONT);
                label.setBorder(new EmptyBorder(0, 5, 0, 5)); // Thêm padding cho cell
                
                switch (value.toString()) {
                    case "Còn hàng":
                        label.setForeground(new Color(0, 128, 0));
                        break;
                    case "Sắp hết":
                        label.setForeground(new Color(255, 140, 0));
                        break;
                    case "Hết hàng":
                        label.setForeground(Color.RED);
                        break;
                }
                
                if (isSelected) {
                    label.setBackground(HOVER_COLOR);
                } else {
                    label.setBackground(table.getBackground());
                }
                
                return label;
            });
            
            
            
        }
        

        // Custom ScrollBar UI for better styling
        private class CustomScrollBarUI extends BasicScrollBarUI {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = CONTENT_COLOR;
                this.trackColor = Color.WHITE;
            }
        }
    }
    
}