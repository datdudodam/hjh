package suKien;

import javax.swing.*;

import giaoDien.FrmChonSach;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SuKienChonSach {
    private final FrmChonSach view;
 

    public SuKienChonSach(JFrame parent) {
        this.view = new FrmChonSach(parent, null);
        initController();
    }

    private void initController() {
        loadData();
     
    }

    private void loadData() {
        
    }

  

  
}

