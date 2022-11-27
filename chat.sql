-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hostiteľ: 127.0.0.1
-- Čas generovania: Št 24.Nov 2022, 23:17
-- Verzia serveru: 10.4.24-MariaDB
-- Verzia PHP: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáza: `chat`
--

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `banned`
--

CREATE TABLE `banned` (
  `email` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Sťahujem dáta pre tabuľku `banned`
--

INSERT INTO `banned` (`email`) VALUES
('matus.weiss@student.ukf.sk');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `prispevky`
--

CREATE TABLE `prispevky` (
  `nickname` varchar(45) NOT NULL,
  `text` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Sťahujem dáta pre tabuľku `prispevky`
--

INSERT INTO `prispevky` (`nickname`, `text`) VALUES
('cierny_baca11', 'vitajte v nasom chate heh'),
('princ-krason2', 'dakujem ibi za tento super chat'),
('len_tvoj_ferri', 'kupim telefon do 10€'),
('len_tvoj_ferri', 'evr'),
('cierny_baca11', 'toto neni bazar!!!'),
('cierny_baca11', 'este raz a dam ti banan'),
('cierny_baca11', 'ban blby autocorrect'),
('CJ_grove2', 'this is america'),
('matusko_weiss', 'niekto zahrat csgo? silver2');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `meno` varchar(45) NOT NULL,
  `priezvisko` varchar(45) NOT NULL,
  `nickname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `passwd` varchar(45) NOT NULL,
  `admin` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Sťahujem dáta pre tabuľku `users`
--

INSERT INTO `users` (`id`, `meno`, `priezvisko`, `nickname`, `email`, `passwd`, `admin`) VALUES
(1, 'Ibi', 'Maiga', 'cierny_baca11', 'maiga.ibi@gmail.com', 'sedibaca1', 1),
(2, 'Pavol', 'Gerbera', 'princ-krason2', 'pali.prebera@student.ukf.sk', 'mamradnohy1', 0),
(3, 'Ferrari', 'Lakatos', 'len_tvoj_ferri', 'f.lakatos@zoznam.sk', 'ferriboss', 0),
(4, 'Matus', 'Weiss', 'matusko_weiss', 'matus.weiss@student.ukf.sk', 'matusko_csgo1', 0),
(5, 'Karol', 'Janovsky', 'CJ_grove2', 'carlito.johnson@yahoo.com', 'grovestreetforever', 0);

--
-- Kľúče pre exportované tabuľky
--

--
-- Indexy pre tabuľku `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pre exportované tabuľky
--

--
-- AUTO_INCREMENT pre tabuľku `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
