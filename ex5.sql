-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 29, 2023 at 08:46 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ex5`
--

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `credit` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `requirement_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`credit`, `id`, `course_name`, `requirement_type`) VALUES
(4, 152, 'תכנות אינטרנט א\'', 'חובה מדעי המחשב'),
(4, 153, 'תכנות אינטרנט ב\'', 'חובה מדעי המחשב'),
(4, 154, 'מתמטיקה דיסקרטית', 'חובה מדעי המחשב'),
(4, 155, 'מבוא למדעי המחשב', 'חובה מדעי המחשב'),
(3, 156, 'בינה מלאכותית', 'בחירה מדעי המחשב'),
(3, 157, 'אלגברה ליניארית א\'', 'חובה מדעי המחשב'),
(2, 158, 'אבולוציה', 'בחירה כללי'),
(3, 159, 'חישה, תפישה וזכרון', 'בחירה כללי'),
(4, 160, 'החברה הערבית בישראל', 'בחירה כללי'),
(3, 161, 'ארכיטקטורות מחשבים', 'בחירה מדעי המחשב'),
(3, 162, 'לוגיקה למדעי המחשב', 'בחירה מדעי המחשב'),
(3, 163, 'בינה מלאכותית ברפואה', 'בחירה מדעי המחשב');

-- --------------------------------------------------------

--
-- Table structure for table `course_seq`
--

CREATE TABLE `course_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course_seq`
--

INSERT INTO `course_seq` (`next_val`) VALUES
(251);

-- --------------------------------------------------------

--
-- Table structure for table `degree_requirement`
--

CREATE TABLE `degree_requirement` (
  `mandatory_credits` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `requirement_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `degree_requirement`
--

INSERT INTO `degree_requirement` (`mandatory_credits`, `id`, `requirement_name`) VALUES
(24, 2, 'בחירה מדעי המחשב'),
(120, 3, 'חובה מדעי המחשב'),
(20, 4, 'בחירה כללי');

-- --------------------------------------------------------

--
-- Table structure for table `degree_requirement_seq`
--

CREATE TABLE `degree_requirement_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `degree_requirement_seq`
--

INSERT INTO `degree_requirement_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Table structure for table `spring_session`
--

CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `spring_session`
--

INSERT INTO `spring_session` (`PRIMARY_ID`, `SESSION_ID`, `CREATION_TIME`, `LAST_ACCESS_TIME`, `MAX_INACTIVE_INTERVAL`, `EXPIRY_TIME`, `PRINCIPAL_NAME`) VALUES
('119e1b74-341f-41ce-b283-31cbb563b171', 'ddc0ec81-5bfe-4333-ab9b-ee694b4cd0fd', 1688063645839, 1688064280399, 1800, 1688066080399, 'Miriam'),
('efd2658f-1991-4913-9e4d-afd73c49fdd1', '00a0a164-a9e9-4957-a94a-c225f92e99e1', 1688060985447, 1688064246451, 1800, 1688066046451, 'Solange');

-- --------------------------------------------------------

--
-- Table structure for table `spring_session_attributes`
--

CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `spring_session_attributes`
--

INSERT INTO `spring_session_attributes` (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`, `ATTRIBUTE_BYTES`) VALUES
('119e1b74-341f-41ce-b283-31cbb563b171', 'org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN', 0xaced0005737200366f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e637372662e44656661756c7443737266546f6b656e5aefb7c82fa2fbd50200034c000a6865616465724e616d657400124c6a6176612f6c616e672f537472696e673b4c000d706172616d657465724e616d6571007e00014c0005746f6b656e71007e0001787074000c582d435352462d544f4b454e7400055f6373726674002436363862313239302d633535642d343230622d616364392d643331386634613433613563),
('119e1b74-341f-41ce-b283-31cbb563b171', 'SPRING_SECURITY_CONTEXT', 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c00000000000002620200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e00000000000002620200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200426f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e617574686f726974792e53696d706c654772616e746564417574686f7269747900000000000002620200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b7870740009524f4c455f555345527871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c7300000000000002620200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f787074000f303a303a303a303a303a303a303a3174002439656363333735312d656363362d346263622d623339312d64393439613962313036363970737200326f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657200000000000002620200075a00116163636f756e744e6f6e457870697265645a00106163636f756e744e6f6e4c6f636b65645a001563726564656e7469616c734e6f6e457870697265645a0007656e61626c65644c000b617574686f72697469657374000f4c6a6176612f7574696c2f5365743b4c000870617373776f726471007e000f4c0008757365726e616d6571007e000f787001010101737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007871007e000a737200116a6176612e7574696c2e54726565536574dd98509395ed875b0300007870737200466f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657224417574686f72697479436f6d70617261746f720000000000000262020000787077040000000171007e001078707400064d697269616d),
('119e1b74-341f-41ce-b283-31cbb563b171', 'SPRING_SECURITY_SAVED_REQUEST', 0xaced0005737200416f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e7361766564726571756573742e44656661756c74536176656452657175657374000000000000026202000f49000a736572766572506f72744c000b636f6e74657874506174687400124c6a6176612f6c616e672f537472696e673b4c0007636f6f6b6965737400154c6a6176612f7574696c2f41727261794c6973743b4c00076865616465727374000f4c6a6176612f7574696c2f4d61703b4c00076c6f63616c657371007e00024c001c6d61746368696e6752657175657374506172616d657465724e616d6571007e00014c00066d6574686f6471007e00014c000a706172616d657465727371007e00034c000870617468496e666f71007e00014c000b7175657279537472696e6771007e00014c000a7265717565737455524971007e00014c000a7265717565737455524c71007e00014c0006736368656d6571007e00014c000a7365727665724e616d6571007e00014c000b736572766c65745061746871007e0001787000001f90740000737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000003770400000003737200396f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e7361766564726571756573742e5361766564436f6f6b696500000000000002620200084900066d61784167655a000673656375726549000776657273696f6e4c0007636f6d6d656e7471007e00014c0006646f6d61696e71007e00014c00046e616d6571007e00014c00047061746871007e00014c000576616c756571007e00017870ffffffff0000000000707074000d496465612d34326130613565667074002439313037323363642d323735392d343534372d616263662d3939336230646565333133337371007e0008ffffffff0000000000707074001157656273746f726d2d62346562623033357074002430316163613932372d646162372d343531332d626433312d3239386431663338363432627371007e0008ffffffff0000000000707074000753455353494f4e707400304e32466c597a4530595467744d474979596930304d5755334c5467324e474d744e7a67314d5445335a6d49335a444d7878737200116a6176612e7574696c2e547265654d61700cc1f63e2d256ae60300014c000a636f6d70617261746f727400164c6a6176612f7574696c2f436f6d70617261746f723b78707372002a6a6176612e6c616e672e537472696e672443617365496e73656e736974697665436f6d70617261746f7277035c7d5c50e5ce02000078707704000000107400066163636570747371007e000600000001770400000001740087746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f617669662c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e377874000f6163636570742d656e636f64696e677371007e000600000001770400000001740011677a69702c206465666c6174652c2062727874000f6163636570742d6c616e67756167657371007e000600000001770400000001740023656e2d55532c656e3b713d302e392c68652d494c3b713d302e382c68653b713d302e377874000a636f6e6e656374696f6e7371007e00060000000177040000000174000a6b6565702d616c69766578740006636f6f6b69657371007e0006000000017704000000017400a4496465612d34326130613565663d39313037323363642d323735392d343534372d616263662d3939336230646565333133333b2057656273746f726d2d62346562623033353d30316163613932372d646162372d343531332d626433312d3239386431663338363432623b2053455353494f4e3d4e32466c597a4530595467744d474979596930304d5755334c5467324e474d744e7a67314d5445335a6d49335a444d7878740004686f73747371007e00060000000177040000000174000e6c6f63616c686f73743a3830383078740007726566657265727371007e00060000000177040000000174002b687474703a2f2f6c6f63616c686f73743a383038302f757365722f616464546f53747564656e744c697374787400097365632d63682d75617371007e000600000001770400000001740040224e6f742e412f4272616e64223b763d2238222c20224368726f6d69756d223b763d22313134222c2022476f6f676c65204368726f6d65223b763d2231313422787400107365632d63682d75612d6d6f62696c657371007e0006000000017704000000017400023f30787400127365632d63682d75612d706c6174666f726d7371007e0006000000017704000000017400092257696e646f7773227874000e7365632d66657463682d646573747371007e000600000001770400000001740008646f63756d656e747874000e7365632d66657463682d6d6f64657371007e0006000000017704000000017400086e617669676174657874000e7365632d66657463682d736974657371007e00060000000177040000000174000b73616d652d6f726967696e7874000e7365632d66657463682d757365727371007e0006000000017704000000017400023f3178740019757067726164652d696e7365637572652d72657175657374737371007e000600000001770400000001740001317874000a757365722d6167656e747371007e00060000000177040000000174006f4d6f7a696c6c612f352e30202857696e646f7773204e542031302e303b2057696e36343b2078363429204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f3131342e302e302e30205361666172692f3533372e333678787371007e000600000004770400000004737200106a6176612e7574696c2e4c6f63616c657ef811609c30f9ec03000649000868617368636f64654c0007636f756e74727971007e00014c000a657874656e73696f6e7371007e00014c00086c616e677561676571007e00014c000673637269707471007e00014c000776617269616e7471007e00017870ffffffff740002555371007e0005740002656e71007e000571007e0005787371007e0048ffffffff71007e000571007e000571007e004b71007e000571007e0005787371007e0048ffffffff740002494c71007e0005740002686571007e000571007e0005787371007e0048ffffffff71007e000571007e000571007e004f71007e000571007e00057878740008636f6e74696e75657400034745547371007e00127077040000000078707074000f2f757365722f6d79436f7572736573740024687474703a2f2f6c6f63616c686f73743a383038302f757365722f6d79436f7572736573740004687474707400096c6f63616c686f737474000f2f757365722f6d79436f7572736573),
('efd2658f-1991-4913-9e4d-afd73c49fdd1', 'org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN', 0xaced0005737200366f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e637372662e44656661756c7443737266546f6b656e5aefb7c82fa2fbd50200034c000a6865616465724e616d657400124c6a6176612f6c616e672f537472696e673b4c000d706172616d657465724e616d6571007e00014c0005746f6b656e71007e0001787074000c582d435352462d544f4b454e7400055f6373726674002438616233376336322d373466382d343765662d383538662d666262393365316139373166),
('efd2658f-1991-4913-9e4d-afd73c49fdd1', 'SPRING_SECURITY_CONTEXT', 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c00000000000002620200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e00000000000002620200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200426f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e617574686f726974792e53696d706c654772616e746564417574686f7269747900000000000002620200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b787074000a524f4c455f41444d494e7871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c7300000000000002620200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f787074000f303a303a303a303a303a303a303a3174002434393334663665362d363565362d346538392d613934302d38353838626532383333333970737200326f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657200000000000002620200075a00116163636f756e744e6f6e457870697265645a00106163636f756e744e6f6e4c6f636b65645a001563726564656e7469616c734e6f6e457870697265645a0007656e61626c65644c000b617574686f72697469657374000f4c6a6176612f7574696c2f5365743b4c000870617373776f726471007e000f4c0008757365726e616d6571007e000f787001010101737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007871007e000a737200116a6176612e7574696c2e54726565536574dd98509395ed875b0300007870737200466f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657224417574686f72697479436f6d70617261746f720000000000000262020000787077040000000171007e00107870740007536f6c616e6765);

-- --------------------------------------------------------

--
-- Table structure for table `user_courses`
--

CREATE TABLE `user_courses` (
  `grade` int(11) DEFAULT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_courses`
--

INSERT INTO `user_courses` (`grade`, `course_id`, `id`, `username`) VALUES
(96, 152, 2, 'Miriam'),
(100, 153, 3, 'Miriam'),
(86, 159, 4, 'Miriam'),
(85, 156, 5, 'Miriam'),
(NULL, 160, 6, 'Miriam'),
(NULL, 161, 7, 'Miriam'),
(NULL, 157, 8, 'Miriam');

-- --------------------------------------------------------

--
-- Table structure for table `user_courses_seq`
--

CREATE TABLE `user_courses_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_courses_seq`
--

INSERT INTO `user_courses_seq` (`next_val`) VALUES
(101);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `degree_requirement`
--
ALTER TABLE `degree_requirement`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `spring_session`
--
ALTER TABLE `spring_session`
  ADD PRIMARY KEY (`PRIMARY_ID`),
  ADD UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  ADD KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  ADD KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`);

--
-- Indexes for table `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
  ADD PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`);

--
-- Indexes for table `user_courses`
--
ALTER TABLE `user_courses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlqgnqmcg0mn2ci98xag6vxpdq` (`course_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
  ADD CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE;

--
-- Constraints for table `user_courses`
--
ALTER TABLE `user_courses`
  ADD CONSTRAINT `FKlqgnqmcg0mn2ci98xag6vxpdq` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;