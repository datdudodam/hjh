package giaoDien;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainForm extends JFrame {
    private JMenuBar menuBar;
    private JMenu adminMenu, manageInfoMenu, searchMenu, transactionMenu, reportMenu, loginInfoMenu;
    JMenuItem adminItem;
    JMenuItem manageBooksItem;
    JMenuItem manageEmployeesItem;
    JMenuItem manageReadersItem;
    JMenuItem eventItem;
    JMenuItem facilitiesItem;
    private JMenuItem searchBooksItem, searchReadersItem, searchEmployeesItem;
    JMenuItem borrowTicketItem;
    JMenuItem returnTicketItem;
    JMenuItem statsItem;
    private JMenuItem changePasswordItem;
    JMenuItem logoutItem;
    JMenuItem exitItem;
    private JToolBar toolBar;
    JButton adminButton;
    JButton readerButton;
    JButton employeeButton;
    JButton bookButton;
    JButton borrowButton;
    JButton returnButton;
    JButton statsButton;
    private JLabel imageLabel;

    public MainForm() {
        setTitle("Main Form");
        setSize(1029, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        menuBar = new JMenuBar();

        // Admin menu
        adminMenu = new JMenu("Quản lý tài khoản");
        adminItem = new JMenuItem("Admin");
        adminMenu.add(adminItem);

        // Manage Information Menu
        manageInfoMenu = new JMenu("Quản lý thông tin");
        manageBooksItem = new JMenuItem("Quản lý sách");
        manageEmployeesItem = new JMenuItem("Quản lý nhân viên");
        manageReadersItem = new JMenuItem("Quản lý độc giả");
        eventItem = new JMenuItem("Sự kiện");
        facilitiesItem = new JMenuItem("Cơ sở vật chất");

        manageInfoMenu.add(manageBooksItem);
        manageInfoMenu.add(manageEmployeesItem);
        manageInfoMenu.add(manageReadersItem);
        manageInfoMenu.add(eventItem);
        manageInfoMenu.add(facilitiesItem);

        // Search Information Menu
        searchMenu = new JMenu("Tìm kiếm thông tin");
        searchBooksItem = new JMenuItem("Tìm kiếm sách");
        searchReadersItem = new JMenuItem("Tìm kiếm độc giả");
        searchEmployeesItem = new JMenuItem("Tìm kiếm nhân viên");
        searchMenu.add(searchBooksItem);
        searchMenu.add(searchReadersItem);
        searchMenu.add(searchEmployeesItem);

        // Transaction Menu
        transactionMenu = new JMenu("Quản lý Mượn - Trả");
        borrowTicketItem = new JMenuItem("Phiếu mượn");
        returnTicketItem = new JMenuItem("Phiếu trả");
        transactionMenu.add(borrowTicketItem);
        transactionMenu.add(returnTicketItem);

        // Report Menu
        reportMenu = new JMenu("Thống kê");
        statsItem = new JMenuItem("Thống kê");
        reportMenu.add(statsItem);

        // Login Information Menu
        loginInfoMenu = new JMenu("Thông tin đăng nhập");
        changePasswordItem = new JMenuItem("Đổi mật khẩu");
        logoutItem = new JMenuItem("Đăng xuất");
        exitItem = new JMenuItem("Thoát");
        loginInfoMenu.add(changePasswordItem);
        loginInfoMenu.add(logoutItem);
        loginInfoMenu.addSeparator();
        loginInfoMenu.add(exitItem);

        // Add menus to menu bar
        menuBar.add(adminMenu);
        menuBar.add(manageInfoMenu);
        menuBar.add(searchMenu);
        menuBar.add(transactionMenu);
        menuBar.add(reportMenu);
        menuBar.add(loginInfoMenu);

        setJMenuBar(menuBar);

        // Create tool bar
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(230, 230, 250));

        // Add buttons to toolbar
        adminButton = createToolBarButton("Admin", "src/images/admin.png");
        readerButton = createToolBarButton("Độc giả", "src/images/reader.png");
        employeeButton = createToolBarButton("Nhân viên", "src/images/employee.png");
        bookButton = createToolBarButton("Sách", "src/images/sach.jpg");
        borrowButton = createToolBarButton("Mượn sách", "src/images/borrow.png");
        returnButton = createToolBarButton("Trả sách", "src/images/return.png");
        statsButton = createToolBarButton("Thống kê", "src/images/stats.png");

        toolBar.add(adminButton);
        toolBar.add(readerButton);
        toolBar.add(employeeButton);
        toolBar.add(bookButton);
        toolBar.add(borrowButton);
        toolBar.add(returnButton);
        toolBar.add(statsButton);

        add(toolBar, BorderLayout.NORTH);

        // Create a JLabel to display an image
        imageLabel = new JLabel();
        setBackgroundImage("src/images/thuvien.jpg");
        add(imageLabel, BorderLayout.CENTER);
    }

    private JButton createToolBarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // Văn bản nằm ngang bên phải biểu tượng
        button.setVerticalTextPosition(SwingConstants.CENTER);  // Đảm bảo căn giữa theo chiều dọc
        button.setMargin(new Insets(5, 10, 5, 10)); // Tùy chỉnh khoảng cách
        return button;
    }


    private void setBackgroundImage(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();

        // Tính toán tỷ lệ gốc của ảnh
        int originalWidth = imageIcon.getIconWidth();
        int originalHeight = imageIcon.getIconHeight();
        double aspectRatio = (double) originalWidth / originalHeight;

        // Tính toán kích thước phù hợp với JFrame
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int newWidth = frameWidth;
        int newHeight = (int) (frameWidth / aspectRatio);

        if (newHeight > frameHeight) {
            newHeight = frameHeight;
            newWidth = (int) (frameHeight * aspectRatio);
        }

        // Thay đổi kích thước ảnh
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(resizedImage));

        // Căn giữa hình ảnh
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainForm().setVisible(true));
    }
}
