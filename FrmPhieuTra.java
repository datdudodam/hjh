package giaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import suKien.SuKienPhieuTra;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FrmPhieuTra extends JFrame {
    private static JTextField txtNgayHenTra;
    private static JTextField txtTenDocGia, txtMaNhanVien;
    private JTextField txtIdPhieuMuon;
    private DefaultTableModel tableModel;
    private static JTextField txtNgayTra;
    private JTextField txtPhiPhatSinh, txtTrangThai;

    public FrmPhieuTra() {
        setTitle("Quản Lý Phiếu Trả");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(173, 216, 230));
        // Tạo panel thông tin phiếu trả
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Phiếu Trả"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblIdPhieuMuon = new JLabel("Mã Phiếu Mượn:");
        txtIdPhieuMuon = new JTextField(20);
        JButton btnSearch = new JButton("Tìm kiếm");

        JLabel lblTenDocGia = new JLabel("Tên Độc Giả:");
        txtTenDocGia = new JTextField(20);

        JLabel lblMaNhanVien = new JLabel("Mã Nhân Viên:");
        txtMaNhanVien = new JTextField(20);

        JLabel lblNgayHenTra = new JLabel("Ngày Hẹn Trả:");
        txtNgayHenTra = new JTextField(20);
        txtNgayHenTra.setEditable(false);

        JLabel lblNgayTra = new JLabel("Ngày Lập Phiếu Trả:");
        txtNgayTra = new JTextField(20);
        txtNgayTra.setEditable(false);

        JLabel lblPhiPhatSinh = new JLabel("Phí Phát Sinh:");
        txtPhiPhatSinh = new JTextField("0.00");

        JLabel lblTrangThai = new JLabel("Trạng Thái:");
        txtTrangThai = new JTextField(20);

        // Đặt giá trị mặc định cho ngày lập phiếu trả
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        txtNgayTra.setText(dateFormat.format(new Date()));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblIdPhieuMuon, gbc);
        gbc.gridx = 1; formPanel.add(txtIdPhieuMuon, gbc);
        gbc.gridx = 2; formPanel.add(btnSearch, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblTenDocGia, gbc);
        gbc.gridx = 1; formPanel.add(txtTenDocGia, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblMaNhanVien, gbc);
        gbc.gridx = 1; formPanel.add(txtMaNhanVien, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblNgayHenTra, gbc);
        gbc.gridx = 1; formPanel.add(txtNgayHenTra, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblNgayTra, gbc);
        gbc.gridx = 1; formPanel.add(txtNgayTra, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(lblPhiPhatSinh, gbc);
        gbc.gridx = 1; formPanel.add(txtPhiPhatSinh, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(lblTrangThai, gbc);
        gbc.gridx = 1; formPanel.add(txtTrangThai, gbc);

        // Panel chi tiết sách trả
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Sách Trả"));

        String[] columnNames = {"Mã Sách", "Tên Sách", "Số Lượng Mượn", "Số Lượng Trả", "Trạng Thái", "Ngày Trả Tiếp Theo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; // Chỉ cột "Số Lượng Trả" và "Trạng Thái" có thể chỉnh sửa
            }
        };
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            
            if (column == 3) {
            	   try {
                       int soLuongTra = Integer.parseInt(tableModel.getValueAt(row, column).toString());
                       int soLuongMuon = Integer.parseInt(tableModel.getValueAt(row, 2).toString());

                       if (soLuongTra < 0 || soLuongTra > soLuongMuon) {
                           JOptionPane.showMessageDialog(this, 
                               "Số lượng trả không hợp lệ. Phải nằm trong khoảng từ 0 đến " + soLuongMuon,
                               "Thông Báo", JOptionPane.WARNING_MESSAGE);
                           tableModel.setValueAt(0, row, column);
                       } else if(soLuongTra < soLuongMuon){
                    	   
                       
                           
                               String ngayTraStr = txtNgayTra.getText().trim();
                               

                               try {
                                   Date ngayTra = dateFormat.parse(ngayTraStr);
                                   Calendar cal = Calendar.getInstance();
                                   cal.setTime(ngayTra);
                                   cal.add(Calendar.DAY_OF_MONTH, 7); // Add 7 days for the next return date
                                   
                                   String ngayTraTiepTheo = dateFormat.format(cal.getTime());
                                   tableModel.setValueAt(ngayTraTiepTheo, row, 5);
                                   txtTrangThai.setText("CHƯA HOÀN TẤT");
                                   // Calculate overdue fees
                                   long daysOverdue = (new Date().getTime() - ngayTra.getTime()) / (24 * 60 * 60 * 1000);
                                   if (daysOverdue > 0) {
                                       double phiPhatSinh = daysOverdue * 2000; // 2,000 VND per day
                                       txtPhiPhatSinh.setText(String.format("%.2f", phiPhatSinh));
                                   }
                               } catch (ParseException ex) {
                                   JOptionPane.showMessageDialog(this, 
                                       "Định dạng ngày không hợp lệ. Vui lòng nhập đúng định dạng yyyy-MM-dd.", 
                                       "Lỗi", JOptionPane.ERROR_MESSAGE);
                               }
                           }else {
                        	   tableModel.setValueAt("", row, 5);
                        	   txtTrangThai.setText("HOÀN TẤT");
                           }
                       }
                   catch (NumberFormatException ex) {
                       JOptionPane.showMessageDialog(this, 
                           "Vui lòng nhập một số hợp lệ cho số lượng trả.", 
                           "Lỗi", JOptionPane.ERROR_MESSAGE);
                       tableModel.setValueAt(0, row, column);
                   }
               }
           });
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Thêm combobox cho cột "Trạng Thái"
        TableColumn trangThaiColumn = table.getColumnModel().getColumn(4);
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Tốt", "Hư Hỏng", "Thiếu Trang"});
        trangThaiColumn.setCellEditor(new DefaultCellEditor(comboBox));

        // Panel nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddRow = new JButton("Thêm Sách");
        JButton btnRemoveRow = new JButton("Xóa Sách");
        JButton btnSave = new JButton("Lưu");
        JButton btnReset = new JButton("Làm Mới");
        JButton btnPrint = new JButton("In Phiếu");

        buttonPanel.add(btnAddRow);
        buttonPanel.add(btnRemoveRow);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnPrint);

        // Thêm các panel vào giao diện chính
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        SuKienPhieuTra skpt = new SuKienPhieuTra(this);
        // Event listeners
        btnSearch.addActionListener(e -> {
            String idPhieuMuon = txtIdPhieuMuon.getText().trim();
            if (!idPhieuMuon.isEmpty()) {
                populateTableWithLoanDetails(idPhieuMuon, tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Phiếu Mượn.", "Thông Báo", JOptionPane.WARNING_MESSAGE);
            }
        });

       

        btnRemoveRow.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.", "Thông Báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnReset.addActionListener(e -> {
            txtIdPhieuMuon.setText("");
            txtTenDocGia.setText("");
            txtNgayHenTra.setText(""); 
            txtMaNhanVien.setText("");
            txtNgayTra.setText(dateFormat.format(new Date()));
            txtPhiPhatSinh.setText("0.00");
            txtTrangThai.setText("");
            tableModel.setRowCount(0);
        });

        btnSave.addActionListener(skpt::savePhieuTra);        	
     

        btnPrint.addActionListener(e -> {
            // Add code to print the slip
            JOptionPane.showMessageDialog(this, "Phiếu trả đã được in.", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void populateTableWithLoanDetails(String idPhieuMuon, DefaultTableModel tableModel) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Fetching loan details
            String query = "SELECT dg.tenDocGia, pm.Ngay_Hen_Tra FROM phieu_muon pm, docgia dg " +
                           "WHERE pm.idDocGia = dg.idDocGia AND pm.idPhieuMuon = ?";
            try (PreparedStatement loadstmt = conn.prepareStatement(query)) {
                loadstmt.setString(1, idPhieuMuon);
                ResultSet load = loadstmt.executeQuery();

                if (load.next()) {
                    String tenDocGia = load.getString("tenDocGia");
                    String ngayHenTra = load.getString("Ngay_Hen_Tra");
                    txtTenDocGia.setText(tenDocGia);
                    txtNgayHenTra.setText(ngayHenTra);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin phiếu mượn.", 
                                                  "Thông Báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Calculate overdue fees
            String ngayHenTraStr = txtNgayHenTra.getText().trim();
            String ngayTraStr = txtNgayTra.getText().trim();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date ngayHenTra = dateFormat.parse(ngayHenTraStr);
                Date ngayTra = dateFormat.parse(ngayTraStr);

                long diffInMillies = ngayTra.getTime() - ngayHenTra.getTime();
                long daysOverdue = diffInMillies / (24 * 60 * 60 * 1000); // Convert milliseconds to days

                if (daysOverdue > 0) {
                    double phiPhatSinh = daysOverdue * 2000; // 2,000 VND per day
                    txtPhiPhatSinh.setText(String.format("%.2f", phiPhatSinh));
                } else {
                    txtPhiPhatSinh.setText("0.00");
                }
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng nhập đúng định dạng yyyy-MM-dd.", 
                                              "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fetching book details
            String bookQuery = "SELECT ct.maSach, s.tenSach, ct.so_luong FROM chi_tiet_phieu_muon ct, sach s " +
                               "WHERE ct.maSach = s.maSach AND ct.idPhieuMuon = ?";
            try (PreparedStatement stmt = conn.prepareStatement(bookQuery)) {
                stmt.setString(1, idPhieuMuon);
                ResultSet rs = stmt.executeQuery();

                tableModel.setRowCount(0); // Clear existing rows
                while (rs.next()) {
                //	String trangThai = rs.getString("TrangThai");
                    String maSach = rs.getString("maSach");
                    String tenSach = rs.getString("tenSach");
                    int soLuongMuon = rs.getInt("so_luong");
               
                 //   txtTrangThai.setText(trangThai);
                    tableModel.addRow(new Object[]{maSach, tenSach, soLuongMuon, "", "Tốt", ""});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage(), 
                                          "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTextField getIdPhieuMuon() { return txtIdPhieuMuon;}
    public JTextField getNgayTra() { return txtNgayTra;}
    public JTextField getPhiPhatSinh() { return txtPhiPhatSinh;}
    public DefaultTableModel getModel() {return tableModel;}
    public void setIdPhieuTra(String idPhieuMuon) {
    	txtIdPhieuMuon.setText(idPhieuMuon);
   
    }
    public JTextField getIdNhanVien() {
    	return txtMaNhanVien;
    }
    public JTextField getTrangThai() {return txtTrangThai;
    
    
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrmPhieuTra frame = new FrmPhieuTra();
            SuKienPhieuTra controller = new SuKienPhieuTra(frame);
            frame.setVisible(true);
        });
    }
}
