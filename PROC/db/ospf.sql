-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.6.24


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema docSec
--

CREATE DATABASE IF NOT EXISTS docSec;
USE docSec;

--
-- Definition of table `cfile`
--

DROP TABLE IF EXISTS `cfile`;
CREATE TABLE `cfile` (
  `cid` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '文件名 ',
  `size` bigint(20) unsigned DEFAULT NULL COMMENT '文件名 ',
  `type` varchar(10) NOT NULL COMMENT '文件类型 （1：pic 2：doc 3：video 4：audio 5: other 0：dir）',
  `url` varchar(200) NOT NULL COMMENT '路径',
  `crtDate` datetime NOT NULL COMMENT '创建日期',
  `uptDate` datetime DEFAULT NULL COMMENT '修改日期',
  `crtUser` varchar(36) NOT NULL COMMENT '创建用户',
  `uptUser` varchar(36) DEFAULT NULL COMMENT '修改用户',
  `partDirCid` varchar(36) DEFAULT NULL COMMENT '父目录ID',
  `delFlg` varchar(10) DEFAULT NULL COMMENT '删除标志',
  `delDate` datetime DEFAULT NULL COMMENT '删除日期',
  `officeType` varchar(10) DEFAULT NULL COMMENT 'office类型',
  PRIMARY KEY (`cid`),
  KEY `FK_cfile_1` (`partDirCid`),
  CONSTRAINT `FK_cfile_1` FOREIGN KEY (`partDirCid`) REFERENCES `cfile` (`cid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件';

--
-- Dumping data for table `cfile`
--

/*!40000 ALTER TABLE `cfile` DISABLE KEYS */;
INSERT INTO `cfile` (`cid`,`name`,`size`,`type`,`url`,`crtDate`,`uptDate`,`crtUser`,`uptUser`,`partDirCid`,`delFlg`,`delDate`,`officeType`) VALUES 
 ('402881e46910aa7b016910d461dd0001','aaa',NULL,'0','aa\\','2019-02-22 00:12:28',NULL,'0001',NULL,NULL,NULL,NULL,''),
 ('402881e46910aa7b016910d49fd30002','我的房产买卖合同.docx',22250,'2','02220012438570004.docx','2019-02-22 00:12:44',NULL,'0001',NULL,'402881e46910aa7b016910d461dd0001',NULL,NULL,'docx'),
 ('402881e46910f011016910f2e03e0000','t.docx',15125,'2','02220045464220000.docx','2019-02-22 00:45:46',NULL,'0001',NULL,'402881e46910aa7b016910d461dd0001',NULL,NULL,'docx'),
 ('402881e46910f011016910f34a330001','bb',NULL,'0','bb\\','2019-02-22 00:46:14',NULL,'0001',NULL,'402881e46910aa7b016910d461dd0001',NULL,NULL,''),
 ('402881e46910f011016910f3a8b20002','房屋买卖合同(房管局)_(2).doc',51712,'2','02220046377430001.doc','2019-02-22 00:46:38',NULL,'0001',NULL,'402881e46910f011016910f34a330001',NULL,NULL,'doc'),
 ('402881e46910f0110169110161270003','a.docx',14965,'2','02220101345180002.docx','2019-02-22 01:01:37',NULL,'0001',NULL,NULL,NULL,NULL,'docx'),
 ('402881e46910f01101691103cfa70004','tool3.xls',40960,'2','02220104162930003.xls','2019-02-22 01:04:16',NULL,'0001',NULL,'402881e46910f011016910f34a330001',NULL,NULL,'xls'),
 ('402881e46911214a016911256d990000','s.xlsx',8019,'2','02220140594050000.xlsx','2019-02-22 01:40:59',NULL,'0001',NULL,'402881e46910f011016910f34a330001',NULL,NULL,'xlsx'),
 ('402881e46911214a016911282f700001','111.ppt',86016,'2','02220144001090001.ppt','2019-02-22 01:44:00',NULL,'0001',NULL,'402881e46910f011016910f34a330001',NULL,NULL,'ppt'),
 ('402881e46911214a016911282ff30002','22222.pptx',30831,'2','02220144002410002.pptx','2019-02-22 01:44:00',NULL,'0001',NULL,'402881e46910f011016910f34a330001',NULL,NULL,'pptx'),
 ('402881e46911214a016911355bf70003','在Android应用中使用百度地图api.pdf',175738,'2','02220158234770003.pdf','2019-02-22 01:58:23',NULL,'0001',NULL,'402881e46910f011016910f34a330001',NULL,NULL,'pdf'),
 ('402881e469133a3001691361f897001b','Chrysanthemum.jpg',879394,'1','02221206215890027.jpg','2019-02-22 12:06:22',NULL,'0001',NULL,NULL,NULL,NULL,'jpg'),
 ('402881e469133a3001691361f8a4001c','Desert.jpg',845941,'1','02221206216020028.jpg','2019-02-22 12:06:22',NULL,'0001',NULL,NULL,NULL,NULL,'jpg');
/*!40000 ALTER TABLE `cfile` ENABLE KEYS */;


--
-- Definition of table `login`
--

DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `cid` varchar(36) NOT NULL,
  `cname` varchar(45) NOT NULL COMMENT '用户名 ',
  `cpwd` varchar(45) NOT NULL COMMENT '密码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录';

--
-- Dumping data for table `login`
--

/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` (`cid`,`cname`,`cpwd`) VALUES 
 ('001','admin','092a195d28cb88ad9b074930722aa3ba'),
 ('002','user','092a195d28cb88ad9b074930722aa3ba'),
 ('003','admin3','092a195d28cb88ad9b074930722aa3ba'),
 ('004','admin4','092a195d28cb88ad9b074930722aa3ba'),
 ('005','admin5','092a195d28cb88ad9b074930722aa3ba'),
 ('006','admin6','092a195d28cb88ad9b074930722aa3ba'),
 ('007','admin7','092a195d28cb88ad9b074930722aa3ba'),
 ('008','admin8','092a195d28cb88ad9b074930722aa3ba'),
 ('009','admin9','092a195d28cb88ad9b074930722aa3ba'),
 ('010','admin10','092a195d28cb88ad9b074930722aa3ba'),
 ('011','admin11','092a195d28cb88ad9b074930722aa3ba'),
 ('013','admin13','092a195d28cb88ad9b074930722aa3ba'),
 ('012','admin12','092a195d28cb88ad9b074930722aa3ba'),
 ('402881e467d6edcd0167d6ef06990000','11','092a195d28cb88ad9b074930722aa3ba'),
 ('402881e467d8c6370167d8d55c140000','2','092a195d28cb88ad9b074930722aa3ba'),
 ('402881e467da73f60167da77247c0000','gds','092a195d28cb88ad9b074930722aa3ba'),
 ('402881e467da95c80167da98696e0001','gds3','gds'),
 ('402881e467da9b850167da9bf46c0000','gds','gds'),
 ('402881e467da9b850167daa9f2690002','gds100','gds'),
 ('402881e467da9b850167daae8e770004','gds7','gds'),
 ('402881e467da9b850167dab0140b0006','gdszm8','gds'),
 ('402881e467da9b850167dab1415b0009','gdszm9','gds'),
 ('402881e467da9b850167dab1fd7b000b','gdszm10','gds'),
 ('402881e467da9b850167dab3093b000d','gdszm11','gds'),
 ('402881e467da9b850167dab3edfc000f','gdszm12','gds'),
 ('402881e467da9b850167dab5f2ad0011','gdszm15','gds'),
 ('402881e467da9b850167dab846d80013','gds17','gds'),
 ('402881e467da9b850167dac084f70015','gds22','gds'),
 ('402881e467da9b850167dac222c30017','gds34','gds'),
 ('402881e467da9b850167dade0bd40019','gdszm25','2e0c828b075b3b10d006e5ad02aa00d9'),
 ('402881e46854f48d0168550f6f940000','admin14','a561059cc183051f4255c6ce8014e1bf'),
 ('402881e4686bc0d901686bc282220000','admin20','a561059cc183051f4255c6ce8014e1bf'),
 ('402881e46874a62d016874ab03530000','22','092a195d28cb88ad9b074930722aa3ba'),
 ('402881e46874d8dc016874f624f10000','1','092a195d28cb88ad9b074930722aa3ba'),
 ('402881e46882a600016882a8af790000','gds111','a561059cc183051f4255c6ce8014e1bf'),
 ('402881e468f14eab0168f14f897a0000','admin22','a561059cc183051f4255c6ce8014e1bf');
/*!40000 ALTER TABLE `login` ENABLE KEYS */;


--
-- Definition of table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `cid` varchar(36) NOT NULL,
  `crealname` varchar(45) DEFAULT NULL COMMENT '姓名(读者名)',
  `studentId` varchar(36) DEFAULT NULL COMMENT '学号',
  `dept` varchar(60) DEFAULT NULL COMMENT '院系',
  `major` varchar(45) DEFAULT NULL COMMENT '专业',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `hobby` varchar(45) DEFAULT NULL COMMENT '兴趣爱好',
  `type` varchar(10) DEFAULT NULL COMMENT '类型(1:读者 2:管理员)',
  `loginCid` varchar(36) NOT NULL COMMENT '登录ID',
  `icon` varchar(100) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`cid`,`crealname`,`studentId`,`dept`,`major`,`email`,`birthday`,`hobby`,`type`,`loginCid`,`icon`) VALUES 
 ('0001','郭东升','002','信息学院','计算机专业1','guodongshengabc@163.com','2018-12-22','排球12','管理员','001','icon/02201855431050000.jpg'),
 ('0002','李四1','no2','信息学院1','计算机专业1','guodongshengabc@163.com','2018-03-02','排球','会员','002',NULL),
 ('0005','李6','no2','信息学院1','计算机专业1','guodongshengabc@163.com','2018-03-02','排球','会员','005',NULL),
 ('0008','李7','no2','信息学院1','计算机专业1','guodongshengabc@163.com','2018-03-10','排球','会员','008',NULL),
 ('0011','李9','no2','信息学院1','计算机专业1','guodongshengabc@163.com','2018-03-09','排球','会员','011',NULL),
 ('402881e467da95c80167da9923470002','郭东升31','001','院系','专业','guodongshengabc@163.com','2019-01-02','兴趣爱好','管理员','402881e467da95c80167da98696e0001',NULL),
 ('402881e467da9b850167da9c25310001','郭东升4','002','院系','专业','guodongshengabc@163.com','2018-01-02','兴趣爱好','会员','402881e467da9b850167da9bf46c0000',NULL),
 ('402881e467da9b850167daa9fbec0003','郭东升6','006','院系6','专业6','guodongshengabc@163.com','2018-01-06','兴趣爱好6','会员','402881e467da9b850167daa9f2690002',NULL),
 ('402881e467da9b850167dab847c80014','郭东升5','001','院系','专业','guodongshengabc@163.com','2018-01-02','兴趣爱好','会员','402881e467da9b850167dab846d80013',NULL),
 ('402881e467da9b850167dac223440018','郭东升6','001','院系','专业','guodongshengabc@163.com','2018-01-02','兴趣爱好','会员','402881e467da9b850167dac222c30017',NULL),
 ('402881e467da9b850167dade0c44001a','郭东升7','001','院系','专业','guodongshengabc@163.com','2018-01-01','兴趣爱好','会员','402881e467da9b850167dade0bd40019',NULL),
 ('402881e46854f48d0168550f70130001','郭东升8','1212','信息学院','计算机专业','guodongshengabc@163.com','2018-12-22','排球12','会员','402881e46854f48d0168550f6f940000',NULL),
 ('402881e4686bc0d901686bc282cc0001','郭东升','1212','信息学院2','计算机专业2','guodongshengabc1@163.com','2018-12-22','排球12','会员','402881e4686bc0d901686bc282220000',NULL),
 ('402881e46882a600016882a8afe00001','李四','1212','信息学院2','计算机专业2','guodongshengabc@163.com','2018-12-22','排球12','会员','402881e46882a600016882a8af790000',NULL),
 ('402881e468f14eab0168f14faa8b0001','郭东升','1212','信息学院2','计算机专业2','guodongshengabc@163.com','2018-12-22','排球12','会员','402881e468f14eab0168f14f897a0000',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
