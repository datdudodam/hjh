package suKien;

//FrmPhieuMuonEvent.java


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import giaoDien.DatabaseConnection;
import giaoDien.FrmChonSach;
import giaoDien.FrmPhieuMuon;
import giaoDien.frmTKiemNhanVien;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class SuKienPhieuMuon {
 private FrmPhieuMuon form;

 public SuKienPhieuMuon(FrmPhieuMuon form) {
     this.form = form;
 }

 public void handleAddBook(ActionEvent e) {
	  FrmChonSach frmChonSach = new FrmChonSach(null, form);
	    frmChonSach.setVisible(true);

	
 }
 private static void saveBorrowForm(String idDocGia, String idNhanVien, String ngayMuon, String ngayHenTra, DefaultTableModel model) {
	    try (Connection conn = DatabaseConnection.getConnection()) {
	        if (conn != null) {
	            // Kiểm tra xem idNhanVien có tồn tại trong bảng nhanvien không
	            String checkNhanVienQuery = "SELECT COUNT(*) FROM nhanvien WHERE idNhanVien = ?";
	            PreparedStatement checkStmt = conn.prepareStatement(checkNhanVienQuery);
	            checkStmt.setString(1, idNhanVien);
	            ResultSet rs = checkStmt.executeQuery();

	            if (rs.next()) {
	                int count = rs.getInt(1);
	                if (count == 0) {
	                    // Nếu không tìm thấy nhân viên, hiển thị thông báo lỗi và kết thúc
	                    JOptionPane.showMessageDialog(null, "Không có nhân viên với ID: " + idNhanVien);
	                    return; // Dừng lại ở đây nếu không tìm thấy nhân viên
	                }
	            }
	            String idPhieuMuon = null;
	            String getIDPM = "select count(idPhieuMuon) +1 from phieu_muon";
	            try(PreparedStatement pss = conn.prepareStatement(getIDPM)){
	            	ResultSet rss = pss.executeQuery();
	            	if(rss.next()) {
	            		idPhieuMuon = "PM"  + rss.getInt(1);
	            	}
	            }

	            // Thêm phiếu mượn vào bảng phieu_muon
	            String insertPhieuMuon = "INSERT INTO phieu_muon (idPhieuMuon,idDocGia, idNhanVien, ngay_muon, ngay_hen_tra) VALUES (?,?, ?, ?, ?)";
	            PreparedStatement stmtPhieuMuon = conn.prepareStatement(insertPhieuMuon);
	            stmtPhieuMuon.setString(1, idPhieuMuon);
	            stmtPhieuMuon.setString(2, idDocGia);
	            stmtPhieuMuon.setString(3, idNhanVien);
	            stmtPhieuMuon.setDate(4, java.sql.Date.valueOf(ngayMuon));
	            stmtPhieuMuon.setDate(5, java.sql.Date.valueOf(ngayHenTra));
	            stmtPhieuMuon.executeUpdate();

	          

	                // Thêm chi tiết sách mượn vào chi_tiet_phieu_muon
	                String insertChiTiet = "INSERT INTO chi_tiet_phieu_muon (idPhieuMuon, maSach, tong_so_luong_muon,so_luong) VALUES (?, ?, ?,?)";
	                PreparedStatement stmtChiTiet = conn.prepareStatement(insertChiTiet);

	                for (int i = 0; i < model.getRowCount(); i++) {
	                    String maSach = model.getValueAt(i, 0).toString();
	                    int soLuong = Integer.parseInt(model.getValueAt(i, 2).toString());

	                    stmtChiTiet.setString(1, idPhieuMuon);
	                    stmtChiTiet.setString(2, maSach);
	                    stmtChiTiet.setInt(3, soLuong);
	                    stmtChiTiet.setInt(4, soLuong);
	                    stmtChiTiet.executeUpdate();
	                }
	                JOptionPane.showMessageDialog(null, "Lưu phiếu mượn thành công!");
	            
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Lỗi khi lưu phiếu mượn: " + e.getMessage());
	    }
	}
 private void updateSLKho(DefaultTableModel model) {
	 try (Connection conn = DatabaseConnection.getConnection()) {
	        if (conn != null) {
	        	String slKho = " select SoLuong from kho where maSach = ? ";
	        	String updateKho = "Update kho set soLuong = ? where maSach = ?";
	        	for(int i=0;i<model.getRowCount();i++) {
	        		String maSach = model.getValueAt(i, 0).toString();
	        		int soLuongMuon = Integer.parseInt( model.getValueAt(i, 2).toString());
	        		
	        	
	        	try(PreparedStatement Selectpss = conn.prepareStatement(slKho)){
	        		
	        		Selectpss.setString(1, maSach);
	        		ResultSet rss = Selectpss.executeQuery();
	        		if(rss.next()) {
	        			int soLuongHienTai = rss.getInt("SoLuong");
	        			if(soLuongHienTai >= soLuongMuon) {
	        				int soLuongMoi = soLuongHienTai - soLuongMuon;
	        				try(PreparedStatement psst = conn.prepareStatement(updateKho)){
	        					psst.setInt(1, soLuongMoi);
	        					psst.setString(2, maSach);
	        					psst.executeUpdate();
	        				}
	        						
	        			}else {
	        				JOptionPane.showMessageDialog(form, "Không đủ số lượng trong kho cho mã sách " + maSach,"lỗi",JOptionPane.ERROR_MESSAGE);
	        			}
	        		}else {
                        JOptionPane.showMessageDialog(null, 
                                "Không tìm thấy sách trong kho với mã sách: " + maSach, 
                                "Lỗi", 
                                JOptionPane.ERROR_MESSAGE);
	        	}
	        	}
	        		
	        	
	        }
	        }
	 }catch(SQLException e) {
	 JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật kho: " + e.getMessage());
 }
 }
 public void handleRemoveBook(ActionEvent e) {
     int selectedRow = form.getTable().getSelectedRow();
     if (selectedRow >= 0) {
         form.getModel().removeRow(selectedRow);
     } else {
         JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để xóa.");
     }
 }

 public void handleSave(ActionEvent e) {
	
	 String idDocGia = form.getborrowerIdComboBox().getText();
	 String idNhanVien = form.getidNhanVien().getText();
	 String ngayMuon = form.getborrowDateField().getText();
	 JSpinner.DateEditor dateEditor = (JSpinner.DateEditor) form.getdueDateSpinner().getEditor();
	 String ngayHenTra = dateEditor.getFormat().format(form.getdueDateSpinner().getValue());
			 

      if (!idDocGia.isEmpty() && !idNhanVien.isEmpty() && form.getTableModel().getRowCount() > 0) {
          saveBorrowForm(idDocGia,idNhanVien, ngayMuon, ngayHenTra, form.getTableModel());
          updateSLKho(form.getTableModel());
      } else {
          JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin và danh sách sách mượn.");
      }
  }

 

 public void handleCancel(ActionEvent e) {
     form.getModel().setRowCount(0); // Clear table data
     form.getborrowerIdComboBox().setText("");
     form.getidNhanVien().setText("");
  //   form.getborrowDateField().setText("");
 //  form.getBorrowerNameField().setText("");
   form.getdueDateSpinner().setValue(new Date());
     JOptionPane.showMessageDialog(null, "Dữ liệu đã được hủy.");
 }
}
