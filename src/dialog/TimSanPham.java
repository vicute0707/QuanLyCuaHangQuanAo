package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dao.Dao_SanPham;
import entity.SanPham;

public class TimSanPham extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(219, 39, 119);
    private static final Color CONTENT_COLOR = new Color(255, 192, 203);
    private static final Color HOVER_COLOR = new Color(252, 231, 243);
    private static final Font HEADER_FONT = new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12);
    private int selectedRow = -1; // Variable to hold the selected row index

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private TableModel tableModel;
    private JTable table;
    private Dao_SanPham daoSanPham = new Dao_SanPham();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            TimSanPham dialog = new TimSanPham();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public TimSanPham() {
        setTitle("Tìm Sản Phẩm");
        setBounds(100, 100, 800, 400);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        contentPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        // Optional: Add a button panel (for example, a Close button)
        JPanel buttonPanel = new JPanel();
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        String[] columns = {
            "Mã SP", "Tên sản phẩm", "Danh mục", "Số lượng tồn", 
            "Giá nhập", "Giá bán", "Thương hiệu", "Tình trạng"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // All columns are non-editable
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(CONTENT_FONT);
        table.setRowHeight(32);
        table.setGridColor(new Color(245, 245, 245));
        table.setSelectionBackground(HOVER_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setIntercellSpacing(new Dimension(10, 10));
        
        // Set preferred size for auto scrolling
        table.setPreferredScrollableViewportSize(new Dimension(800, 400));
        table.setFillsViewportHeight(true);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	
            }
        });
        
        // Setup table header
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        // Add sample data
        addSampleData();
        
        // Create scroll pane with custom ScrollBarUI
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(245, 245, 245))
        ));
        
        // Customize scroll bars
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        
        // Set scroll bar policies
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
       
        return tablePanel;
    }

    private void addSampleData() {
      	 ArrayList<SanPham> danhsachSanPham = daoSanPham.layTatCaSanPham();        
        for (SanPham s : danhsachSanPham) {
            String[] row = {
                s.getMaSP(),
                s.getTenSP(),
                s.getDanhmuc().getTenDM(),
                String.valueOf(s.getSoLuongTonKho()),  // Chuyển đổi sang String
                String.valueOf(s.getGiaNhap()),        // Chuyển đổi sang String
                String.valueOf(s.getGiaBan()),         // Chuyển đổi sang String
                s.getThuongHieu(),
                s.getTinhtrang()
            };
            ((DefaultTableModel) tableModel).addRow(row);
        }
           
           // Set custom cell renderer for status column
           table.getColumnModel().getColumn(7).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
               JLabel label = new JLabel(value.toString());
               label.setOpaque(true);
               label.setHorizontalAlignment(JLabel.CENTER);
               label.setFont(CONTENT_FONT);
               label.setBorder(new EmptyBorder(0, 5, 0, 5)); // Thêm padding cho cell
               
               switch (value.toString()) {
                   case "Còn hàng":
                       label.setForeground(new Color(0, 128, 0));
                       break;
                   case "Sắp hết":
                       label.setForeground(new Color(255, 140, 0));
                       break;
                   case "Hết hàng":
                       label.setForeground(Color.RED);
                       break;
               }
               
               if (isSelected) {
                   label.setBackground(HOVER_COLOR);
               } else {
                   label.setBackground(table.getBackground());
               }
               
               return label;
           });
       }

    // Custom ScrollBar UI for better styling
    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = CONTENT_COLOR;
            this.trackColor = Color.WHITE;
        }
    }
}
