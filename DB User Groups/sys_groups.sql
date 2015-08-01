-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 01. Oktober 2014 jam 11:45
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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
