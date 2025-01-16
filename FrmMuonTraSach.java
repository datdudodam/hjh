package giaoDien;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import suKien.SuKienMuonTra;

public class FrmMuonTraSach extends JFrame {
	private JRadioButton radMaDG,radIDPM;
	private JButton btnTim,btnPM,btnPT;
	private JTextField txtMaDG,txtIDPM;
	private DefaultTableModel model;
	private JTable table1;
	private SuKienMuonTra eventMuonTra;

	public FrmMuonTraSach() {
		setTitle("Quản lý trả sách");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel);
		//
		JPanel timPanel = new JPanel(new GridLayout(2,2,10,10));
		JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel tongHop = new JPanel(new BorderLayout());
		timPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
		radMaDG = new JRadioButton("Mã độc giả");
		radIDPM = new JRadioButton("ID phiếu mượn");
		txtIDPM = new JTextField(20);
		txtMaDG = new JTextField(20);
		btnTim = new JButton("Tìm kiếm");
	
		btnPT = new JButton("TRẢ SÁCH");
		timPanel.add(radMaDG);
		timPanel.add(txtMaDG);
		timPanel.add(radIDPM);
		timPanel.add(txtIDPM);
		search.add(timPanel);
		search.add(btnTim);
		
		JPanel muontraPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		muontraPanel.add(btnPT);
		tongHop.add(search,BorderLayout.CENTER);
		tongHop.add(muontraPanel,BorderLayout.SOUTH);
		mainPanel.add(tongHop,BorderLayout.NORTH);
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu mượn"));
		String[] column = {"Id Phiếu Mượn","Mã Độc giả","tên độc giả","ngày mượn","ngày hẹn trả","trạng thái"};
		model = new DefaultTableModel(column,0);
		table1 = new JTable(model);
		JScrollPane tablePane = new JScrollPane(table1);
		table1.setFillsViewportHeight(true);
		mainPanel.add(tablePane,BorderLayout.CENTER);
		
		
	//////////////////
		eventMuonTra = new SuKienMuonTra(txtMaDG,txtIDPM,radMaDG,radIDPM,model);
		radMaDG.addActionListener(e -> eventMuonTra.selectMa());
		radIDPM.addActionListener(e -> eventMuonTra.selectID());
		btnTim.addActionListener(e -> eventMuonTra.loadTim());
		btnPT.addActionListener(e -> { 
			int row = table1.getSelectedRow();
			if(row != -1) {
				String idPM = table1.getValueAt(row, 0).toString();
				FrmPhieuTra form = new FrmPhieuTra();
				form.setIdPhieuTra(idPM);
				form.setVisible(true);
			}
		});
	}
	public static void main(String[] args) {
		FrmMuonTraSach frame = new FrmMuonTraSach();
		frame.setVisible(true);
	}
}
