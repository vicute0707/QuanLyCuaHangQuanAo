package dialog;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import entity.PhanLoaiSanPham;
import entity.SanPham;
import entity.ThemSanPhamTam;
import dao.Dao_PhanLoaiSanPham;
import dao.Dao_SanPham;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ThemPhanLoai extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 192, 203);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private PhanLoaiSanPham plsp = new PhanLoaiSanPham();
    private JTextField txtMauSac, txtKichCo, txtChatLieu;
    private JTable table;
    private DefaultTableModel tableModel;
    private boolean isConfirmed = false;
    private Dao_PhanLoaiSanPham dao = new Dao_PhanLoaiSanPham();
    private ThemSanPhamTam tam = new ThemSanPhamTam();
    private Dao_SanPham daoSanPham = new Dao_SanPham();
    public ThemPhanLoai(Frame owner) {
        super(owner, "Thêm Phân Loại Sản Phẩm", true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("THÊM PHÂN LOẠI SẢN PHẨM", SwingConstants.CENTER);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Tạo các trường nhập liệu
        txtMauSac = createStyledTextField("Màu sắc");
        txtKichCo = createStyledTextField("Kích cỡ");
        txtChatLieu = createStyledTextField("Chất liệu");

        inputPanel.add(createInputGroup("Màu sắc", txtMauSac));
        inputPanel.add(createInputGroup("Kích cỡ", txtKichCo));
        inputPanel.add(createInputGroup("Chất liệu", txtChatLieu));

        // Table Panel
        JPanel tablePanel = createTablePanel();
       
        
        

        JPanel rightButtonPanel = new JPanel(new GridLayout(4, 1, 0, 5)); // Reduce vertical space  
        rightButtonPanel.setBackground(Color.WHITE);  
        rightButtonPanel.setBorder(new EmptyBorder(0, 10, 0, 20));  

        JButton btnThem = createStyledButton("Thêm phân loại", new Color(219, 39, 119));  
        JButton btnSua = createStyledButton("Sửa phân loại", new Color(124, 58, 237));  
        JButton btnXoa = createStyledButton("Xóa phân loại", new Color(225, 29, 72));  
        JButton btnLamMoi = createStyledButton("Làm mới", new Color(104, 129, 57));  

        // Set preferred sizes to make buttons smaller  
        btnThem.setPreferredSize(new Dimension(160, 30));  
        btnSua.setPreferredSize(new Dimension(160, 30));  
        btnXoa.setPreferredSize(new Dimension(160, 30));  
        btnLamMoi.setPreferredSize(new Dimension(160, 30));  

        rightButtonPanel.add(btnThem);  
        rightButtonPanel.add(btnSua);  
        rightButtonPanel.add(btnXoa);  
        rightButtonPanel.add(btnLamMoi);  

        // Bottom Button Panel  
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));  
        bottomButtonPanel.setBackground(Color.WHITE);  
        bottomButtonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));  

        JButton btnSave = createStyledButton("Thêm sản phẩm", PRIMARY_COLOR);  
        JButton btnCancel = createStyledButton("Quay lại", new Color(255, 182, 193));  

        btnSave.setPreferredSize(new Dimension(150, 40));  
        btnCancel.setPreferredSize(new Dimension(150, 40));  

        bottomButtonPanel.add(btnSave);  
        bottomButtonPanel.add(btnCancel);  

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(rightButtonPanel, BorderLayout.EAST);
        
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Add components to dialog
        add(headerLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(bottomButtonPanel, BorderLayout.SOUTH);

        // Button Actions
        btnThem.addActionListener(e -> addVariant());
        btnSua.addActionListener(e -> editVariant());
        btnXoa.addActionListener(e -> deleteVariant());
        btnLamMoi.addActionListener(e -> clearFields());
        btnSave.addActionListener(e -> {
            isConfirmed = true;
            
            
         // Kiểm tra xem bảng có dữ liệu không (số lượng hàng lớn hơn 0)
            if (tableModel.getRowCount() > 0) {
                for (int row = 0; row < tableModel.getRowCount(); row++) {         
                    String variantID = (String) tableModel.getValueAt(row, 1);  
                    String productID = tam.getMaSP();  // Lấy "Mã PL"
                    String color = (String) tableModel.getValueAt(row, 2);      
                    String size = (String) tableModel.getValueAt(row, 3);      
                    String material = (String) tableModel.getValueAt(row, 4);   
                    boolean success = dao.insertProductVariant(variantID, productID, color, size, material);
                    if (success) {
                        System.out.println("Inserted successfully: " + variantID);
                    } else {
                        System.out.println("Failed to insert: " + variantID);
                    }
                }
            } else {
                
                System.out.println("The table is empty. No data to insert.");
            }

            
            
            
            String masp = tam.getMaSP();
           
            String ten = tam.getTxtTenSP();
            
            String madm = daoSanPham.getCategoryIDByName(tam.getTxtDanhMuc());
            int slton = 0;
            double gianhap = 0;
            double  giaban = Double.parseDouble(tam.getTxtGiaBan());
            String brand = tam.getTxtThuongHieu();
            String linkanh = tam.getLinkanh();
            if(daoSanPham.themSanPham(masp, ten, madm,slton, giaban,gianhap, brand, linkanh)) {
            	JOptionPane.showMessageDialog(this, "Thêm Sản phẩm thành công");
            }
            
            
            
            
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());

        // Dialog settings
        setSize(900, 600);
        setLocationRelativeTo(getOwner());
    }

    private JPanel createInputGroup(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(CONTENT_FONT);
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(CONTENT_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        
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

    private JPanel createTablePanel() {
        // Create table model
        String[] columns = {"STT","Mã PL", "Màu sắc", "Kích cỡ", "Chất liệu"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        table = new JTable(tableModel);
        table.setFont(CONTENT_FONT);
        table.setRowHeight(35);
        table.getTableHeader().setFont(LABEL_FONT);
        table.getTableHeader().setBackground(Color.WHITE);
        table.setSelectionBackground(HOVER_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(245, 245, 245));

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));

        // Create panel for table
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(0, 20, 0, 10));
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {	
                int selectedRow = table.getSelectedRow();
                txtMauSac.setText((String) tableModel.getValueAt(selectedRow, 2));
                txtKichCo.setText((String) tableModel.getValueAt(selectedRow, 3));
                txtChatLieu.setText((String) tableModel.getValueAt(selectedRow, 4));
                
            }
        });
       
        
        return panel;
    }
    
    
 
      

    private void addVariant() {
    	int count = dao.getTotalProductVariants();
    	String mapl  = "VAR" + String.format("%03d", tableModel.getRowCount() + count + 1);
        String mauSac = txtMauSac.getText().trim(); 
        String kichCo = txtKichCo.getText().trim();
        String chatLieu = txtChatLieu.getText().trim();
   

        if (mauSac.isEmpty() || kichCo.isEmpty() || chatLieu.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
       
    	  Object[] row = {
    	            tableModel.getRowCount() + 1,
    	            mapl,
    	            mauSac,
    	            kichCo,
    	            chatLieu,
    	            ""  // Thương hiệu để trống
    	        };
    	        
    	        tableModel.addRow(row);

        clearFields();
    }

    private void editVariant() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn phân loại cần sửa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String mapl =   (String) tableModel.getValueAt(selectedRow, 1);
        String mauSac = txtMauSac.getText().trim();
        String kichCo = txtKichCo.getText().trim();
        String chatLieu = txtChatLieu.getText().trim();

        if (mauSac.isEmpty() || kichCo.isEmpty() || chatLieu.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(dao.capNhatProductVariant(mapl, mauSac, kichCo, chatLieu)) {
        	 tableModel.setValueAt(mauSac, selectedRow, 1);
             tableModel.setValueAt(kichCo, selectedRow, 2);
             tableModel.setValueAt(chatLieu, selectedRow, 3);
        }else {
        	JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
       

        clearFields();
    }

    private void deleteVariant() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn phân loại cần xóa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa phân loại này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
        	String mapl =   (String) tableModel.getValueAt(selectedRow, 1);
        	if(dao.xoaPhanLoaiSanPham(mapl)) {
        		 tableModel.removeRow(selectedRow);
                 // Update STT after deletion
                 for (int i = 0; i < tableModel.getRowCount(); i++) {
                     tableModel.setValueAt(i + 1, i, 0);
                 }
                 JOptionPane.showMessageDialog(this, "Xóa thành công");
        	}else {
        		JOptionPane.showMessageDialog(this, "Xóa thất bại");
        	}
           
        }
    }

    private void clearFields() {
        txtMauSac.setText("");
        txtKichCo.setText("");
        txtChatLieu.setText("");
        table.clearSelection();
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }


    
}