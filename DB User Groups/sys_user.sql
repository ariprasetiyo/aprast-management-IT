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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
