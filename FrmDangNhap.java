package giaoDien;



import javax.swing.*;

import suKien.SuKienDangNhap;

import java.awt.*;

public class FrmDangNhap extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblErrorMessage;
    private JButton btnLogin;
    private JButton btnExit;

    public FrmDangNhap() {
        init();
    }

    private void init() {
        setTitle("Đăng nhập");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute layout
        setLocationRelativeTo(null); // Center the window

        // Title label
        JLabel lblTitle = new JLabel("Đăng Nhập");
        lblTitle.setFont(new Font("sansserif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(7, 164, 121));
        lblTitle.setBounds(130, 20, 150, 30);
        add(lblTitle);

        // Username label and text field
        JLabel lblUsername = new JLabel("Tài khoản:");
        lblUsername.setFont(new Font("sansserif", Font.PLAIN, 14));
        lblUsername.setBounds(50, 80, 100, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 80, 180, 25);
        add(txtUsername);

        JLabel lblPasswordIcon = new JLabel(new ImageIcon("/images/image.png")); // Update with correct path
        lblPasswordIcon.setBounds(30, 120, 30, 30); // Position of the icon
        add(lblPasswordIcon);

        // Password label and password field
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("sansserif", Font.PLAIN, 14));
        lblPassword.setBounds(50, 120, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 120, 180, 25);
        add(txtPassword);

        // Login button
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(150, 170, 100, 30);
        btnLogin.setBackground(new Color(7, 164, 121));
        btnLogin.setForeground(Color.WHITE);
        add(btnLogin);

        // Exit button
        btnExit = new JButton("Thoát");
        btnExit.setBounds(250, 170, 100, 30);
        add(btnExit);

        // Error message label
        lblErrorMessage = new JLabel("");
        lblErrorMessage.setFont(new Font("sansserif", Font.PLAIN, 12));
        lblErrorMessage.setForeground(Color.RED);
        lblErrorMessage.setBounds(150, 210, 300, 25);
        add(lblErrorMessage);
    }

    // Getters for accessing UI components
    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JLabel getLblErrorMessage() {
        return lblErrorMessage;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnExit() {
        return btnExit;
    }
    public static void main(String[] args) {
    	FrmDangNhap loginUI = new FrmDangNhap();
        SuKienDangNhap controller = new SuKienDangNhap(loginUI); // Connect UI and Controller
        loginUI.setVisible(true);
	}
}

