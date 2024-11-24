package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;

import dialog.SuaNhanVienDialog;
import dialog.ThemNhanVienDialog;

public class Form_NhanVien extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 192, 203);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);

    private int selectedRow = -1;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Constructor
    public Form_NhanVien() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JButton createRoundedButton(String text, String iconPath, boolean withText) {
        JButton button = new JButton();
        button.setFont(CONTENT_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        
        if (withText) {
            button.setText(text);
            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
            button.setIconTextGap(10);
            button.setPreferredSize(new Dimension(120, 38));
        } else {
            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
            button.setPreferredSize(new Dimension(38, 38));
        }
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.getBackground().equals(PRIMARY_COLOR)) return;
                button.setBackground(HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.getBackground().equals(PRIMARY_COLOR)) return;
                button.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(20, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 35));
        searchField.setFont(CONTENT_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchButton = createRoundedButton("", "/icon/search.png", false);
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(searchField.getText());
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionsPanel.setBackground(Color.WHITE);

        JButton addButton = createRoundedButton("Thêm nhân viên", "/icon/circle-plus.png", true);
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(160, 38));

        JButton editButton = createRoundedButton("Sửa", "/icon/pencil.png", true);
        JButton deleteButton = createRoundedButton("Xóa", "/icon/trash.png", true);

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteEmployee());

        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionsPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Mã NV", "Họ và tên", "Giới tính", "Ngày sinh", "SDT", "Email"};
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

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            if (i == 0 || i == 2 || i == 4) {
                column.setCellRenderer(centerRenderer);
            }
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.getSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 245, 245)));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void showAddDialog() {
        ThemNhanVienDialog dialog = new ThemNhanVienDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            tableModel
        );
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một nhân viên để chỉnh sửa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        else {
        	 SuaNhanVienDialog dialog = new SuaNhanVienDialog(
        			 (Frame) SwingUtilities.getWindowAncestor(this), 
        		        tableModel,   // Model của bảng
        		        selectedRow   // Dòng đang chọn
        		    );
        		    dialog.setVisible(true);
        }
        // Hiển thị dialog chỉnh sửa
        // Có thể tái sử dụng ThemNhanVienDialog với dữ liệu có sẵn
    }

    private void deleteEmployee() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một nhân viên để xóa",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa nhân viên này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            selectedRow = -1;
            JOptionPane.showMessageDialog(this,
                "Xóa nhân viên thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
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
}