package giaoDien;



import giaoDien.frmTKiemNhanVien;
import suKien.SuKienTKNhanVien;

import javax.swing.SwingUtilities;



public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frmTKiemNhanVien frame = new frmTKiemNhanVien();
            new SuKienTKNhanVien(frame);  // Gán event handler cho frame
            frame.setVisible(true);
        });
    }
}
