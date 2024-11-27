package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dao.UserRoleDAO;
import dialog.UserInfoDialog;
import entity.User;
import entity.UserSession;

public class Form_GiaoDienChinh extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
	private JPanel panelCenter;
	private JButton currentButton;
	private Form_TrangChu homePanel;
	private Form_SanPham spPanel;
	private Form_DanhMuc dmPanel;
	private Form_NhaCungCap nccPanel;
	private Form_NhanVien nvPanel;
	private QuanLyNhapHang nhPanel;
	private QuanLyDonBan dhPanel;

	    private JPanel contentPanel;
	    private String currentUser;
	    private UserRoleDAO userRoleDAO;
		private HashMap<String, JPanel> panels;
	private static final Font MENU_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 13);

	public Form_GiaoDienChinh(String username) {
		this.currentUser = username;
		this.userRoleDAO = new UserRoleDAO();
		setSize(new Dimension(1400, 700));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);

		// North Panel (Header)
		JPanel panelNorth = createHeaderPanel();
		mainPanel.add(panelNorth, BorderLayout.NORTH);

		// Center Panel
		panelCenter = new JPanel();
		panelCenter.setBackground(Color.WHITE);
		panelCenter.setLayout(new BorderLayout());

		// Initialize and show home panel
		homePanel = new Form_TrangChu();
		spPanel = new Form_SanPham(username);
		dmPanel = new Form_DanhMuc();
		showPanel(homePanel);
		nccPanel = new Form_NhaCungCap();
		nvPanel = new Form_NhanVien();
		nhPanel = new QuanLyNhapHang();
		dhPanel = new QuanLyDonBan();

		mainPanel.add(panelCenter, BorderLayout.CENTER);
		setContentPane(mainPanel);
		setVisible(true);
	}

	private JPanel createHeaderPanel() {
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));
		panelNorth.setBackground(Color.WHITE);
		// Title Panel
		JPanel pnlTieuDe = new JPanel(new BorderLayout());
		pnlTieuDe.setBackground(Color.WHITE);
		JLabel lblLogo = new JLabel("   WOMEN FASHION");
		lblLogo.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
		lblLogo.setForeground(PRIMARY_COLOR);
		User currentUser = UserSession.getInstance().getCurrentUser();
		JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		userPanel.setBackground(Color.WHITE);

		JLabel lblUser = new JLabel("Hi, " + currentUser.getFullName() + "   ");

		lblUser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
		lblUser.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Thêm tooltip
		lblUser.setToolTipText("Xem thông tin tài khoản");

		// Thêm sự kiện click
		lblUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInfoDialog dialog = new UserInfoDialog(Form_GiaoDienChinh.this);
				dialog.setVisible(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblUser.setForeground(PRIMARY_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblUser.setForeground(Color.BLACK);
			}
		});

		userPanel.add(lblUser);
		pnlTieuDe.add(userPanel, BorderLayout.EAST);
		lblUser.setIcon(new ImageIcon(getClass().getResource("/icon/circle-user.png")));
		lblUser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
		lblUser.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlTieuDe.add(lblLogo, BorderLayout.WEST);
		// Menu Panel
		JPanel pnlJMenu = new JPanel();
		pnlJMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		pnlJMenu.setBackground(Color.WHITE);
		String[] menuItems = { "Trang chủ", "Sản phẩm", "Nhập hàng", "Xuất hàng", "Danh mục", "Nhà cung cấp",
				"Nhân viên", "Thống kê" };
		for (String item : menuItems) {
			pnlJMenu.add(createMenuButton(item));
		}
		panelNorth.add(pnlTieuDe);
		panelNorth.add(pnlJMenu);
		panelNorth.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
		return panelNorth;
	}

	private JButton createMenuButton(String text) {
		JButton button = new JButton(text);
		button.setFont(MENU_FONT);
		button.setForeground(new Color(75, 85, 99));
		button.setBackground(Color.WHITE);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(150, 30));

		button.addActionListener(e -> {
			if (currentButton != null) {
				currentButton.setForeground(new Color(75, 85, 99));
				currentButton.setBackground(Color.WHITE);
			}

			currentButton = button;
			button.setForeground(PRIMARY_COLOR);
			button.setBackground(new Color(253, 242, 248));

			if (text.equals("Trang chủ")) {
				showPanel(homePanel);
			} else if (text.equals("Sản phẩm")) {
				showPanel(spPanel);

			} else if (text.equals("Danh mục")) {
				showPanel(dmPanel);
			} else if (text.equals("Nhà cung cấp")) {
				showPanel(nccPanel);
			} else if (text.equals("Nhân viên")) {
				showPanel(nvPanel);
			} else if (text.equals("Nhập hàng")) {
				showPanel(nhPanel);
			} else if (text.equals("Xuất hàng")) {
				showPanel(dhPanel);
			}

		});

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (button != currentButton) {
					button.setForeground(PRIMARY_COLOR);
					button.setBackground(new Color(253, 242, 248));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (button != currentButton) {
					button.setForeground(new Color(75, 85, 99));
					button.setBackground(Color.WHITE);
				}
			}
		});

		String iconPath = switch (text) {
		case "Trang chủ" -> "/icon/house.png";
		case "Sản phẩm" -> "/icon/shirt.png";
		case "Nhập hàng" -> "/icon/folder-input.png";
		case "Xuất hàng" -> "/icon/folder-output.png";
		case "Danh mục" -> "/icon/chart-column-stacked.png";
		case "Nhà cung cấp" -> "/icon/container.png";
		case "Nhân viên" -> "/icon/users.png";
		case "Thống kê" -> "/icon/chart-no-axes-combined.png";
		default -> null;
		};

		if (iconPath != null) {
			button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
		}

		return button;
	}

	private void showPanel(JPanel panel) {
		panelCenter.removeAll();
		panelCenter.add(panel, BorderLayout.CENTER);
		panelCenter.revalidate();
		panelCenter.repaint();
	}
}