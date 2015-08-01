-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 01. Oktober 2014 jam 11:52
-- Versi Server: 5.5.16
-- Versi PHP: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `itbsp`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_groups`
--

CREATE TABLE IF NOT EXISTS `sys_groups` (
  `groupno` int(11) NOT NULL,
  `groupname` varchar(100) NOT NULL,
  PRIMARY KEY (`groupno`),
  KEY `groupname` (`groupname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_groups`
--

INSERT INTO `sys_groups` (`groupno`, `groupname`) VALUES
(5, 'Admin Operational'),
(4, 'IT Admin'),
(6, 'Test2');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_groupsprogram`
--

CREATE TABLE IF NOT EXISTS `sys_groupsprogram` (
  `groupno` int(11) NOT NULL,
  `menuid` varchar(100) NOT NULL,
  `menuname` varchar(100) NOT NULL,
  `menubyasc` varchar(50) NOT NULL,
  `acceslevel` varchar(6) NOT NULL,
  KEY `groupno` (`groupno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_groupsprogram`
--

INSERT INTO `sys_groupsprogram` (`groupno`, `menuid`, `menuname`, `menubyasc`, `acceslevel`) VALUES
(5, 'MenuFile', 'File', 'a', '11111'),
(5, 'MenuLogOut', 'Log Out', 'ab', '11111'),
(5, 'MenuExit', 'Exit', 'ac', '11111'),
(5, 'MenuInventory', 'Inventory', 'c', '11111'),
(5, 'MenuPurchase', 'Purchase', 'ca', '11111'),
(5, 'MenuPurchaseOrder', 'Purchase Order', 'caa', '11111'),
(5, 'MenuPenerimaanBarang', 'Penerimaan Barang', 'cab', '11111'),
(5, 'MenuPeminjaman', 'Peminjaman Barang', 'cb', '11111'),
(5, 'MenuPengeluaranPinjaman', 'Pengeluaran Pinjam', 'cba', '11111'),
(5, 'MenuPengembalianPinjaman', 'Pengembalian', 'cbb', '11111'),
(5, 'MenuPengeluaranBarang', 'Pengeluaran Barang', 'cc', '11111'),
(5, 'MenuPengeluaranBarang2', 'Pengeluaran', 'cca', '11111'),
(5, 'MenuSuratJalanBarang', 'Surat Jalan', 'ccb', '11111'),
(6, 'MenuFile', 'File', 'a', '10011'),
(6, 'MenuLogOut', 'Log Out', 'ab', '10011'),
(6, 'MenuExit', 'Exit', 'ac', '10011'),
(6, 'MenuInventory', 'Inventory', 'c', '10011'),
(6, 'MenuPurchase', 'Purchase', 'ca', '10011'),
(6, 'MenuPurchaseOrder', 'Purchase Order', 'caa', '10011'),
(6, 'MenuPenerimaanBarang', 'Penerimaan Barang', 'cab', '10011'),
(6, 'MenuPeminjaman', 'Peminjaman Barang', 'cb', '10011'),
(6, 'MenuPengeluaranPinjaman', 'Pengeluaran Pinjam', 'cba', '10011'),
(6, 'MenuPengembalianPinjaman', 'Pengembalian', 'cbb', '10011'),
(6, 'MenuReport', 'Report', 'd', '10011'),
(6, 'MenuStockMovement', 'Stock Movement IT', 'da', '10011'),
(6, 'MenuReportPeminjaman', 'Report Peminjaman', 'db', '10011'),
(4, 'MenuFile', 'File', 'a', '11111'),
(4, 'MenuLogOut', 'Log Out', 'ab', '11111'),
(4, 'MenuExit', 'Exit', 'ac', '11111'),
(4, 'MenuHRD', 'Menu Pelamar', 'c', '11111'),
(4, 'MenuInventory', 'Inventory', 'c', '11111'),
(4, 'MenuPurchase', 'Purchase', 'ca', '11111'),
(4, 'MenuPurchaseOrder', 'Purchase Order', 'caa', '11111'),
(4, 'MenuPenerimaanBarang', 'Penerimaan Barang', 'cab', '11111'),
(4, 'MenuPeminjaman', 'Peminjaman Barang', 'cb', '11111'),
(4, 'MenuPengeluaranPinjaman', 'Pengeluaran Pinjam', 'cba', '11111'),
(4, 'MenuPengembalianPinjaman', 'Pengembalian', 'cbb', '11111'),
(4, 'MenuPengeluaranBarang', 'Pengeluaran Barang', 'cc', '11111'),
(4, 'MenuPengeluaranBarang2', 'Pengeluaran', 'cca', '11111'),
(4, 'MenuSuratJalanBarang', 'Surat Jalan', 'ccb', '11111'),
(4, 'MenuReport', 'Report', 'd', '11111'),
(4, 'MenuStockMovement', 'Stock Movement IT', 'da', '11111'),
(4, 'MenuReportPeminjaman', 'Report Peminjaman', 'db', '11111'),
(4, 'MenuUtility', 'Utility', 'e', '11111'),
(4, 'MenuPeriode', 'Periode', 'ea', '11111'),
(4, 'MenuOpenPeriode', 'Open Periode', 'eaa', '11111'),
(4, 'MenuClosePeriode', 'Close Periode', 'eab', '11111'),
(4, 'MenuSetup', 'Setup', 'x', '11111'),
(4, 'MenuSetupProgram', 'Setup Program', 'xa', '11111'),
(4, 'MenuProgramParameter', 'Program Parameter', 'xaa', '11111'),
(4, 'MenuSetupInventory', 'Setup Inventory', 'xab', '11111'),
(4, 'MenuAdministrator', 'User Administrator', 'xb', '11111'),
(4, 'MenuHRD', 'HRD', 'c', '11111');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_program`
--

CREATE TABLE IF NOT EXISTS `sys_program` (
  `menuid` varchar(100) NOT NULL,
  `menuname` varchar(100) NOT NULL,
  `menubyasc` varchar(50) NOT NULL,
  PRIMARY KEY (`menubyasc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_program`
--

INSERT INTO `sys_program` (`menuid`, `menuname`, `menubyasc`) VALUES
('MenuFile', 'File', 'a'),
('MenuLogOut', 'Log Out', 'ab'),
('MenuExit', 'Exit', 'ac'),
('MenuCuti', 'Cuti', 'b'),
('MenuHRD', 'HRD', 'c'),
('MenuPurchaseOrder', 'Purchase Order', 'caa'),
('MenuPenerimaanBarang', 'Penerimaan Barang', 'cab'),
('MenuPeminjaman', 'Peminjaman Barang', 'cb'),
('MenuPengeluaranPinjaman', 'Pengeluaran Pinjam', 'cba'),
('MenuPengembalianPinjaman', 'Pengembalian', 'cbb'),
('MenuPengeluaranBarang', 'Pengeluaran Barang', 'cc'),
('MenuPengeluaranBarang2', 'Pengeluaran', 'cca'),
('MenuSuratJalanBarang', 'Surat Jalan', 'ccb'),
('MenuReport', 'Report', 'd'),
('MenuStockMovement', 'Stock Movement IT', 'da'),
('MenuReportPeminjaman', 'Report Peminjaman', 'db'),
('MenuUtility', 'Utility', 'e'),
('MenuPeriode', 'Periode', 'ea'),
('MenuOpenPeriode', 'Open Periode', 'eaa'),
('MenuClosePeriode', 'Close Periode', 'eab'),
('MenuSetup', 'Setup', 'x'),
('MenuSetupProgram', 'Setup Program', 'xa'),
('MenuProgramParameter', 'Program Parameter', 'xaa'),
('MenuSetupInventory', 'Setup Inventory', 'xab'),
('MenuAdministrator', 'User Administrator', 'xb');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_user`
--

CREATE TABLE IF NOT EXISTS `sys_user` (
  `userid` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(200) NOT NULL,
  `expired` varchar(50) NOT NULL,
  `disable` int(3) NOT NULL,
  `status` int(3) NOT NULL,
  `lastlogin` datetime NOT NULL,
  `lastupdate` datetime NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_user`
--

INSERT INTO `sys_user` (`userid`, `username`, `password`, `expired`, `disable`, `status`, `lastlogin`, `lastupdate`) VALUES
('admin2', 'admin2', '3YmVuam8zcasafgbenjo3', ' -', 0, 1, '2013-12-11 14:19:33', '2013-12-11 14:19:33'),
('ari', 'ari', '3YnNqcasafgbsj', '-', 0, 1, '2013-09-26 23:12:34', '2013-09-26 23:12:34'),
('xcv', 'xcv', '6eWR3dghjcydw', '23-04-2013', 1, 1, '2013-09-27 08:06:16', '2013-09-27 08:06:16');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_usergroups`
--

CREATE TABLE IF NOT EXISTS `sys_usergroups` (
  `username` varchar(100) NOT NULL,
  `usergroups` varchar(100) NOT NULL,
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_usergroups`
--

INSERT INTO `sys_usergroups` (`username`, `usergroups`) VALUES
('ari', 'Test1'),
('ari', 'IT Admin'),
('xcv', 'IT Admin'),
('xcv', 'All'),
('admin2', 'Test1'),
('admin2', 'IT Admin'),
('admin2', 'Admin Operational'),
('admin2', 'Test2');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbdetailtrans`
--

CREATE TABLE IF NOT EXISTS `tbdetailtrans` (
  `period` varchar(20) NOT NULL,
  `key_no` int(10) NOT NULL,
  `no` int(4) NOT NULL,
  `trans_no` varchar(50) NOT NULL,
  `item_part` varchar(100) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `qty` varchar(10) NOT NULL,
  `price` varchar(20) NOT NULL,
  `price_tot` varchar(20) NOT NULL,
  `unit` varchar(10) NOT NULL,
  `tanggal_bth` varchar(100) NOT NULL,
  `ket` varchar(200) NOT NULL,
  `trans_date` varchar(100) NOT NULL,
  `user` varchar(100) NOT NULL,
  `updated` varchar(100) NOT NULL,
  PRIMARY KEY (`key_no`),
  KEY `trans_no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tbdetailtrans`
--

INSERT INTO `tbdetailtrans` (`period`, `key_no`, `no`, `trans_no`, `item_part`, `item_name`, `qty`, `price`, `price_tot`, `unit`, `tanggal_bth`, `ket`, `trans_date`, `user`, `updated`) VALUES
('201307', 1, 1, 'PO2013771', 'OS-MK-6-001', 'os mikrotik level 6 versi 5.4', '1', '950,000', '950,000', 'license', '07/07/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 13, 1, 'PO20132171', 'OS-MK-6-001', 'os mikrotik level 6 versi 5.4', '1', '950,000', '950,000', 'license', '07/21/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 14, 2, 'PO20132171', 'sql-server-4-001', 'dvd sql server 2008 r2', '1', '1,100', '1,100', 'biji', '07/21/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 15, 3, 'PO20132171', 'OS-WIN-SERVER-001', 'os windows server 2008 r2 cal', '1', '8,000,000', '8,000,000', 'license', '07/21/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 16, 1, 'PO20132271', 'OS-MK-6-001', 'os mikrotik level 6 versi 5.4', '2', '950,000', '1,900,000', 'license', '07/22/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 17, 2, 'PO20132271', 'ram-ddr3-4-001', 'ram ddr3 4 gb', '1', '12,000', '12,000', 'biji', '07/22/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 18, 3, 'PO20132271', 'ram-ddr3-4-002', 'ram ddr3 4 gb vgen', '2', '14,000', '28,000', 'biji', '07/22/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 19, 4, 'PO20132271', 'sql-server-4-001', 'dvd sql server 2008 r2', '2', '1,100', '2,200', 'biji', '07/22/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 20, 5, 'PO20132271', 'OS-WIN-SERVER-001', 'os windows server 2008 r2 cal', '2', '8,000,000', '16,000,000', 'license', '07/22/2013', '', 'xxx', 'xxxx', 'xxxx'),
('201307', 21, 1, 'PO20132371', 'OS-MK-6-001', 'os mikrotik level 6 versi 5.4', '1', '950,000', '950,000', 'license', '07/23/2013', '', '20130723', 'xxxx', 'xxxx'),
('201307', 22, 1, 'PO20132671', 'ram-ddr3-4-001', 'ram ddr3 4 gb', '1', '12,000', '12,000', 'biji', '07/26/2013', '', '20130726', 'xxxx', 'xxxx'),
('201307', 23, 2, 'PO20132671', 'ram-ddr3-4-002', 'ram ddr3 4 gb vgen', '2', '14,000', '28,000', 'biji', '07/26/2013', '', '20130726', 'xxxx', 'xxxx'),
('201307', 24, 1, 'PO20133171', 'ram-ddr3-4-001', 'ram ddr3 4 gb', '1', '12,000', '12,000', 'biji', '07/31/2013', '', '20130731', 'xxxx', 'xxxx'),
('201307', 25, 2, 'PO20133171', 'sql-server-4-001', 'dvd sql server 2008 r2', '2', '1,100', '2,200', 'biji', '07/31/2013', '', '20130731', 'xxxx', 'xxxx'),
('201307', 26, 3, 'PO20133171', 'OS-WIN-SERVER-001', 'os windows server 2008 r2 cal', '10', '8,000,000', '80,000,000', 'license', '07/31/2013', '', '20130731', 'xxxx', 'xxxx'),
('201307', 27, 4, 'PO20133171', 'OS-MK-6-001', 'os mikrotik level 6 versi 5.4', '1', '950,000', '950,000', 'license', '08/22/2013', '', '20130731', 'xxxx', 'xxxx'),
('201308', 28, 1, 'PO20133081', 'OS-MK-6-001', 'os mikrotik level 6 versi 5.4', '2', '950,000', '1,900,000', 'license', '08/21/2013', '', '20130830', 'xxxx', 'xxxx'),
('201308', 29, 2, 'PO20133081', 'sql-server-4-001', 'dvd sql server 2008 r2', '2', '1,100', '2,200', 'biji', '08/21/2013', '', '20130830', 'xxxx', 'xxxx'),
('201308', 30, 3, 'PO20133081', 'OS-WIN-SERVER-001', 'os windows server 2008 r2 cal', '2', '8,000,000', '16,000,000', 'license', '08/21/2013', '', '20130830', 'xxxx', 'xxxx'),
('201308', 31, 4, 'PO20133081', 'ram-ddr3-4-002', 'ram ddr3 4 gb vgen', '10', '14,000', '140,000', 'biji', '08/21/2013', '', '20130830', 'xxxx', 'xxxx');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbheaderpo`
--

CREATE TABLE IF NOT EXISTS `tbheaderpo` (
  `key_no` int(11) NOT NULL,
  `period` int(11) NOT NULL,
  `trans_no` varchar(100) NOT NULL,
  `trans_date` int(11) NOT NULL,
  `goods_status` varchar(50) NOT NULL,
  `depaterment` varchar(100) NOT NULL,
  `date_po` varchar(100) NOT NULL,
  `supplier` varchar(300) NOT NULL,
  `supplier_address` varchar(300) NOT NULL,
  `delivery_address` varchar(100) NOT NULL,
  `sub_total` varchar(10) NOT NULL,
  `ppn` varchar(10) NOT NULL,
  `diskon` varchar(10) NOT NULL,
  `grand_total` varchar(100) NOT NULL,
  `amount` varchar(100) NOT NULL,
  `user` varchar(100) NOT NULL,
  `created` varchar(100) NOT NULL,
  `updated` varchar(100) NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tbheaderpo`
--

INSERT INTO `tbheaderpo` (`key_no`, `period`, `trans_no`, `trans_date`, `goods_status`, `depaterment`, `date_po`, `supplier`, `supplier_address`, `delivery_address`, `sub_total`, `ppn`, `diskon`, `grand_total`, `amount`, `user`, `created`, `updated`) VALUES
(18, 201309, 'PO20131091', 20130910, '1', 'IT', '2013-09-10', '', ' ', ' ', '0', '0', '0', '0', 'Item 1', 'x', 'x', 'x'),
(16, 201308, 'PO20131881', 20130818, '1', 'IT', '2013-08-18', '', ' ', ' ', '0', '0', '0', '0', 'Item 1', 'x', 'x', 'x'),
(11, 201307, 'PO20132171', 0, '1', 'IT', '2013-07-21', 'CV.Jaya Comp', 'Harco Mangga Dua blok b95 Lantai3 Jakarta Utara', 'Kelapa Gading Bukit Indah Block G 26/27 Jakarta Utara', '0', '0', '0', '8,951,100', 'Item 1', 'x', 'x', 'x'),
(12, 201307, 'PO20132271', 0, '1', 'IT', '2013-07-22', 'CV. JAYA SAKTI', 'Harco mangga dua ', 'Kelapa gading ', '0', '0', '0', '17,942,200', 'Item 1', 'x', 'x', 'x'),
(13, 201307, 'PO20132371', 20130723, '1', 'IT', '2013-07-23', 'bnb', 'nbnb nbnbn', 'bnb nbnb', '0', '0', '0', '950,000', 'Item 1', 'x', 'x', 'x'),
(14, 201307, 'PO20132671', 20130726, '1', 'IT', '2013-07-26', '', ' ', ' ', '0', '0', '0', '40,000', 'Item 1', 'x', 'x', 'x'),
(17, 201308, 'PO20133081', 20130830, '1', 'IT', '2013-08-30', '', ' ', ' ', '0', '0', '0', '18,042,200', 'Item 1', 'x', 'x', 'x'),
(15, 201307, 'PO20133171', 20130731, '1', 'IT', '2013-07-31', 'Cv. Jaya Sakti', 'Harco Mangga Dua Lantai 38B ', 'Kelapa Gading Bukit Indah Block G 26/27 ', '0', '0', '0', '80,964,200', 'Item 1', 'x', 'x', 'x'),
(1, 201307, 'PO2013771', 0, '1', 'IT', '2013-07-07', 'jakart', 'jakarta jakarta', 'jakarya aad', '0', '0', '0', '950,000', 'Item 1', 'x', 'x', 'x');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbitem`
--

CREATE TABLE IF NOT EXISTS `tbitem` (
  `item_part` varchar(100) NOT NULL,
  `item_name` varchar(300) NOT NULL,
  `item_group` varchar(100) NOT NULL,
  `item_type` varchar(100) NOT NULL,
  `status_active` int(11) NOT NULL,
  `unit` varchar(50) NOT NULL,
  `price_1` int(10) NOT NULL,
  `price_2` int(10) NOT NULL,
  `Do_Date_1` date NOT NULL,
  `Do_Date_2` date NOT NULL,
  `user` varchar(100) NOT NULL,
  `last_update` int(100) NOT NULL,
  PRIMARY KEY (`item_part`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tbitem`
--

INSERT INTO `tbitem` (`item_part`, `item_name`, `item_group`, `item_type`, `status_active`, `unit`, `price_1`, `price_2`, `Do_Date_1`, `Do_Date_2`, `user`, `last_update`) VALUES
('OS-MK-6-001', 'os mikrotik level 6 versi 5.4', 'os', 'license', 1, 'license', 950000, 1000000, '2013-01-15', '2011-06-29', 'ari', 10),
('OS-WIN-SERVER-001', 'os windows server 2008 r2 cal', 'os', 'license', 1, 'license', 800, 8000000, '2013-10-15', '2013-06-29', 'ari', 10),
('ram-ddr3-4-001', 'ram ddr3 4 gb', 'ram', 'hardware', 1, 'biji', 1000, 12000, '2013-01-12', '2013-02-15', 'ari', 10),
('ram-ddr3-4-002', 'ram ddr3 4 gb vgen', 'ram', 'hardware', 1, 'biji', 1400, 14000, '2014-01-01', '2014-06-15', 'ari', 10),
('sql-server-4-001', 'dvd sql server 2008 r2', 'sql', 'software', 1, 'biji', 1100, 13000, '2013-05-29', '2014-02-15', 'ari', 10);

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `sys_groupsprogram`
--
ALTER TABLE `sys_groupsprogram`
  ADD CONSTRAINT `sys_groupsprogram_ibfk_1` FOREIGN KEY (`groupno`) REFERENCES `sys_groups` (`groupno`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `sys_usergroups`
--
ALTER TABLE `sys_usergroups`
  ADD CONSTRAINT `sys_usergroups_ibfk_1` FOREIGN KEY (`username`) REFERENCES `sys_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tbdetailtrans`
--
ALTER TABLE `tbdetailtrans`
  ADD CONSTRAINT `tbdetailtrans_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `tbheaderpo` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
