package dialog;  

import javax.swing.*;  
import javax.swing.border.*;  
import javax.swing.table.*;

import dao.Dao_SanPham;
import entity.SanPham;

import java.awt.*;  
import java.awt.event.*;  

public class ChiTietSanPham extends JDialog {  
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);  
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);  
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);  
    private static final Font CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 14);  

    private JTextField txtMauSac, txtKichCo, txtChatLieu;  
    private DefaultTableModel tableModel;  
    private int selectedRow;  
    private static final Color PASTEL_PINK = new Color(255,255,255); // Màu hồng pastel nhẹ nhàng
    private static final Color DARK_PINK = new Color(219, 39, 119);    // Màu hồng đậm cho các nút
    private static final Color LIGHT_PINK = new Color(252, 231, 243);  // Màu hồng nhạt cho hover
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
  
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    private JTextField txtTenSP, txtDanhMuc, txtTonKho, txtGiaBan, txtGiaNhap, txtThuongHieu;
    private JTextArea txtMoTa;
    private JLabel lblImage;
    private String imagePath = "";
    private boolean isConfirmed = false;
	private String masp;
	private Dao_SanPham daoSanPham = new Dao_SanPham();
    public ChiTietSanPham(Frame owner, DefaultTableModel tableModel, int selectedRow) {  
        super(owner, "Sản Phẩm", true);  
        this.tableModel = tableModel;  
        this.selectedRow = selectedRow;  
        initComponents();  
    }  
    
//    public ChiTietSanPham(Frame owner, String masp) {  
//        super(owner, "Sản Phẩm", true);  
//        this.masp = masp;  
//        initComponents();  
//    }  

    private void initComponents() {  
    	setLayout(new BorderLayout());
        setBackground(PASTEL_PINK);
        getRootPane().setBackground(PASTEL_PINK);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(PASTEL_PINK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
        // Title với style mới
        JLabel titleLabel = new JLabel("THÔNG TIN SẢN PHẨM");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(DARK_PINK);
        titleLabel.setBackground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel panelNorht = new JPanel();
        panelNorht.setBackground(Color.white);
        panelNorht.add(titleLabel);
        // Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(PASTEL_PINK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Image Panel với style mới
        JPanel imagePanel = new JPanel(new BorderLayout(0, 10));
        imagePanel.setBackground(PASTEL_PINK);
        
        
        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(200, 200));
        lblImage.setBorder(BorderFactory.createLineBorder(DARK_PINK, 2));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setBackground(Color.WHITE);
        lblImage.setOpaque(true);
        
        String masp = (String) tableModel.getValueAt(selectedRow, 0);
        SanPham sanPham = daoSanPham.laySanPhamTheoMa(masp);

        // Lấy đường dẫn hình ảnh
        String linkanh = sanPham.getLinhAnh();
        System.out.println(linkanh);
        // Tạo ImageIcon từ đường dẫn hình ảnh
//        ImageIcon icon = new ImageIcon("images\\products\\ao-thun-nam.jpg");
        ImageIcon icon = new ImageIcon(getClass().getResource(linkanh));


        // Thay đổi kích thước ảnh (ví dụ: 100x100)
        Image image = icon.getImage().getScaledInstance(220, 300, Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(image));

        
        
        imagePanel.add(lblImage, BorderLayout.CENTER);
   

        // Form Panel với style mới
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PASTEL_PINK);
        formPanel.setBorder(new EmptyBorder(0, 20, 0, 0));

        // Initialize components với font mới
        txtTenSP = createStyledTextField();
        txtDanhMuc = createStyledTextField();
        txtTonKho = createStyledTextField();
        txtGiaBan = createStyledTextField();
        txtGiaNhap = createStyledTextField();
        txtThuongHieu = createStyledTextField();
        
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setFont(INPUT_FONT);
        txtMoTa.setBackground(Color.WHITE);
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_PINK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);

        // Add components to form panel với label style mới
        addFormRow(formPanel, "Tên sản phẩm:", txtTenSP, 0);
        addFormRow(formPanel, "Danh mục:", txtDanhMuc, 1);
        addFormRow(formPanel, "Tồn kho:", txtTonKho, 2);
        addFormRow(formPanel, "Giá bán:", txtGiaBan, 3);
        addFormRow(formPanel, "Giá nhập:", txtGiaNhap, 4);
        addFormRow(formPanel, "Thương hiệu:", txtThuongHieu, 5);

        String tenSP = (String) tableModel.getValueAt(selectedRow, 1);
        String tenDm =  (String) tableModel.getValueAt(selectedRow, 2);
        String tonkho =  (String) tableModel.getValueAt(selectedRow, 3);
        String gianhap =  (String) tableModel.getValueAt(selectedRow, 4);
        String giaban =  (String) tableModel.getValueAt(selectedRow, 5);
        String thuonghieu =  (String) tableModel.getValueAt(selectedRow, 6);
        
        txtTenSP.setText(tenSP); 
        txtDanhMuc.setText(tonkho);
        txtTonKho.setText(tonkho);
        txtGiaBan.setText(giaban +"  VNĐ");
        txtGiaNhap.setText(gianhap +"  VNĐ");
        txtThuongHieu.setText(thuonghieu);
        txtTenSP.setEditable(false);
        txtDanhMuc.setEditable(false);
        txtTonKho.setEditable(false);
        txtGiaBan.setEditable(false);
        txtGiaNhap.setEditable(false);
        txtThuongHieu.setEditable(false);


        // Button Panel với style mới
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(PASTEL_PINK);

        JButton btnPhanLoai = createStyledButton("Xem phân loại", DARK_PINK);
        JButton btnCancel = createStyledButton("Hủy bỏ", new Color(255, 182, 193));
        
        btnPhanLoai.addActionListener(e -> {
        	XemChiTietPhanLoai dialog = new XemChiTietPhanLoai((Frame) SwingUtilities.getWindowAncestor(ChiTietSanPham.this));
            dialog.setVisible(true);
        });
        btnPhanLoai.setPreferredSize(new Dimension(160,40));
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnPhanLoai);
        buttonPanel.add(btnCancel);

        // Layout assembly
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PASTEL_PINK);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(imagePanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        centerPanel.add(formPanel, gbc);

        // Add all panels to dialog
        add(panelNorht, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Final dialog setup
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(new Dimension(130, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        
        // Thêm hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

	  private void addFormRow(JPanel panel, String labelText, JComponent component, int row) {
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = row;
	        gbc.anchor = GridBagConstraints.WEST;
	        gbc.insets = new Insets(8, 5, 8, 15);

	        JLabel label = new JLabel(labelText);
	        label.setFont(LABEL_FONT);
	        label.setForeground(Color.BLACK);
	        panel.add(label, gbc);

	        gbc.gridx = 1;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.weightx = 1.0;
	        panel.add(component, gbc);
	    }

	  private JTextField createStyledTextField() {
	        JTextField textField = new JTextField(20);
	        textField.setFont(INPUT_FONT);
	        textField.setPreferredSize(new Dimension(250, 35));
	        textField.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(DARK_PINK),
	            BorderFactory.createEmptyBorder(5, 10, 5, 10)
	        ));
	        textField.setBackground(Color.WHITE);
	        return textField;
	    }

	
}