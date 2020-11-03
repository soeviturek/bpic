/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.20 : Database - bpic
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bpic` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bpic`;

/*Table structure for table `sys_login_user` */

DROP TABLE IF EXISTS `sys_login_user`;

CREATE TABLE `sys_login_user` (
  `c_emp_cde` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上线员工编码',
  `c_emp_cnm` varchar(40) DEFAULT NULL COMMENT '上线员工姓名',
  `c_dpt_cde` varchar(11) DEFAULT NULL COMMENT '上线员工所属部门',
  `cphone` varchar(11) DEFAULT '' COMMENT '上线员工手机号',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `c_tel_mob` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `c_passwd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(50) DEFAULT '' COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `develop_time` datetime DEFAULT NULL COMMENT '发展日期',
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信号',
  `c_edu_cde` varchar(10) DEFAULT NULL COMMENT '教育程度',
  PRIMARY KEY (`c_tel_mob`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

/*Data for the table `sys_login_user` */

insert  into `sys_login_user`(`c_emp_cde`,`c_emp_cnm`,`c_dpt_cde`,`cphone`,`user_name`,`c_tel_mob`,`sex`,`c_passwd`,`status`,`del_flag`,`login_ip`,`login_date`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`develop_time`,`open_id`,`c_edu_cde`) values ('2','105',NULL,'','ry','15666666666','1','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2018-03-16 11:33:00','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','测试员',NULL,NULL,NULL),('123','103',NULL,'','测试','15856895895','0','e10adc3949ba59abbe56e057f20f883e','0','0','',NULL,'',NULL,'',NULL,NULL,NULL,NULL,NULL),('1','103',NULL,'','admin','15888888888','1','$2a$10$Js5bb0Z30lZ5q0pr9jtCT.SIA5XOTt068S3IBOyYVzDt7aWLYfZfu','0','0','127.0.0.1','2018-03-16 11:33:00','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','管理员',NULL,NULL,NULL),(NULL,NULL,NULL,'','测试2','18838984858','0','$2a$10$95OTKZS9YsKh0jt3HPd93e/oihm43KGjV.EXPNCysWuarRvK2ozf6','0','0','',NULL,'','2020-08-12 15:16:20','',NULL,NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
