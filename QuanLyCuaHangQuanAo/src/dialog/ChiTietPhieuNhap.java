package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import style.StyleButton;

public class ChiTietPhieuNhap extends JDialog {
	  private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
	    private static final Color CONTENT_COLOR = new Color(255, 242, 242);
	    private static final Color HOVER_COLOR = new Color(252, 231, 243);
	    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14);
	    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
	    private static final Font TITLE_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16);

	public void chiTietPhieuNhap(JTable table, DefaultTableModel detailModel) {

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
        detailModel = new DefaultTableModel(columns, 0) {
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
        StyleButton stButton = new StyleButton();
        stButton.styleButton(printButton, true);
        printButton.addActionListener(evt -> {
            // Xử lý in phiếu
            JOptionPane.showMessageDialog(detailDialog, 
                "Đang in phiếu " + maPhieu,
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        });

        // Nút đóng
        JButton closeButton = new JButton("Đóng");
        StyleButton styleButton = new StyleButton();
        styleButton.styleButton(closeButton, false);
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

}


