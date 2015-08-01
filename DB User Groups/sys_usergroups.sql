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

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `sys_usergroups`
--
ALTER TABLE `sys_usergroups`
  ADD CONSTRAINT `sys_usergroups_ibfk_1` FOREIGN KEY (`username`) REFERENCES `sys_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
