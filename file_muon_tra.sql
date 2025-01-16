use qltv
CREATE TABLE `phieu_muon` (
  `idPhieuMuon` varchar(10) NOT NULL,
  `idDocGia` varchar(10) DEFAULT NULL,
  `idNhanVien` varchar(10) NOT NULL,
  `ngay_muon` date NOT NULL,
  `ngay_hen_tra` date NOT NULL,
  PRIMARY KEY (`idPhieuMuon`),
  KEY `idDocGia` (`idDocGia`),
  KEY `phieu_muon_ibfk_2_idx` (`idNhanVien`),
  CONSTRAINT `phieu_muon_ibfk_1` FOREIGN KEY (`idDocGia`) REFERENCES `docgia` (`idDocGia`),
  CONSTRAINT `phieu_muon_ibfk_2` FOREIGN KEY (`idNhanVien`) REFERENCES `nhanvien` (`idNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
CREATE TABLE `chi_tiet_phieu_muon` (
  `idPhieuMuon` varchar(10) NOT NULL,
  `maSach` varchar(10) NOT NULL,
  `tong_so_luong_muon` int NOT NULL,
  `so_luong` int DEFAULT NULL,
  PRIMARY KEY (`idPhieuMuon`,`maSach`),
  KEY `maSach` (`maSach`),
  CONSTRAINT `chi_tiet_phieu_muon_ibfk_2` FOREIGN KEY (`maSach`) REFERENCES `sach` (`maSach`),
  CONSTRAINT `chi_tiet_phieu_muon_ibfk_3` FOREIGN KEY (`idPhieuMuon`) REFERENCES `phieu_muon` (`idPhieuMuon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
CREATE TABLE `phieu_tra` (
  `idPhieuTra` varchar(10) NOT NULL,
  `idPhieuMuon` varchar(10) DEFAULT NULL,
  `idNhanVien` varchar(10) NOT NULL,
  `ngay_tra` date NOT NULL,
  `phi_phat_sinh` decimal(10,2) DEFAULT '0.00',
  `trang_thai` varchar(20) NOT NULL,
  PRIMARY KEY (`idPhieuTra`),
  KEY `phieu_tra_ibfk_2_idx` (`idNhanVien`),
  KEY `phieu_tra_ibfk_1_idx` (`idPhieuMuon`),
  CONSTRAINT `phieu_tra_ibfk_1` FOREIGN KEY (`idPhieuMuon`) REFERENCES `phieu_muon` (`idPhieuMuon`),
  CONSTRAINT `phieu_tra_ibfk_2` FOREIGN KEY (`idNhanVien`) REFERENCES `nhanvien` (`idNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
CREATE TABLE `chi_tiet_phieu_tra` (
  `idPhieuTra` varchar(10) NOT NULL,
  `maSach` varchar(10) NOT NULL,
  `so_luong_muon` int NOT NULL,
  `so_luong_tra` int NOT NULL,
  `trang_thai` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idPhieuTra`,`maSach`),
  KEY `maSach` (`maSach`),
  CONSTRAINT `chi_tiet_phieu_tra_ibfk_1` FOREIGN KEY (`idPhieuTra`) REFERENCES `phieu_tra` (`idPhieuTra`),
  CONSTRAINT `chi_tiet_phieu_tra_ibfk_2` FOREIGN KEY (`maSach`) REFERENCES `sach` (`maSach`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci