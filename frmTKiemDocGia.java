package giaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import suKien.SuKienTKiemDocGia;

import java.awt.*;

public class frmTKiemDocGia extends JFrame {
    private JTextField txbMa, txbTen;
    private JRadioButton rdbtnMa, rdbtnTen;
    private JTable dgvKQ;
    private JButton btnTim, btnReset, btnThoat;
    private DefaultTableModel tableModel;
    private SuKienTKiemDocGia eventHandler;

    public frmTKiemDocGia() {
        setTitle("Tìm Kiếm Độc Giả");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Custom JPanel for Background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false); // Make header transparent
        JLabel lblHeader = new JLabel("Tìm Kiếm Độc Giả");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 102, 204));
        headerPanel.add(lblHeader);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setOpaque(false); // Make search transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rdbtnMa = new JRadioButton("Tìm theo Mã");
        rdbtnTen = new JRadioButton("Tìm theo Tên");
        ButtonGroup searchGroup = new ButtonGroup();
        searchGroup.add(rdbtnMa);
        searchGroup.add(rdbtnTen);

        txbMa = new JTextField(20);
        txbTen = new JTextField(20);

        rdbtnMa.setOpaque(false);
        rdbtnTen.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(rdbtnMa, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        searchPanel.add(txbMa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(rdbtnTen, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        searchPanel.add(txbTen, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        btnTim = createCustomButton("Tìm", new Color(0, 153, 51));
        btnReset = createCustomButton("Reset", new Color(255, 153, 0));
        btnThoat = createCustomButton("Thoát", new Color(204, 0, 0));

        buttonPanel.add(btnTim);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnThoat);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Danh sách đôc giả"));

        // Table Panel
        String[] columnNames = {"ID Độc Giả", "Tên Độc Giả", "Địa Chỉ", "SĐT", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dgvKQ = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Làm cho JTable trong suốt
        dgvKQ.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(dgvKQ);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        center.add(tableScrollPane);

        // Add Panels to Main Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(center, BorderLayout.SOUTH);

        add(mainPanel);

        // Tạo instance của DocGiaSearchEventHandler để xử lý các sự kiện
        eventHandler = new SuKienTKiemDocGia(txbMa, txbTen, rdbtnMa, rdbtnTen, tableModel);

        // Gán sự kiện
        rdbtnMa.addActionListener(e -> eventHandler.onRadioButtonMaSelected());
        rdbtnTen.addActionListener(e -> eventHandler.onRadioButtonTenSelected());
        btnTim.addActionListener(e -> eventHandler.onTimButtonClicked());
        btnReset.addActionListener(e -> eventHandler.onResetButtonClicked());
        btnThoat.addActionListener(e -> eventHandler.onThoatButtonClicked());
    }

    private JButton createCustomButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new frmTKiemDocGia().setVisible(true));
    }
}
