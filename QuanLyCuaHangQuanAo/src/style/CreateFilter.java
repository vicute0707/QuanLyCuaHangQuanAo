package style;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;

public class CreateFilter {
	private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
	private static final Color CONTENT_COLOR = new Color(255, 242, 242);
	private static final Color HOVER_COLOR = new Color(252, 231, 243);
	private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14);
	private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
	private static final Font TITLE_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16);

	public JPanel createFilterField(String label, JComponent component) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(CONTENT_COLOR);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titleLabel = new JLabel(label);
		titleLabel.setFont(CONTENT_FONT);
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		component.setAlignmentX(Component.CENTER_ALIGNMENT);

		String tooltipText = getTooltipText(label);
		if (tooltipText != null) {
			component.setToolTipText(tooltipText);
		}

		panel.add(titleLabel);
		panel.add(Box.createVerticalStrut(8));
		panel.add(component);

		return panel;
	}

	public String getTooltipText(String label) {
		switch (label) {
		case "Nhà cung cấp":
			return "Chọn nhà cung cấp để lọc phiếu nhập";
		case "Nhân viên tiếp nhận":
			return "Chọn nhân viên tiếp nhận để lọc phiếu nhập";
		case "Khoảng tiền (VND)":
			return "Nhập khoảng tiền cần lọc";
		default:
			return null;
		}
	}

	public JPanel createDateFilterPanel(JDateChooser fromDateChooser,JDateChooser toDateChooser) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(CONTENT_COLOR);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titleLabel = new JLabel("Thời gian");
		titleLabel.setFont(CONTENT_FONT);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(titleLabel);
		panel.add(Box.createVerticalStrut(12));

		panel.add(createDateChooserPanel("Từ ngày:", fromDateChooser));
		panel.add(Box.createVerticalStrut(12));
		panel.add(createDateChooserPanel("Đến ngày:", toDateChooser));

		return panel;
	}
	public JPanel createDateChooserPanel(String label, JDateChooser chooser) {
		JPanel panel = new JPanel(new BorderLayout(10, 0));
		panel.setBackground(CONTENT_COLOR);

		JLabel dateLabel = new JLabel(label);
		dateLabel.setFont(CONTENT_FONT);
		dateLabel.setPreferredSize(new Dimension(65, 35));
		panel.add(dateLabel, BorderLayout.WEST);
		panel.add(chooser, BorderLayout.CENTER);

		return panel;
	}
	public JLabel createFilterTitle() {
		JLabel titleLabel = new JLabel("Lọc kết quả");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(PRIMARY_COLOR);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		return titleLabel;
	}
	public JButton createSearchButton() {
		JButton button = new JButton();
		button.setIcon(new ImageIcon(getClass().getResource("/icon/search.png")));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFocusPainted(false);
		button.setBackground(Color.WHITE);
		button.setPreferredSize(new Dimension(38, 38));
		CreateActionButton createActionButton2 = new CreateActionButton();
		createActionButton2.addButtonHoverEffect(button);
		return button;
	}
	public JPanel createFilterButtonsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		panel.setBackground(CONTENT_COLOR);

		JButton applyButton = createFilterButton("Áp dụng", true);
		JButton resetButton = createFilterButton("Đặt lại", false);

		applyButton.setPreferredSize(new Dimension(70, 38));
		resetButton.setPreferredSize(new Dimension(70, 38));

		panel.add(applyButton);
		panel.add(resetButton);

		return panel;
	}
	public JButton createFilterButton(String text, boolean isPrimary) {
		JButton button = new JButton(text);
		button.setFont(CONTENT_FONT);

		if (isPrimary) {
			button.setBackground(PRIMARY_COLOR);
			button.setForeground(Color.WHITE);
			button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
		} else {
			button.setBackground(Color.WHITE);
			button.setForeground(Color.BLACK);
			button.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
		}

		button.setFocusPainted(false);
		CreateActionButton createActionButton1 = new CreateActionButton();
		createActionButton1.addButtonHoverEffect(button);

		return button;
	}
	   public JButton createDetailButton(String text, String iconPath, boolean isPrimary) {
	        JButton button = new JButton(text);
	        button.setFont(CONTENT_FONT);
	        button.setFocusPainted(false);

	        if (iconPath != null) {
	            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
	        }

	        if (isPrimary) {
	            button.setBackground(PRIMARY_COLOR);
	            button.setForeground(Color.WHITE);
	            button.setBorder(new LineBorder(PRIMARY_COLOR));
	        } else {
	            button.setBackground(Color.WHITE);
	            button.setForeground(Color.BLACK);
	            button.setBorder(new LineBorder(new Color(230, 230, 230)));
	        }

	        button.setPreferredSize(new Dimension(120, 35));
	        CreateActionButton btnAction = new CreateActionButton();
	        btnAction.addButtonHoverEffect(button);

	        return button;
	    }
	    public void styleFormattedTextField(JFormattedTextField textField) {
	        textField.setPreferredSize(new Dimension(70, 30));
	        textField.setFont(CONTENT_FONT);
	        textField.setBackground(Color.WHITE);
	        textField.setBorder(BorderFactory.createCompoundBorder(
	            new LineBorder(new Color(230, 230, 230), 1),
	            BorderFactory.createEmptyBorder(0, 8, 0, 8)
	        ));

	        textField.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                if (textField.isEnabled()) {
	                    textField.setBorder(BorderFactory.createCompoundBorder(
	                        new LineBorder(PRIMARY_COLOR, 1),
	                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
	                    ));
	                }
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	                if (!textField.hasFocus()) {
	                    textField.setBorder(BorderFactory.createCompoundBorder(
	                        new LineBorder(new Color(230, 230, 230), 1),
	                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
	                    ));
	                }
	            }
	        });

	        textField.addFocusListener(new FocusAdapter() {
	            @Override
	            public void focusGained(FocusEvent e) {
	                textField.setBorder(BorderFactory.createCompoundBorder(
	                    new LineBorder(PRIMARY_COLOR, 1),
	                    BorderFactory.createEmptyBorder(0, 8, 0, 8)
	                ));
	            }

	            @Override
	            public void focusLost(FocusEvent e) {
	                textField.setBorder(BorderFactory.createCompoundBorder(
	                    new LineBorder(new Color(230, 230, 230), 1),
	                    BorderFactory.createEmptyBorder(0, 8, 0, 8)
	                ));
	            }
	        });
	    }

	



}
