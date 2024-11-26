package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import style.CreateFormField;
import table.TBL_NCC;

public class SuaNhaCungCap extends JDialog {
	private int selectedRow = -1;
	private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
	private static final Color CONTENT_COLOR = new Color(255, 192, 203);
	private static final Color HOVER_COLOR = new Color(252, 231, 243);
	private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
	private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
	CreateFormField createFormField;
	TBL_NCC tbl_NCC;
	JTable table;

	private DefaultTableModel tableModel;

	public SuaNhaCungCap() {
		createFormField = new CreateFormField();
		tbl_NCC = new TBL_NCC();

	}

	public void showEditDialog(JTable table, int selectedRow) {
		JDialog dialog = new JDialog();
		dialog.setTitle("Sửa nhà cung cấp");
		dialog.setModal(true);
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

		// Tạo các trường nhập liệu và điền dữ liệu từ dòng đã chọn
		JTextField nameField = createFormField.createFormField("Tên nhà cung cấp:", formPanel, gbc, 0);
		nameField.setText((String) table.getValueAt(selectedRow, 1));

		JTextField addressField = createFormField.createFormField("Địa chỉ:", formPanel, gbc, 1);
		addressField.setText((String) table.getValueAt(selectedRow, 2));

		JTextField emailField = createFormField.createFormField("Email:", formPanel, gbc, 2);
		emailField.setText((String) table.getValueAt(selectedRow, 3));

		JTextField phoneField = createFormField.createFormField("Số điện thoại:", formPanel, gbc, 3);
		phoneField.setText((String) table.getValueAt(selectedRow, 4));

		// Panel chứa buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(Color.WHITE);

		JButton saveButton = new JButton("Lưu");
		saveButton.setBackground(PRIMARY_COLOR);
		saveButton.setForeground(Color.WHITE);

		tableModel = (DefaultTableModel) table.getModel();

		saveButton.addActionListener(e -> {
			// Cập nhật dữ liệu vào table
			tableModel.setValueAt(nameField.getText(), selectedRow, 1);
			tableModel.setValueAt(addressField.getText(), selectedRow, 2);
			tableModel.setValueAt(emailField.getText(), selectedRow, 3);
			tableModel.setValueAt(phoneField.getText(), selectedRow, 4);
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

}
