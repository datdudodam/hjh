package suKien;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import giaoDien.FrmPhieuTra;


public class SuKienPhieuTra {
private FrmPhieuTra form;
public SuKienPhieuTra(FrmPhieuTra form) {
	this.form = form;
}
private static void saveForm(String idPhieuMuon,String idNhanVien, String ngayTra, String phiPhatSinh,String TrangThai, DefaultTableModel model) {
    try (Connection conn = giaoDien.DatabaseConnection.getConnection()) {
        if (conn != null) {
            conn.setAutoCommit(false); // Bắt đầu transaction
            String KTNhanVien = "select count(*) from nhanvien where idNhanVien = ?";
            try(PreparedStatement pt = conn.prepareStatement(KTNhanVien)){
            	pt.setString(1, idNhanVien);
            	ResultSet rs = pt.executeQuery();
            	if(rs.next()) {
            		int count = rs.getInt(1);
            		if(count==0) {
            			JOptionPane.showMessageDialog(null, "Không có Nhân Viên " + idNhanVien);
            			return;
            		}
            	}
            }
            String idPhieuTra = null;
            String GetSoPTDangCo = "select count(idPhieuTra)+1 from phieu_tra ";
            try(PreparedStatement pss = conn.prepareStatement(GetSoPTDangCo)){
            	ResultSet rss = pss.executeQuery();
            	if(rss.next()) {
            	 idPhieuTra = "PT" + rss.getInt(1);
            }
            }
            // Lưu phiếu trả
            String insertPhieuTra = "INSERT INTO phieu_tra(idPhieuTra,idPhieuMuon,idNhanVien, ngay_tra, phi_phat_sinh, trang_thai) VALUES (?,?,?, ?, ?, ?)";
            try (PreparedStatement ptsm = conn.prepareStatement(insertPhieuTra)) {
            	ptsm.setString(1, idPhieuTra);
                ptsm.setString(2, idPhieuMuon);
                ptsm.setString(3,idNhanVien);
                ptsm.setDate(4, Date.valueOf(ngayTra));
                ptsm.setString(5, phiPhatSinh);
                
                
                ptsm.setString(6, TrangThai);

                ptsm.executeUpdate();

                // Lấy ID của phiếu trả vừa thêm
                
                        
                        // Lưu chi tiết phiếu trả
                        String insertChiTiet = "INSERT INTO chi_tiet_phieu_tra (idPhieuTra, maSach, so_luong_muon, so_luong_tra,trang_thai) VALUES ( ?, ?, ?, ?,?)";                        try (PreparedStatement psm = conn.prepareStatement(insertChiTiet)) {
                            for (int i = 0; i < model.getRowCount(); i++) {
                                String maSach = model.getValueAt(i, 0).toString();
                                int soLuongMuon = Integer.parseInt(model.getValueAt(i, 2).toString());
                                int soLuongTra = Integer.parseInt(model.getValueAt(i, 3).toString());
                               
                                String trangThai = model.getValueAt(i, 4).toString();
                                psm.setString(1, idPhieuTra);
                                psm.setString(2, maSach);
                                psm.setInt(3, soLuongMuon);
                                psm.setInt(4, soLuongTra);
                               
                                psm.setString(5, trangThai);

                                psm.addBatch(); // Thêm vào batch
                            }

                            psm.executeBatch(); // Thực thi batch
                        }
                    
                    
                
            }

            conn.commit(); // Commit transaction
            JOptionPane.showMessageDialog(null, "Lưu phiếu trả thành công!");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "LỖI KHI LƯU PHIẾU TRẢ: " + ex.getMessage());
        ex.printStackTrace();
    }
}
private static void updateNgayTraTT(String idPhieuMuon, DefaultTableModel model) {
    try (Connection conn = giaoDien.DatabaseConnection.getConnection()) {
        if (conn != null) {
            String updateNgayHenTra = "UPDATE phieu_muon SET ngay_hen_tra = ? WHERE idPhieuMuon = ?";
            try (PreparedStatement psss = conn.prepareStatement(updateNgayHenTra)) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    // Lấy giá trị ngày trả tiếp theo từ model
                    String ngayTraTiepTheo = model.getValueAt(i, 5) != null ? model.getValueAt(i, 5).toString() : null;

                    // Kiểm tra nếu ngayTraTiepTheo là null hoặc rỗng thì bỏ qua
                    if (ngayTraTiepTheo == null || ngayTraTiepTheo.trim().isEmpty()) {
                        continue; // Bỏ qua dòng này và tiếp tục với dòng tiếp theo
                    }

                    try {
                        // Chuyển đổi sang kiểu java.sql.Date
                        psss.setDate(1, java.sql.Date.valueOf(ngayTraTiepTheo));
                    } catch (IllegalArgumentException ex) {
                        // Thông báo lỗi định dạng ngày nếu giá trị không hợp lệ
                        JOptionPane.showMessageDialog(null,
                            "Ngày trả tiếp theo không hợp lệ ở hàng " + (i + 1) + ". Định dạng yêu cầu là yyyy-MM-dd.",
                            "Lỗi định dạng ngày",
                            JOptionPane.ERROR_MESSAGE);
                        return; // Thoát khỏi phương thức nếu có lỗi
                    }

                    // Đặt giá trị idPhieuMuon
                    psss.setString(2, idPhieuMuon);
                    psss.executeUpdate();
                }
            }
        }
    } catch (Exception ex) {
        // Thông báo lỗi nếu xảy ra bất kỳ vấn đề nào khác
        JOptionPane.showMessageDialog(null,
            "Lỗi khi cập nhật ngày hẹn trả: " + ex.getMessage(),
            "Lỗi hệ thống",
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

private static void updateSoLuongMuon(String idPhieuMuon, DefaultTableModel model) {
    try (Connection conn = giaoDien.DatabaseConnection.getConnection()) {
        if (conn != null) {
            // Tính toán tổng số sách đã trả
            int soLuongDaTraMoi = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                int soLuongTra = Integer.parseInt(model.getValueAt(i, 3).toString());
                soLuongDaTraMoi += soLuongTra;
            }
            
            // Truy vấn phiếu mượn để lấy tổng số lượng mượn
            String sqlMuon = "SELECT so_luong FROM chi_tiet_phieu_muon WHERE idPhieuMuon = ?";
            try (PreparedStatement psMuon = conn.prepareStatement(sqlMuon)) {
                psMuon.setString(1, idPhieuMuon);
                ResultSet rs = psMuon.executeQuery();
                if (rs.next()) {
                    int tongSachMuon = rs.getInt("so_luong");
                   

                    // Tính toán số lượng sách còn thiếu
                    int soLuongConThieu = tongSachMuon -  soLuongDaTraMoi;

                    // Cập nhật lại số lượng sách đã trả và trạng thái phiếu mượn
                    String sqlCapNhatMuon = "UPDATE chi_tiet_phieu_muon SET so_luong = ? WHERE idPhieuMuon = ?";
                    try (PreparedStatement psCapNhat = conn.prepareStatement(sqlCapNhatMuon)) {
                        psCapNhat.setInt(1,soLuongConThieu ); // Cập nhật tổng số sách đã trả
                        
                        // Kiểm tra trạng thái phiếu mượn (hoàn tất nếu trả đủ, chưa hoàn tất nếu còn thiếu)
                      //   trangThai = (soLuongConThieu == 0) ? "Hoàn Tất" : "Chưa Hoàn Tất";
                     //   psCapNhat.setString(2, trangThai);
                        psCapNhat.setString(2, idPhieuMuon);
                        psCapNhat.executeUpdate();
                    }
                }
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật phiếu mượn: " + ex.getMessage());
        ex.printStackTrace();
    }
}


public void savePhieuTra(ActionEvent e) {
	 
	String PhieuMuon = form.getIdPhieuMuon().getText();
	String idNhanVien = form.getIdNhanVien().getText();
	String ngayTra = form.getNgayTra().getText();
	String phiPhatSinh = form.getPhiPhatSinh().getText();
	String trangThai = form.getTrangThai().getText();
	if(!PhieuMuon.isEmpty()&& !idNhanVien.isEmpty() && !ngayTra.isEmpty()&&!phiPhatSinh.isEmpty()) {
		saveForm(PhieuMuon,idNhanVien,ngayTra,phiPhatSinh,trangThai,form.getModel());
		updateSoLuongMuon(PhieuMuon,form.getModel());
		updateNgayTraTT(PhieuMuon,form.getModel());
	}else {
		JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin và danh sách sách .");
	}
}

}
