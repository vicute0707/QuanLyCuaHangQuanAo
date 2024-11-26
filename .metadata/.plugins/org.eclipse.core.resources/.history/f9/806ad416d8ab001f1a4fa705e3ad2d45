package dialog;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;

public class SuaNhanVienDialog extends JDialog {
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat;
    private int selectedRow;
    
    // Các trường nhập liệu
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> genderCombo;
    private JDateChooser birthDateChooser;
    private JTextField phoneField;
    private JTextField emailField;
    
    public SuaNhanVienDialog(Frame owner, DefaultTableModel tableModel, int selectedRow) {
        super(owner, "Sửa thông tin nhân viên", true);
        this.tableModel = tableModel;
        this.selectedRow = selectedRow;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        initComponents();
        loadEmployeeData();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private void initComponents() {
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        
        int gridy = 0;
        
        // Thông tin cơ bản section
        addSectionHeader(formPanel, "Thông tin cơ bản", gbc, gridy++);
        
        // Mã nhân viên
        idField = createFormField("Mã nhân viên:", formPanel, gbc, gridy++);
        idField.setEditable(false);
        idField.setBackground(new Color(245, 245, 245));
        
        // Họ và tên
        nameField = createFormField("Họ và tên: *", formPanel, gbc, gridy++);
        
        // Giới tính
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.setBackground(Color.WHITE);
        JLabel genderLabel = new JLabel("Giới tính: *");
        genderLabel.setFont(CONTENT_FONT);
        String[] genders = {"Nam", "Nữ"};
        genderCombo = new JComboBox<>(genders);
        genderCombo.setFont(CONTENT_FONT);
        genderCombo.setPreferredSize(new Dimension(200, 35));
        genderPanel.add(genderLabel);
        genderPanel.add(Box.createHorizontalStrut(63));
        genderPanel.add(genderCombo);
        gbc.gridy = gridy++;
        formPanel.add(genderPanel, gbc);
        
        // Ngày sinh
        JPanel birthDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        birthDatePanel.setBackground(Color.WHITE);
        JLabel birthDateLabel = new JLabel("Ngày sinh: *");
        birthDateLabel.setFont(CONTENT_FONT);
        birthDateChooser = new JDateChooser();
        birthDateChooser.setPreferredSize(new Dimension(200, 35));
        birthDateChooser.setFont(CONTENT_FONT);
        birthDateChooser.setDateFormatString("dd/MM/yyyy");
        birthDatePanel.add(birthDateLabel);
        birthDatePanel.add(Box.createHorizontalStrut(60));
        birthDatePanel.add(birthDateChooser);
        gbc.gridy = gridy++;
        formPanel.add(birthDatePanel, gbc);
        
        // Thông tin liên hệ section
        addSectionHeader(formPanel, "Thông tin liên hệ", gbc, gridy++);
        
        // Số điện thoại
        phoneField = createFormField("Số điện thoại: *", formPanel, gbc, gridy++);
        
        // Email
        emailField = createFormField("Email: *", formPanel, gbc, gridy++);
        
        // Scrollpane cho form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Required fields note
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notePanel.setBackground(Color.WHITE);
        notePanel.setBorder(new EmptyBorder(0, 25, 10, 25));
        JLabel noteLabel = new JLabel("(*) Thông tin bắt buộc");
        noteLabel.setFont(new Font(CONTENT_FONT.getName(), Font.ITALIC, 11));
        noteLabel.setForeground(new Color(150, 150, 150));
        notePanel.add(noteLabel);
        mainPanel.add(notePanel, BorderLayout.SOUTH);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadEmployeeData() {
        try {
            // Lấy dữ liệu từ bảng và điền vào các trường
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            genderCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
            
            // Xử lý ngày sinh
            String birthDateStr = tableModel.getValueAt(selectedRow, 3).toString();
            Date birthDate = dateFormat.parse(birthDateStr);
            birthDateChooser.setDate(birthDate);
            
            phoneField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            emailField.setText(tableModel.getValueAt(selectedRow, 5).toString());
        } catch (Exception ex) {
            showError("Lỗi khi tải dữ liệu nhân viên: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void addSectionHeader(JPanel panel, String title, GridBagConstraints gbc, int gridy) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(10, 0, 5, 0));
        
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font(HEADER_FONT.getName(), Font.BOLD, 13));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(230, 230, 230));
        headerPanel.add(separator, BorderLayout.SOUTH);
        
        gbc.gridy = gridy;
        panel.add(headerPanel, gbc);
    }
    
    private JTextField createFormField(String labelText, JPanel panel, GridBagConstraints gbc, int row) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fieldPanel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(CONTENT_FONT);
        
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setFont(CONTENT_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createHorizontalStrut(calculateLabelSpacing(labelText)));
        fieldPanel.add(textField);
        
        gbc.gridy = row;
        panel.add(fieldPanel, gbc);
        
        return textField;
    }
    
    private int calculateLabelSpacing(String labelText) {
        switch (labelText) {
            case "Email: *": return 87;
            case "Số điện thoại: *": return 45;
            default: return 65;
        }
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        
        JButton saveButton = createStyledButton("Lưu thay đổi", true);
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> {
            if (validateFields()) {
                updateEmployee();
            }
        });
        
        JButton cancelButton = createStyledButton("Hủy", false);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn hủy? Các thay đổi sẽ không được lưu.",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(CONTENT_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isPrimary) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setBorder(new LineBorder(PRIMARY_COLOR));
            
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(PRIMARY_COLOR.darker());
                    button.setBorder(new LineBorder(PRIMARY_COLOR.darker()));
                }
                
                public void mouseExited(MouseEvent e) {
                    button.setBackground(PRIMARY_COLOR);
                    button.setBorder(new LineBorder(PRIMARY_COLOR));
                }
            });
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(new LineBorder(new Color(230, 230, 230)));
            
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(HOVER_COLOR);
                }
                
                public void mouseExited(MouseEvent e) {
                    button.setBackground(Color.WHITE);
                }
            });
        }
        
        return button;
    }
    
    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder("Vui lòng kiểm tra lại:\n");
        boolean isValid = true;
        
        // Kiểm tra tên
        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("- Họ và tên không được để trống\n");
            isValid = false;
        } else if (!nameField.getText().trim().matches("^[\\p{L} .'-]+$")) {
            errorMessage.append("- Họ và tên chỉ được chứa chữ cái và khoảng trắng\n");
            isValid = false;
        }
        
        // Kiểm tra ngày sinh
        if (birthDateChooser.getDate() == null) {
            errorMessage.append("- Ngày sinh không được để trống\n");
            isValid = false;
        } else {
            Date birthDate = birthDateChooser.getDate();
            Date currentDate = new Date();
            
            // Kiểm tra tuổi (ít nhất 18 tuổi)
            long age = (currentDate.getTime() - birthDate.getTime()) / (1000L * 60 * 60 * 24 * 365);
            if (age < 18) {
                errorMessage.append("- Nhân viên phải đủ 18 tuổi\n");
                isValid = false;
            }
            
            if (birthDate.after(currentDate)) {
                errorMessage.append("- Ngày sinh không thể sau ngày hiện tại\n");
                isValid = false;
            }
        }
        
        // Kiểm tra số điện thoại
        if (phoneField.getText().trim().isEmpty()) {
            errorMessage.append("- Số điện thoại không được để trống\n");
            isValid = false;
        } else if (!isValidPhone(phoneField.getText().trim())) {
            errorMessage.append("- Số điện thoại không hợp lệ (phải có 10 số và bắt đầu bằng 0 hoặc +84)\n");
            isValid = false;
        }
        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("- Email không được để trống\n");
            isValid = false;
        } else if (!isValidEmail(emailField.getText().trim())) {
            errorMessage.append("- Email không hợp lệ\n");
            isValid = false;
        }
        
        if (!isValid) {
            JOptionPane.showMessageDialog(
                this,
                errorMessage.toString(),
                "Lỗi nhập liệu",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return isValid;
    }
    
    private void updateEmployee() {
        try {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn cập nhật thông tin nhân viên này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                // Cập nhật dữ liệu trong bảng
                tableModel.setValueAt(nameField.getText().trim(), selectedRow, 1);
                tableModel.setValueAt(genderCombo.getSelectedItem().toString(), selectedRow, 2);
                tableModel.setValueAt(dateFormat.format(birthDateChooser.getDate()), selectedRow, 3);
                tableModel.setValueAt(phoneField.getText().trim(), selectedRow, 4);
                tableModel.setValueAt(emailField.getText().trim(), selectedRow, 5);
                
                // Hiển thị thông báo thành công
                JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật thông tin nhân viên thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Đóng dialog
                dispose();
            }
        } catch (Exception ex) {
            showError("Lỗi khi cập nhật thông tin nhân viên: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // Các phương thức utility hỗ trợ
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    // Phương thức để kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
    
    // Phương thức để kiểm tra định dạng số điện thoại
    private boolean isValidPhone(String phone) {
        return phone.matches("^(0|\\+84)[0-9]{9}$");
    }
}