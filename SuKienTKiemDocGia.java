package suKien;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class SuKienTKiemDocGia {
    private JTextField txbMa, txbTen;
    private JRadioButton rdbtnMa, rdbtnTen;
    private DefaultTableModel tableModel;
    private final String chuoiKN = "jdbc:mysql://localhost:3306/QLTV?useSSL=false&allowPublicKeyRetrieval=true";
    private final String dbUser = "dat";
    private final String dbPass = "ntdat1610c";

    public SuKienTKiemDocGia(JTextField txbMa, JTextField txbTen, JRadioButton rdbtnMa, JRadioButton rdbtnTen, DefaultTableModel tableModel) {
        this.txbMa = txbMa;
        this.txbTen = txbTen;
        this.rdbtnMa = rdbtnMa;
        this.rdbtnTen = rdbtnTen;
        this.tableModel = tableModel;
    }

    public void onRadioButtonMaSelected() {
        txbMa.setEnabled(true);
        txbTen.setEnabled(false);
        txbTen.setText("");
    }

    public void onRadioButtonTenSelected() {
        txbTen.setEnabled(true);
        txbMa.setEnabled(false);
        txbMa.setText("");
    }

    public void onTimButtonClicked() {
        if (rdbtnMa.isSelected()) {
            loadTK();
        } else if (rdbtnTen.isSelected()) {
            loadTen();
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một trong hai tùy chọn!");
        }
    }

    public void onResetButtonClicked() {
        txbMa.setText("");
        txbTen.setText("");
        loadData();
    }

    public void onThoatButtonClicked() {
        System.exit(0);
    }

    private void loadData() {
        try (Connection conn = DriverManager.getConnection(chuoiKN, dbUser, dbPass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM docGia")) {

            tableModel.setRowCount(0); // Clear table
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("idDocGia"),
                        rs.getString("tenDocGia"),
                        rs.getString("diaChi"),
                        rs.getString("sdtDocGia"),
                        rs.getString("emailDocGia")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối: " + ex.getMessage());
        }
    }

    private void loadTK() {
        String query = "SELECT * FROM docGia WHERE idDocGia LIKE ?";
        searchDatabase(query, txbMa.getText());
    }

    private void loadTen() {
        String query = "SELECT * FROM docGia WHERE tenDocGia LIKE ?";
        searchDatabase(query, txbTen.getText());
    }

    private void searchDatabase(String query, String searchText) {
        try (Connection conn = DriverManager.getConnection(chuoiKN, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchText.trim() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                tableModel.setRowCount(0); // Clear table
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("idDocGia"),
                            rs.getString("tenDocGia"),
                            rs.getString("diaChi"),
                            rs.getString("sdtDocGia"),
                            rs.getString("emailDocGia")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn: " + ex.getMessage());
        }
    }
}
