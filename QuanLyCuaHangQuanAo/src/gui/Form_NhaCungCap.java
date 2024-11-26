package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.*;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dialog.SuaNhaCungCap;
import dialog.ThemNhaCungCap;
import export.ExcelExporterNCC;
import style.CreateRoundedButton;
import table.TBL_NCC;

public class Form_NhaCungCap extends JPanel {
	private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
	private static final Color CONTENT_COLOR = new Color(255, 192, 203);
	private static final Color HOVER_COLOR = new Color(252, 231, 243);
	private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
	private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
	private int selectedRow = -1;
	JTable table;
	DefaultTableModel tableModel;
	TBL_NCC tblNcc;
	private JTextField searchField;
	public Form_NhaCungCap() {
		initComponents();
		table = tblNcc.getTable();
		tableModel = tblNcc.getTableModel();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedRow = table.getSelectedRow();

			}
		});
	}
	private void initComponents() {
		setLayout(new BorderLayout(0, 20));
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(30, 30, 30, 30));
		// Thêm các components
		add(createTopPanel(), BorderLayout.NORTH);
		// Khởi tạo và thêm table
		tblNcc = new TBL_NCC();
		// Thêm table vào vị trí CENTER của BorderLayout
		add(tblNcc, BorderLayout.CENTER);
		tblNcc.setVisible(true);
		table = tblNcc.getTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedRow = table.getSelectedRow();
				// Enable các nút chỉ khi đã chọn dòng

			}
		});

	}

	private JPanel createTopPanel() {
		CreateRoundedButton btnStyle = new CreateRoundedButton();
		JPanel topPanel = new JPanel(new BorderLayout(20, 0));
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
		// Left components with search
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		searchPanel.setBackground(Color.WHITE);
		// Combo box "Tất cả"
		JComboBox<String> filterCombo = new JComboBox<>(new String[] { "Tất cả" });
		filterCombo.setPreferredSize(new Dimension(120, 35));
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
		filterCombo.setFont(CONTENT_FONT);
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(200, 35));
		searchField.setFont(CONTENT_FONT);
		
		JButton searchButton = btnStyle.createRoundedButton("", "/icon/search.png", false);
		searchPanel.add(filterCombo);
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		// Right components with buttons
		JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		actionsPanel.setBackground(Color.WHITE);
		JButton addButton = btnStyle.createRoundedButton("Thêm NCC", "/icon/circle-plus.png", true);
		addButton.setBackground(PRIMARY_COLOR);
		addButton.setForeground(Color.WHITE);
		addButton.setPreferredSize(new Dimension(160, 38));
		JButton editButton = btnStyle.createRoundedButton("Edit", "/icon/pencil.png", true);
		JButton deleteButton = btnStyle.createRoundedButton("Xóa", "/icon/trash.png", true);
		JButton aboutButton = btnStyle.createRoundedButton("About", "/icon/info.png", true);
		JButton exportButton = btnStyle.createRoundedButton("Xuất Excel", "/icon/printer.png", true);
		exportButton.setPreferredSize(new Dimension(160, 38));
		// Add action listeners
		addButton.addActionListener(e -> {
			ThemNhaCungCap themNhaCungCap = new ThemNhaCungCap();
			themNhaCungCap.showAddDialog(table, tableModel);
		});
		SuaNhaCungCap suaNhaCungCap = new SuaNhaCungCap();
		editButton.addActionListener(e -> {
			if (selectedRow != -1) {
				suaNhaCungCap.showEditDialog(table, selectedRow);
			} else {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để sửa", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});
		deleteButton.addActionListener(e -> deleteSupplier());
		aboutButton.addActionListener(e -> showAboutDialog());
		exportButton.addActionListener(e -> exportToExcel());
		actionsPanel.add(addButton);
		actionsPanel.add(editButton);
		actionsPanel.add(deleteButton);
		actionsPanel.add(aboutButton);
		actionsPanel.add(exportButton);
		topPanel.add(searchPanel, BorderLayout.WEST);
		topPanel.add(actionsPanel, BorderLayout.EAST);
		return topPanel;
	}

	private void deleteSupplier() {
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để xóa", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận xóa",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			tableModel.removeRow(selectedRow);
			selectedRow = -1;
		}
	}

	private void showAboutDialog() {
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để xem thông tin", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String info = String.format("""
				Mã NCC: %s
				Tên nhà cung cấp: %s
				Địa chỉ: %s
				Email: %s
				Số điện thoại: %s
				""", table.getValueAt(selectedRow, 0), table.getValueAt(selectedRow, 1),
				table.getValueAt(selectedRow, 2), table.getValueAt(selectedRow, 3), table.getValueAt(selectedRow, 4));

		JOptionPane.showMessageDialog(this, info, "Thông tin nhà cung cấp", JOptionPane.INFORMATION_MESSAGE);
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

	// Phương thức xuất Excel
	private void exportToExcel() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
			fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xls"));

			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile().getAbsolutePath();
				if (!filePath.endsWith(".xls")) {
					filePath += ".xls";
				}

				ExcelExporterNCC exporter = new ExcelExporterNCC();
				exporter.exportToExcel(table, filePath);

				JOptionPane.showMessageDialog(this, "Xuất Excel thành công!\nFile được lưu tại: " + filePath,
						"Thông báo", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	// Phương thức validations
	private boolean validateFields(String name, String address, String email, String phone) {
		if (name.trim().isEmpty() || address.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Validate email format
		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			JOptionPane.showMessageDialog(this, "Email không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Validate phone number
		if (!phone.matches("\\d{10}")) {
			JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (phải có 10 chữ số)", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

}
