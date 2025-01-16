package suKien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import giaoDien.DatabaseConnection;



public class SuKienMuonTra {
	private JRadioButton radMaDG,radIDPM;
	private JButton btnTim,btnPM,btnPT;
	private JTextField txtMaDG,txtIDPM;
	private DefaultTableModel model;
	private JTable table1;
	public SuKienMuonTra(JTextField txtMaDG,JTextField txtIDPM,JRadioButton radMaDG,JRadioButton radIDPM, DefaultTableModel model) {
		this.txtMaDG = txtMaDG;
		this.txtIDPM = txtIDPM;
		this.radMaDG = radMaDG;
		this.radIDPM = radIDPM;
		this.model = model;
	}
	
	public void selectMa() {
		txtMaDG.setEnabled(true);
		txtIDPM.setEnabled(false);
		txtIDPM.setText("");
	}
	public void selectID() {
		txtIDPM.setEnabled(true);
		txtMaDG.setEnabled(false);
		txtMaDG.setText("");
	}
	public void loadTim() {
		if(radMaDG.isSelected()) {
			LoadMa();
		}else if(radIDPM.isSelected()) {
			LoadID();
		}else {
			JOptionPane.showMessageDialog(null,"Vui lòng chọn 1 trong 2 tùy chọn!");
		}
	}
	private void LoadMa() {
		  String query = "SELECT pm.*, IF(pt.idPhieuMuon IS NULL, 'Chưa trả sách', pt.trang_thai) AS trang_thai "
	                + "FROM phieu_muon pm LEFT JOIN phieu_tra pt ON pm.idPhieuMuon = pt.idPhieuMuon "
	                + "WHERE pm.idDocGia LIKE ?";
	        LoadData(query, txtMaDG.getText());
		
	}
	private void LoadID() {
		 String query = "SELECT pm.*, IF(pt.idPhieuMuon IS NULL, 'Chưa trả sách', pt.trang_thai) AS trang_thai "
	                + "FROM phieu_muon pm LEFT JOIN phieu_tra pt ON pm.idPhieuMuon = pt.idPhieuMuon "
	                + "WHERE pm.idPhieuMuon LIKE ?";
	        LoadData(query, txtIDPM.getText());
	}
	public void LoadData(String query,String txt) {
		try(Connection conn = DatabaseConnection.getConnection()){
			if(conn!= null) {
				try(PreparedStatement pss = conn.prepareStatement(query)){
					pss.setString(1, "%" + txt.trim() + "%");
					ResultSet rss = pss.executeQuery();
					model.setRowCount(0);
					while(rss.next()) {
						model.addRow(new Object[] {
								rss.getString("idPhieuMuon"),
								rss.getString("idDocGia"),
								rss.getString("idNhanVien"),
								rss.getString("ngay_muon"),
								rss.getString("ngay_hen_tra"),
								rss.getString("trang_thai")
						});
					}
				}
			}
		}catch(SQLException e) {
			 JOptionPane.showMessageDialog(null, "Lỗi truy vấn: " + e.getMessage());
			
		}
	}
}
