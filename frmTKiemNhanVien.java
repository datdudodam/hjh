package giaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class frmTKiemNhanVien extends JFrame {
    private DefaultTableModel tableModel;
    private JTextField txbten, txbCV;
    private JRadioButton rdbten, rdbCV;
    private JTable dgvKQ;
    private JButton btnTim, btnReset, btnThoat;

    public frmTKiemNhanVien() {
        setTitle("Tìm kiếm nhân viên");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo panel chứa thông tin tìm kiếm (without background)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Search Panel with grid layout
        JPanel searchPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        searchPanel.setBackground(new Color(173, 216, 230));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm Kiếm Nhân Viên"));
       
        // Radio buttons
        rdbten = new JRadioButton("Tên Nhân Viên");
        rdbCV = new JRadioButton("Chức Vụ");
        ButtonGroup searchGroup = new ButtonGroup();
        searchGroup.add(rdbten);
        searchGroup.add(rdbCV);

        // Text fields
        txbten = new JTextField(15);
        txbCV = new JTextField(15);

        // Add components to search panel
        searchPanel.add(rdbten);
        searchPanel.add(txbten);
        searchPanel.add(rdbCV);
        searchPanel.add(txbCV);

        // Action buttons
        btnTim = new JButton("Tìm");
        btnReset = new JButton("Reset");
        btnThoat = new JButton("Thoát");
        
        // Button panel with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnTim);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnThoat);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Danh Sách Nhân Viên"));
        
        // Bảng kết quả
        String[] columnNames = {"ID Nhân Viên", "Tên Nhân Viên", "Chức Vụ", "SĐT", "Khu Vực", "Email", "Địa Chỉ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dgvKQ = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Vô hiệu hóa chỉnh sửa trực tiếp
            }
        };
        dgvKQ.setFillsViewportHeight(true);
        
        // Tùy chỉnh JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(dgvKQ);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Panel chứa bảng kết quả
        center.add(tableScrollPane);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Thêm các panel vào mainPanel
        mainPanel.add(centerPanel, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        // Thêm mainPanel vào frame
        add(mainPanel);
    }

    public JTextField getTxbten() {
        return txbten;
    }

    public JTextField getTxbCV() {
        return txbCV;
    }

    public JRadioButton getRdbten() {
        return rdbten;
    }

    public JRadioButton getRdbCV() {
        return rdbCV;
    }

    public JButton getBtnTim() {
        return btnTim;
    }

    public JButton getBtnReset() {
        return btnReset;
    }

    public JButton getBtnThoat() {
        return btnThoat;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getDgvKQ() {
        return dgvKQ;
    }
}
