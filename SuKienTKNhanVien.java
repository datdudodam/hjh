package suKien;


import giaoDien.frmTKiemNhanVien;
import javax.swing.*;
import java.sql.*;

public class SuKienTKNhanVien {

    private final frmTKiemNhanVien frame;
    private final String chuoiKN = "jdbc:mysql://localhost:3306/QLTV?useSSL=false&allowPublicKeyRetrieval=true";
    private final String dbUser = "dat";
    private final String dbPass = "ntdat1610c";

    public SuKienTKNhanVien(frmTKiemNhanVien frame) {
        this.frame = frame;

        // Xử lý sự kiện cho các radio button
        frame.getRdbten().addActionListener(e -> {
            frame.getTxbten().setEnabled(true);
            frame.getTxbCV().setEnabled(false);
            frame.getTxbCV().setText("");
        });

        frame.getRdbCV().addActionListener(e -> {
            frame.getTxbCV().setEnabled(true);
            frame.getTxbten().setEnabled(false);
            frame.getTxbten().setText("");
        });

        // Xử lý sự kiện nút tìm kiếm
        frame.getBtnTim().addActionListener(e -> {
            if (frame.getRdbten().isSelected()) {
                loadTen();
            } else if (frame.getRdbCV().isSelected()) {
                loadCV();
            } else {
                JOptionPane.showMessageDialog(frame, "Vui lòng chọn một trong hai tùy chọn!");
            }
        });

        // Xử lý sự kiện nút reset
        frame.getBtnReset().addActionListener(e -> {
            frame.getTxbten().setText("");
            frame.getTxbCV().setText("");
            frame.getTableModel().setRowCount(0);
        });

        // Xử lý sự kiện nút thoát
        frame.getBtnThoat().addActionListener(e -> System.exit(0));
    }

    private void loadTen() {
        String query = "SELECT * FROM nhanvien WHERE tenNhanVien LIKE ?";
        searchDatabase(query, frame.getTxbten().getText());
    }

    private void loadCV() {
        String query = "SELECT * FROM nhanvien WHERE chucVu LIKE ?";
        searchDatabase(query, frame.getTxbCV().getText());
    }

    private void searchDatabase(String query, String searchText) {
        try (Connection conn = DriverManager.getConnection(chuoiKN, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchText.trim() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                frame.getTableModel().setRowCount(0); // Clear table
                while (rs.next()) {
                    frame.getTableModel().addRow(new Object[]{
                            rs.getString("idNhanVien"),
                            rs.getString("tenNhanVien"),
                            rs.getString("chucVu"),
                            rs.getString("sdtNhanVien"),
                            rs.getString("khuVuc"),
                            rs.getString("emailNhanVien"),
                            rs.getString("diaChiNhanVien")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Lỗi truy vấn: " + ex.getMessage());
        }
    }
}
