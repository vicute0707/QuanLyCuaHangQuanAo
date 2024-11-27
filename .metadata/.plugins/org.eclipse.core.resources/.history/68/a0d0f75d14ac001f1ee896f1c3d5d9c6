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
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dialog.ChiTietSanPham;
import dialog.EditSanPham;
import dialog.ThemSanPham;
import style.CreateRoundedButton;

import table.TBL_SP;

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
    
    public Form_SanPham() {
        initComponents();
    }
    
    private void initComponents() {
    	setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Tăng padding cho toàn bộ form

        // Top Panel
        add(createTopPanel(), BorderLayout.NORTH);

        TBL_SP tbl_SP = new TBL_SP();
        table = new JTable();
        tableModel = new DefaultTableModel();
        add(tbl_SP.createTablePanel(table, tableModel), BorderLayout.CENTER);
                
        // Cập nhật references
        table = tbl_SP.getTable();
        tableModel = tbl_SP.getTableModel();
        
        // Thêm listener cho table
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = table.getSelectedRow();
            }
        });
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(20, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Left components
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);
        
        // Custom ComboBox
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Tất cả"});
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
        CreateRoundedButton styleBtn = new CreateRoundedButton();
        JButton searchButton = styleBtn.createRoundedButton("", "/icon/search.png", false);
        
        leftPanel.add(filterCombo);
        leftPanel.add(searchField);
        leftPanel.add(searchButton);
        
        // Right components
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);
        CreateRoundedButton styleBtna = new CreateRoundedButton();
        JButton addButton = styleBtna.createRoundedButton("Thêm sản phẩm", "/icon/circle-plus.png", true);
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        
        JButton editButton = styleBtna.createRoundedButton("Edit", "/icon/pencil.png", true);
        JButton deleteButton = styleBtna.createRoundedButton("Xóa", "/icon/trash.png", true);
        JButton infoButton = styleBtna.createRoundedButton("About", "/icon/info.png", true);
        JButton exportButton = styleBtna.createRoundedButton("Xuất Excel", "/icon/printer.png", true);
        
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
        	 if (selectedRow == -1) {  
                 // Không có hàng nào được chọn  
                 JOptionPane.showMessageDialog(Form_SanPham.this,  
                         "Vui lòng chọn một sản phẩm để chỉnh sửa.",  
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

}