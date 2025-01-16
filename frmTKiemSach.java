package giaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import suKien.SuKienTKSach;

import java.awt.*;

public class frmTKiemSach extends JPanel {

    private JTextField txbTen, txbShow;
    private JComboBox<String> cbbTLoai;
    private JRadioButton rdbtnTen, rdbtnTL, rdbtnTG;
    private JButton btnTim, btnReset, btnRS;
    private JTable dgvTKSach;
    private DefaultTableModel tableModel;

    public frmTKiemSach	() {
        setLayout(new BorderLayout());
      

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));

        // Create search panel
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm Kiếm Sách"));
        searchPanel.setBackground(new Color(173, 216, 230));

        txbTen = new JTextField();
        txbShow = new JTextField();
        cbbTLoai = new JComboBox<>();

        rdbtnTen = new JRadioButton("Tên Sách");
        rdbtnTL = new JRadioButton("Thể Loại");
        rdbtnTG = new JRadioButton("Tác Giả");

        ButtonGroup group = new ButtonGroup();
        group.add(rdbtnTen);
        group.add(rdbtnTL);
        group.add(rdbtnTG);

        btnTim = createButton("Tìm Kiếm", new Color(0, 153, 51));
        btnReset = createButton("Làm Mới", new Color(255, 153, 0));
        btnRS = createButton("Thoát", new Color(204, 0, 0));

        searchPanel.add(rdbtnTen);
        searchPanel.add(txbTen);
        searchPanel.add(rdbtnTL);
        searchPanel.add(cbbTLoai);
        searchPanel.add(rdbtnTG);
        searchPanel.add(txbShow);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Danh Mục Sách"));

        // Table for displaying results
        tableModel = new DefaultTableModel(new String[]{
                "Mã Sách", "Tên Sách", "Tác Giả", "Nhà Xuất Bản", "Năm Xuất Bản", "Thể Loại", "Vị Trí", "Số Lượng"
        }, 0);

        dgvTKSach = new JTable(tableModel);
        dgvTKSach.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(dgvTKSach);
        scrollPane.setPreferredSize(new Dimension(800, 250));
        centerPanel.add(scrollPane);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonPanel.add(btnTim);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnRS);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(searchPanel, BorderLayout.NORTH);
        center.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(center, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Helper method to create buttons
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    // Getters for components
    public JTextField getTxbTen() {
        return txbTen;
    }

    public JTextField getTxbShow() {
        return txbShow;
    }

    public JComboBox<String> getCbbTLoai() {
        return cbbTLoai;
    }

    public JRadioButton getRdbtnTen() {
        return rdbtnTen;
    }

    public JRadioButton getRdbtnTL() {
        return rdbtnTL;
    }

    public JRadioButton getRdbtnTG() {
        return rdbtnTG;
    }

    public JButton getBtnTim() {
        return btnTim;
    }

    public JButton getBtnReset() {
        return btnReset;
    }

    public JButton getBtnRS() {
        return btnRS;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
 public static void main(String[] args) {
	 SwingUtilities.invokeLater(() -> {
         frmTKiemSach view = new frmTKiemSach();
         SuKienTKSach controller = new SuKienTKSach(view);

         JFrame frame = new JFrame("Tìm Kiếm Sách");
         frame.setSize(850, 650);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setLocationRelativeTo(null);
         frame.add(view);
         frame.setVisible(true);
     });
 
}  
}

