package dialog;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import dao.Dao_SanPham;
import entity.ThemSanPhamTam;
import gui.Form_SanPham;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class ThemSanPham extends JDialog {
    // Định nghĩa màu sắc và font chung
    private static final Color PASTEL_PINK = new Color(255,255,255); // Màu hồng pastel nhẹ nhàng
    private static final Color DARK_PINK = new Color(219, 39, 119);    // Màu hồng đậm cho các nút
    private static final Color LIGHT_PINK = new Color(252, 231, 243);  // Màu hồng nhạt cho hover
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private ThemSanPhamTam tam = new ThemSanPhamTam();
    private JTextField txtTenSP, txtTonKho, txtGiaBan, txtGiaNhap, txtThuongHieu;
    private JTextArea txtMoTa;
    private JLabel lblImage;
    private String imagePath = "";
    private boolean isConfirmed = false;
	private JComboBox txtDanhMuc;
	private Dao_SanPham daoSanPham = new Dao_SanPham();
    
    public ThemSanPham(Frame owner) {
        super(owner, "Thêm Sản Phẩm Mới", true);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(PASTEL_PINK);
        getRootPane().setBackground(PASTEL_PINK);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(PASTEL_PINK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
        // Title với style mới
        JLabel titleLabel = new JLabel("THÊM SẢN PHẨM MỚI");
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
        TitledBorder imageBorder = new TitledBorder("Tải ảnh lên");
        imageBorder.setTitleFont(LABEL_FONT);
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
            imageBorder,
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(200, 200));
        lblImage.setBorder(BorderFactory.createLineBorder(DARK_PINK, 2));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setBackground(Color.WHITE);
        lblImage.setOpaque(true);
        
        JButton btnChooseImage = createStyledButton("Chọn ảnh", DARK_PINK);
        btnChooseImage.addActionListener(e -> chooseImage());
        
        imagePanel.add(lblImage, BorderLayout.CENTER);
        imagePanel.add(btnChooseImage, BorderLayout.SOUTH);

        // Form Panel với style mới
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PASTEL_PINK);
        formPanel.setBorder(new EmptyBorder(0, 20, 0, 0));

        // Initialize components với font mới
        txtTenSP = createStyledTextField();
        txtDanhMuc = new JComboBox(); // Khởi tạo combo box rỗng
        txtDanhMuc.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        fillCategoryNamesToComboBox();
       
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
        addFormRow(formPanel, "Giá bán:", txtGiaBan, 2);
        
        addFormRow(formPanel, "Thương hiệu:", txtThuongHieu, 3);
      

        // Button Panel với style mới
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(PASTEL_PINK);

        JButton btnPhanLoai = createStyledButton("Thêm phân loại", DARK_PINK);
        JButton btnCancel = createStyledButton("Hủy bỏ", new Color(255, 182, 193));
        
        btnPhanLoai.addActionListener(e -> {
        	tam.setLinkanh(imagePath);
        	tam.setTxtGiaBan(txtGiaBan.getText());
        	tam.setTxtTenSP(txtTenSP.getText());
        	tam.setTxtThuongHieu(txtThuongHieu.getText());
        	tam.setTxtDanhMuc((String) txtDanhMuc.getSelectedItem());
        	ThemPhanLoai dialog = new ThemPhanLoai((Frame) SwingUtilities.getWindowAncestor(ThemSanPham.this));
        	
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
    // Phương thức để điền dữ liệu vào JComboBox
    public void fillCategoryNamesToComboBox() {
    
        ArrayList<String> categoryNames = daoSanPham.getAllCategoryNames();    
        txtDanhMuc.removeAllItems();
        for (String categoryName : categoryNames) {
            txtDanhMuc.addItem(categoryName);
        }
        if (!categoryNames.isEmpty()) {
            txtDanhMuc.setSelectedIndex(0);
        }
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


    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
            
            // Display image preview
            try {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image image = imageIcon.getImage().getScaledInstance(
                    230, 270, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(image));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading image: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public String getImagePath() {
        return imagePath;
    }

    // Getters for form data
    public String getTenSP() { return txtTenSP.getText(); }
    public String getDanhMuc() { return (String) txtDanhMuc.getSelectedItem(); }
    public String getTonKho() { return txtTonKho.getText(); }
    public String getGiaBan() { return txtGiaBan.getText(); }
    public String getGiaNhap() { return txtGiaNhap.getText(); }
    public String getThuongHieu() { return txtThuongHieu.getText(); }
    public String getMoTa() { return txtMoTa.getText(); }
}