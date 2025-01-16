package suKien;



import javax.swing.*;

import giaoDien.frmTKiemSach;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SuKienTKSach {

    private frmTKiemSach view;
    private final String chuoiKN = "jdbc:mysql://localhost:3306/QLTV?useSSL=false&allowPublicKeyRetrieval=true";
    private final String dbUser = "dat";
    private final String dbPass = "ntdat1610c";

    public SuKienTKSach(frmTKiemSach view) {
        this.view = view;

        // Event listeners
        view.getBtnTim().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });

        view.getBtnReset().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetInputs();
            }
        });

        view.getBtnRS().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Action listeners for the radio buttons
        view.getRdbtnTen().addActionListener(e -> toggleInputs(true, false, false));
        view.getRdbtnTL().addActionListener(e -> toggleInputs(false, true, false));
        view.getRdbtnTG().addActionListener(e -> toggleInputs(false, false, true));
    }

    // Method to toggle between different input fields
    private void toggleInputs(boolean enableTen, boolean enableTL, boolean enableTG) {
        view.getTxbTen().setEnabled(enableTen);
        view.getCbbTLoai().setEnabled(enableTL);
        view.getTxbShow().setEnabled(enableTG);
    }

    // Method to reset inputs
    private void resetInputs() {
        view.getTxbTen().setText("");
        view.getTxbShow().setText("");
        view.getCbbTLoai().setSelectedIndex(-1);
        view.getTableModel().setRowCount(0);
    }

    // Method to perform book search
    private void searchBooks() {
        String query;
        String param;

        if (view.getRdbtnTen().isSelected()) {
            query = "SELECT * FROM Sach WHERE tenSach LIKE ?";
            param = view.getTxbTen().getText().trim();
        } else if (view.getRdbtnTL().isSelected()) {
            query = "SELECT * FROM Sach WHERE theLoai = ?";
            param = (String) view.getCbbTLoai().getSelectedItem();
        } else if (view.getRdbtnTG().isSelected()) {
            query = "SELECT * FROM Sach WHERE tacGia LIKE ?";
            param = view.getTxbShow().getText().trim();
        } else {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một phương thức tìm kiếm.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(chuoiKN, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (view.getRdbtnTL().isSelected()) {
                stmt.setString(1, param);
            } else {
                stmt.setString(1, "%" + param + "%");
            }

            ResultSet rs = stmt.executeQuery();
            view.getTableModel().setRowCount(0);

            while (rs.next()) {
                view.getTableModel().addRow(new Object[]{
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getString("tacGia"),
                        rs.getString("nhaXuatBan"),
                        rs.getDate("namXuatBan"),
                        rs.getString("theLoai"),
                        rs.getString("viTriTV"),
                        rs.getInt("soLuong")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error searching books: " + e.getMessage());
        }
    }
}

