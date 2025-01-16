package giaoDien;

import javax.swing.*;
import suKien.SuKienPhieuMuon;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

public class FrmPhieuMuon extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JButton addButton, removeButton, saveButton, cancelButton,btnTim;
    private JTextField borrowerNameField, borrowDateField, borrowerIdComboBox,tenNhanVien;
    private JSpinner dueDateSpinner;

    public FrmPhieuMuon() {
        setTitle("Quản Lý Phiếu Mượn");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        setLayout(new BorderLayout());

        // Phần 1: Thông tin độc giả
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Phần 2: Danh sách sách mượn
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Phần 3: Nút hành động
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Gắn sự kiện
        SuKienPhieuMuon eventHandler = new SuKienPhieuMuon(this);
        addButton.addActionListener(eventHandler::handleAddBook);
        removeButton.addActionListener(eventHandler::handleRemoveBook);
        saveButton.addActionListener(eventHandler::handleSave);
        cancelButton.addActionListener(eventHandler::handleCancel);
        btnTim.addActionListener(e -> HienThiTen());
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        panel.setBackground(new Color(173, 216, 230)); 
        panel.setBorder(BorderFactory.createTitledBorder("Thông Tin Độc Giả"));

        borrowerIdComboBox = new JTextField();
         btnTim = new JButton("Tìm kiếm");
        borrowerNameField = new JTextField();
        tenNhanVien = new JTextField();
        borrowDateField = new JTextField(java.time.LocalDate.now().toString());
        borrowDateField.setEditable(false);
        dueDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dueDateSpinner, "yyyy-MM-dd");
        dueDateSpinner.setEditor(dateEditor);
        panel.add(new JLabel("Mã Độc Giả:"));
        
        JPanel borrowidPanel = new JPanel(new BorderLayout());
        borrowidPanel.add(borrowerIdComboBox,BorderLayout.CENTER);
        borrowidPanel.add(btnTim,BorderLayout.EAST);
        panel.add(borrowidPanel);
      
        panel.add(new JLabel("Tên Độc Giả:"));
        panel.add(borrowerNameField);
        panel.add(new JLabel("Mã Nhân Viên:"));
        panel.add(tenNhanVien);
        panel.add(new JLabel("Ngày Mượn:"));
        panel.add(borrowDateField);
        panel.add(new JLabel("Ngày Hẹn Trả:"));
        panel.add(dueDateSpinner);
     
        return panel;
        
    }
    private void HienThiTen() {
        String idDocGia = borrowerIdComboBox.getText().trim();
        if (idDocGia.isEmpty()) {
            borrowerNameField.setText("");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT tenDocGia FROM docgia WHERE idDocGia = ?")) {
            pstm.setString(1, idDocGia);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    borrowerNameField.setText(rs.getString("tenDocGia"));
                } else {
                    borrowerNameField.setText("Không tìm thấy tên độc giả");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Danh Sách Sách Mượn"));

        String[] columns = {"Mã Sách", "Tên Sách", "Số Lượng"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Thêm Sách");
        removeButton = new JButton("Xóa Sách");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        saveButton = new JButton("Lưu Phiếu Mượn");
        cancelButton = new JButton("Hủy Bỏ");

        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    public void addBookToTable(String maSach, String tenSach, int soLuong) {
        model.addRow(new Object[]{maSach, tenSach, soLuong});
    }

    // Getter methods
    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }
    public JTextField getBorrowerNameField() { return borrowerNameField; }
    public JTextField getidNhanVien() {return tenNhanVien;}
    public JTextField getBorrowerIdComboBox() { return borrowerIdComboBox; }
    public JTextField getBorrowDateField() { return borrowDateField; }
    public JSpinner getDueDateSpinner() { return dueDateSpinner; }

    public static void main(String[] args) {
        // Khởi tạo JFrame và Controller
        FrmPhieuMuon frame = new FrmPhieuMuon();
        SuKienPhieuMuon controller = new SuKienPhieuMuon(frame); // Gắn controller vào UI
        frame.setVisible(true);
    }

    // Getter methods
    public JTextField getborrowerIdComboBox() {
        return borrowerIdComboBox;
    }

    public JTextField getborrowDateField() {
        return borrowDateField;
    }

    public JSpinner getdueDateSpinner() {
        return dueDateSpinner;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }
}
