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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
