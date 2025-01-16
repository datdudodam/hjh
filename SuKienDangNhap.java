package suKien;



import javax.swing.*;

import giaoDien.FrmDangNhap;
import giaoDien.MainForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SuKienDangNhap {

    private FrmDangNhap ui;

    public SuKienDangNhap(FrmDangNhap ui) {
        this.ui = ui;
        addEventListeners();
    }

    private void addEventListeners() {
        // Add action listener for the login button
        ui.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = ui.getUsername();
                String password = ui.getPassword();
                ui.getLblErrorMessage().setText(""); // Clear previous error message

                // Perform login validation
                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    new MainForm().setVisible(true);  // Open the main form
                    ui.dispose(); // Close login window
                } else {
                    ui.getLblErrorMessage().setText("Sai tài khoản hoặc mật khẩu!");
                }
            }
        });

        // Add action listener for the exit button
        ui.getBtnExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        boolean isValid = false;
        try {
            // Database connection
            String url = "jdbc:mysql://localhost:3306/QLTV?useSSL=false&allowPublicKeyRetrieval=true";
            String dbUser = "dat";
            String dbPass = "ntdat1610c";

            Connection connect = DriverManager.getConnection(url, dbUser, dbPass);

            // SQL query to check login credentials
            String query = "SELECT username, adPass FROM admin WHERE username = ? AND adPass = ?";
            PreparedStatement pstmt = connect.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Execute query
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isValid = true;
            }

            rs.close();
            pstmt.close();
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
