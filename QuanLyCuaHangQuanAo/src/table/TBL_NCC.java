package table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import style.CustomScrollBarUI;

public class TBL_NCC extends JPanel {
	private JTable table;
	private DefaultTableModel tableModel;
	private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
	private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
	private static final Color HOVER_COLOR = new Color(252, 231, 243);
	private int selectedRow = -1;

	public TBL_NCC() {
		setLayout(new BorderLayout());
		// Thêm trực tiếp kết quả của hienthiTable()
		add(hienthiTable(), BorderLayout.CENTER);
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	private JPanel hienthiTable() {
		// TODO Auto-generated method stub
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(Color.WHITE);

		String[] columns = { "Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại" };
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
		columnModel.getColumn(0).setPreferredWidth(80); // Mã NCC
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
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

		// Add selection listener
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedRow = table.getSelectedRow();
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));
		CustomScrollBarUI customScrollBarUI = new CustomScrollBarUI();
		scrollPane.getVerticalScrollBar().setUI(customScrollBarUI);

		// Thêm scrollPane vào tablePanel
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		return tablePanel;
	}


}
