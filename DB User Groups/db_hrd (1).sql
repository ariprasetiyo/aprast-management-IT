-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 01. Oktober 2014 jam 11:48
-- Versi Server: 5.5.16
-- Versi PHP: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `db_hrd`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_pelamar_pendidikan`
--

CREATE TABLE IF NOT EXISTS `detail_pelamar_pendidikan` (
  `trans_no` varchar(15) NOT NULL,
  `tingkat` varchar(50) NOT NULL,
  `nama_sekolah` varchar(100) NOT NULL,
  `jurusan` varchar(50) NOT NULL,
  `tahun1` varchar(20) NOT NULL,
  `created_dated` datetime NOT NULL,
  KEY `trans_no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_pelamar_pendidikan`
--

INSERT INTO `detail_pelamar_pendidikan` (`trans_no`, `tingkat`, `nama_sekolah`, `jurusan`, `tahun1`, `created_dated`) VALUES
('P2014072', '212', 'dsds', '121', '2014 - 2014', '2014-07-20 11:11:46'),
('P2014071', 'asa', 'sas', 'asa', '2014 - 2014', '2014-07-21 23:53:15');

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_pelamar_pendidikan_non_formal`
--

CREATE TABLE IF NOT EXISTS `detail_pelamar_pendidikan_non_formal` (
  `trans_no` varchar(15) NOT NULL,
  `tema` varchar(100) NOT NULL,
  `penyelengara` varchar(100) NOT NULL,
  `waktu` varchar(10) NOT NULL,
  `created_dated` datetime NOT NULL,
  KEY `trans_no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_pelamar_pendidikan_non_formal`
--

INSERT INTO `detail_pelamar_pendidikan_non_formal` (`trans_no`, `tema`, `penyelengara`, `waktu`, `created_dated`) VALUES
('P2014072', 'asds', '21212', '07-03-2014', '2014-07-20 11:11:46'),
('P2014071', 'sas', 'asas', '07-03-2014', '2014-07-21 23:53:15');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_absensi`
--

CREATE TABLE IF NOT EXISTS `header_absensi` (
  `nik` varchar(20) NOT NULL,
  `nama` varchar(200) NOT NULL,
  `jenis_kelamin` varchar(2) NOT NULL,
  `jabatan` varchar(50) NOT NULL,
  `cabang` varchar(100) NOT NULL,
  `cabang_lokasi` varchar(200) NOT NULL,
  `masuk` int(2) NOT NULL,
  `absen` int(2) NOT NULL,
  `izin_cuti` int(2) NOT NULL,
  `lembur` int(2) NOT NULL,
  `sakit` int(2) NOT NULL,
  `periode` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  KEY `nama` (`nama`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_absensi`
--

INSERT INTO `header_absensi` (`nik`, `nama`, `jenis_kelamin`, `jabatan`, `cabang`, `cabang_lokasi`, `masuk`, `absen`, `izin_cuti`, `lembur`, `sakit`, `periode`, `created_date`) VALUES
('NIK2014072', 'ari', 'P', 'Marketing', 'Kantor Cabang Pembantu (KCP)', 'Jawa Barat - Karawang', 3, 1, 0, 0, 1, 201407, '2014-07-17 21:55:39'),
('NIK2014071', 'sas', 'P', 'Direktur', 'Kantor Cabang Pembantu (KCP)', 'Jawa Barat - Karawang', 1, 1, 21, 3, 1, 201407, '2014-07-17 21:55:39');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_cuti`
--

CREATE TABLE IF NOT EXISTS `header_cuti` (
  `no_cuti` varchar(25) NOT NULL,
  `key_no` int(11) NOT NULL,
  `nik` varchar(30) NOT NULL,
  `keperluan` varchar(200) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `tanggal_keluar` varchar(20) NOT NULL,
  `tanggal_masuk` varchar(20) NOT NULL,
  `jumlah_cuti` int(2) NOT NULL,
  `telphone` int(14) NOT NULL,
  `create_dated` datetime NOT NULL,
  `periode` varchar(10) NOT NULL,
  `periode_cuti` varchar(100) NOT NULL,
  PRIMARY KEY (`no_cuti`),
  KEY `nik` (`nik`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_cuti`
--

INSERT INTO `header_cuti` (`no_cuti`, `key_no`, `nik`, `keperluan`, `nama`, `tanggal_keluar`, `tanggal_masuk`, `jumlah_cuti`, `telphone`, `create_dated`, `periode`, `periode_cuti`) VALUES
('CT2014071', 2, 'NIK2014071', 'asas', 'sas', '15-02-2012', '22-10-2014', 11, 111, '2014-07-13 00:16:43', '201407', 'sasaaaaaaaaaaa'),
('CT2014072', 2, 'NIK2014071', '1212', 'sas', '15-02-2012', '22-10-2014', 10, 111, '2014-07-13 00:16:29', '201407', '212');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_gaji`
--

CREATE TABLE IF NOT EXISTS `header_gaji` (
  `nik` varchar(20) NOT NULL,
  `periode` varchar(15) NOT NULL,
  `nama` varchar(200) NOT NULL,
  `jk` varchar(10) NOT NULL,
  `jabatan` varchar(50) NOT NULL,
  `gaji_pokok` double NOT NULL,
  `t_jabatan` double NOT NULL,
  `t_makan` double NOT NULL,
  `t_transport` double NOT NULL,
  `t_jamsostek` double NOT NULL,
  `t_kesehatan` double NOT NULL,
  `t_lainnya` double NOT NULL,
  `lembur` double NOT NULL,
  `bonus` double NOT NULL,
  `pajak` double NOT NULL,
  `pinjaman_karyawan` double NOT NULL,
  `pinjaman_lainnya` double NOT NULL,
  `pot_jamsostek` double NOT NULL,
  `asr_kesehatan` double NOT NULL,
  `denda_kedisipliman` double NOT NULL,
  `create_dated` datetime NOT NULL,
  KEY `nik` (`nik`,`periode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_gaji`
--

INSERT INTO `header_gaji` (`nik`, `periode`, `nama`, `jk`, `jabatan`, `gaji_pokok`, `t_jabatan`, `t_makan`, `t_transport`, `t_jamsostek`, `t_kesehatan`, `t_lainnya`, `lembur`, `bonus`, `pajak`, `pinjaman_karyawan`, `pinjaman_lainnya`, `pot_jamsostek`, `asr_kesehatan`, `denda_kedisipliman`, `create_dated`) VALUES
('NIK2014072', '201407', 'ari', 'L', 'Marketing', 10000000, 0, 66000, 66000, 123800, 13800, 0, 0, 0, 0, 0, 10, 0, 0, 0, '2014-07-20 11:18:16');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_lembur`
--

CREATE TABLE IF NOT EXISTS `header_lembur` (
  `key_no` int(11) NOT NULL,
  `no_lembur` varchar(20) NOT NULL,
  `nik` varchar(20) NOT NULL,
  `nama` varchar(200) NOT NULL,
  `ket1` text NOT NULL,
  `ket2` text NOT NULL,
  `tanggal1` varchar(50) NOT NULL,
  `tanggal2` varchar(50) NOT NULL,
  `tanggal3` varchar(50) NOT NULL,
  `tanggal4` varchar(50) NOT NULL,
  `periode` int(11) NOT NULL,
  `create_dated` datetime NOT NULL,
  `jam1` double DEFAULT NULL,
  `jam2` double DEFAULT NULL,
  `jam3` double DEFAULT NULL,
  `jam4` double DEFAULT NULL,
  PRIMARY KEY (`no_lembur`),
  KEY `nik` (`nik`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_lembur`
--

INSERT INTO `header_lembur` (`key_no`, `no_lembur`, `nik`, `nama`, `ket1`, `ket2`, `tanggal1`, `tanggal2`, `tanggal3`, `tanggal4`, `periode`, `create_dated`, `jam1`, `jam2`, `jam3`, `jam4`) VALUES
(1, 'LB2014071', 'NIK2014072', 'ari', 'sasa', 'asasasas', '21-07-2014', '21-07-2014', '21-07-2014', '21-07-2014', 201407, '2014-07-21 23:52:40', 1405961433001, 1405961433025, 1405961433107, 1405961433152),
(2, 'LB2014072', 'NIK2014072', 'ari', 'sasa', 'asasasas', '21-07-2014', '21-07-2014', '21-07-2014', '21-07-2014', 201407, '2014-07-21 23:52:48', 1405961433001, 1405961433025, 1405961433107, 1405961433152),
(3, 'LB2014073', 'NIK2014072', 'ari', 'sasa', 'asasasas', '21-07-2014', '21-07-2014', '21-07-2014', '21-07-2014', 201407, '2014-07-21 23:52:56', 1405961433001, 1405961433025, 1405961433107, 1405961433152),
(4, 'LB2014074', 'NIK2014071', 'ari', 'dsd', 'sdsdsd', '21-07-2014', '21-07-2014', '21-07-2014', '21-07-2014', 201407, '2014-07-21 23:53:42', 1405961602618, 1405961602633, 1405961602726, 1405961602766),
(5, 'LB2014075', 'NIK2014071', 'ari', 'dsd', 'sdsdsd', '21-07-2014', '21-07-2014', '21-07-2014', '21-07-2014', 201407, '2014-07-21 23:53:58', 1405961602618, 1405961602633, 1405961602726, 1405961602766);

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_pegawai`
--

CREATE TABLE IF NOT EXISTS `header_pegawai` (
  `no_pelamar` varchar(100) NOT NULL,
  `key_no` int(11) NOT NULL,
  `nik` varchar(100) NOT NULL,
  `cabang` varchar(100) NOT NULL,
  `cabang_lokasi` varchar(100) NOT NULL,
  `jabatan` varchar(100) NOT NULL,
  `cuti` int(2) NOT NULL,
  `gaji_pokok` int(14) NOT NULL,
  `periode` varchar(20) NOT NULL,
  `status_pekerja` int(1) NOT NULL,
  PRIMARY KEY (`nik`),
  KEY `no_pelamar` (`no_pelamar`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_pegawai`
--

INSERT INTO `header_pegawai` (`no_pelamar`, `key_no`, `nik`, `cabang`, `cabang_lokasi`, `jabatan`, `cuti`, `gaji_pokok`, `periode`, `status_pekerja`) VALUES
('P2014071', 1, 'NIK2014071', 'Kantor Cabang Pembantu (KCP)', 'Jawa Barat - Karawang', 'Direktur', 211, 2121, '201407', 0),
('P2014072', 2, 'NIK2014072', 'Kantor Cabang Pembantu (KCP)', 'Jawa Barat - Karawang', 'Marketing', 12, 10000000, '201407', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_pelamar`
--

CREATE TABLE IF NOT EXISTS `header_pelamar` (
  `trans_no` varchar(15) NOT NULL,
  `nama` varchar(300) NOT NULL,
  `kelahiran_tanggal` varchar(10) NOT NULL,
  `kelahiran_tempat` varchar(50) NOT NULL,
  `jenis_kelamain` int(1) NOT NULL,
  `status_nikah` int(1) NOT NULL,
  `agama` varchar(15) NOT NULL,
  `kewarganegaraan` varchar(100) NOT NULL,
  `no_ktp` int(100) NOT NULL,
  `no_sim` int(100) NOT NULL,
  `alamat` varchar(300) NOT NULL,
  `kode_pos` int(10) NOT NULL,
  `no_telphone` int(14) NOT NULL,
  `no_hp` int(14) NOT NULL,
  `email` varchar(50) NOT NULL,
  `status_tempat_tinggal` int(1) NOT NULL,
  `hobby` varchar(100) NOT NULL,
  `status_pelamar` varchar(2) NOT NULL,
  `created_date` datetime NOT NULL,
  `key_no` int(11) NOT NULL,
  `periode` varchar(20) NOT NULL,
  `tanggal_dibuat` varchar(20) NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_pelamar`
--

INSERT INTO `header_pelamar` (`trans_no`, `nama`, `kelahiran_tanggal`, `kelahiran_tempat`, `jenis_kelamain`, `status_nikah`, `agama`, `kewarganegaraan`, `no_ktp`, `no_sim`, `alamat`, `kode_pos`, `no_telphone`, `no_hp`, `email`, `status_tempat_tinggal`, `hobby`, `status_pelamar`, `created_date`, `key_no`, `periode`, `tanggal_dibuat`) VALUES
('P2014071', 'sas', '07-03-2014', 'asas', 0, 0, 'sas', 'asa', 212, 12, 'asas', 212, 1212, 212, '2121', 0, 'asas', '0', '2014-07-21 23:53:15', 1, '201407', '9 - 7 - 2014'),
('P2014072', 'ari', '07-03-2014', 'klaten', 0, 0, 'sa', 'asa', 121121, 1212, 'asas', 212, 121211, 2323, '323', 0, 'asas', '0', '2014-07-20 11:11:46', 2, '201407', '13 - 7 - 2014');

-- --------------------------------------------------------

--
-- Struktur dari tabel `settinguser`
--

CREATE TABLE IF NOT EXISTS `settinguser` (
  `user` varchar(200) NOT NULL,
  `password` varchar(500) NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `settinguser`
--

INSERT INTO `settinguser` (`user`, `password`, `created_date`) VALUES
('ari', 'c3ec0f7b054e729c5a716c8125839829', '2014-09-25 13:07:03'),
('coba', 'c3ec0f7b054e729c5a716c8125839829', '2014-09-25 13:07:27'),
('siti', 'db04eb4b07e0aaf8d1d477ae342bdff9', '2014-07-19 17:15:20');

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `detail_pelamar_pendidikan`
--
ALTER TABLE `detail_pelamar_pendidikan`
  ADD CONSTRAINT `detail_pelamar_pendidikan_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_pelamar` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_pelamar_pendidikan_non_formal`
--
ALTER TABLE `detail_pelamar_pendidikan_non_formal`
  ADD CONSTRAINT `detail_pelamar_pendidikan_non_formal_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_pelamar` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `header_pegawai`
--
ALTER TABLE `header_pegawai`
  ADD CONSTRAINT `header_pegawai_ibfk_1` FOREIGN KEY (`no_pelamar`) REFERENCES `header_pelamar` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
