package giaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FrmChonSach extends JDialog {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField, quantityField;  // Sửa: thêm quantityField cho số lượng mượn
    private JButton searchButton, confirmButton, cancelButton;
    private String maSach, tenSach;
    private FrmPhieuMuon parentFrame;

    public FrmChonSach(JFrame parent, FrmPhieuMuon parentFrame) {
        super(parent, "Chọn Sách Từ Kho", true);
        this.parentFrame = parentFrame;  // Lưu trữ tham chiếu tới frame cha
        setLayout(new BorderLayout());
        setSize(700, 500);
        setLocationRelativeTo(parent);

        // Thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Tìm Kiếm");
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Bảng hiển thị sách
        String[] columns = {"Mã Sách", "Tên Sách", "Số Lượng Có", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa dữ liệu trong bảng
            }
        };
        table = new JTable(tableModel);

        loadData(""); // Tải tất cả dữ liệu ban đầu
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel cho ô nhập số lượng mượn
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.add(new JLabel("Số Lượng Mượn:"));  // Thêm Label kế bên ô nhập liệu
        quantityField = new JTextField(5);  // TextField để nhập số lượng mượn
        quantityPanel.add(quantityField);

        // Đảm bảo quantityPanel không bị che khuất
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(quantityPanel, BorderLayout.NORTH);

        // Nút xác nhận và hủy
        JPanel buttonPanel = new JPanel();
        confirmButton = new JButton("Xác Nhận");
        cancelButton = new JButton("Hủy");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH); // Thêm southPanel vào BorderLayout.SOUTH

        // Xử lý sự kiện
        searchButton.addActionListener(e -> searchBooks());
        cancelButton.addActionListener(e -> dispose());
        confirmButton.addActionListener(e -> confirmSelection());
    }

    private void loadData(String keyword) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT s.maSach, s.tenSach, k.soLuong, k.trangThai FROM kho k, sach s WHERE k.maSach = s.maSach";
            if (!keyword.isEmpty()) {
                query += " AND k.maSach LIKE '%" + keyword + "%'";
            }
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0); // Xóa dữ liệu cũ
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getInt("soLuong"),
                        rs.getString("trangThai")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBooks() {
        String keyword = searchField.getText().trim();
        loadData(keyword);
    }

    private void confirmSelection() {
        int selectedRow = table.getSelectedRow();  // Chọn dòng được chọn trong bảng
        if (selectedRow != -1) {
            String maSach = (String) table.getValueAt(selectedRow, 0);
            String tenSach = (String) table.getValueAt(selectedRow, 1);

            // Lấy số lượng mượn từ JTextField ngoài bảng
            String soLuongMuonStr = quantityField.getText().trim();

            try {
                int soLuongMuon = Integer.parseInt(soLuongMuonStr);  // Chuyển đổi thành số nguyên
                if (soLuongMuon > 0) {
                    // Truy vấn số lượng sách có sẵn trong kho
                    int soLuongTonKho = getStockQuantity(maSach);

                    if (soLuongMuon <= soLuongTonKho) {
                        // Thêm sách vào JTable của FrmPhieuMuon nếu số lượng mượn hợp lệ
                        parentFrame.addBookToTable(maSach, tenSach, soLuongMuon);
                        dispose(); // Đóng hộp thoại sau khi xác nhận
                    } else {
                        JOptionPane.showMessageDialog(this, "Số lượng mượn không thể lớn hơn số lượng sách trong kho. Số lượng còn lại: " + soLuongTonKho, 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Số lượng mượn phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng mượn phải là số nguyên hợp lệ. Vui lòng kiểm tra lại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuốn sách để mượn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getStockQuantity(String maSach) {
        int soLuongTonKho = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT soLuong FROM kho WHERE maSach = '" + maSach + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                soLuongTonKho = rs.getInt("soLuong");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return soLuongTonKho;
    }
}
