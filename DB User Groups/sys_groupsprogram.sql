-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 01. Oktober 2014 jam 11:46
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

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `sys_groupsprogram`
--
ALTER TABLE `sys_groupsprogram`
  ADD CONSTRAINT `sys_groupsprogram_ibfk_1` FOREIGN KEY (`groupno`) REFERENCES `sys_groups` (`groupno`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
