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

/*Table structure for table `config_sales` */

DROP TABLE IF EXISTS `config_sales`;

CREATE TABLE `config_sales` (
  `config_code` bigint DEFAULT NULL COMMENT '配置权限代码',
  `dept_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属公司',
  `sales` varchar(32) DEFAULT NULL COMMENT '员工编码',
  `dept_code` varchar(32) DEFAULT NULL COMMENT '公司编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `config_sales` */

insert  into `config_sales`(`config_code`,`dept_name`,`sales`,`dept_code`) values (20200810587595,'江苏,南京,南京一部','100035646','3200'),(20200812878985,'江苏','100034568','32'),(20200810587595,'江苏','100034568','32'),(20200812165620,'上海,徐汇,桂林路','123456','1212'),(20200812165620,'北京,保定','121212','2222'),(20200812165620,'上海,徐汇,桂林路','123456','1212'),(20200812165620,'北京,保定','121212','2222');

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `product_code` bigint DEFAULT NULL COMMENT '产品代码',
  `poundage` float DEFAULT NULL COMMENT '费用比例-手续费',
  `performance` float DEFAULT NULL COMMENT '费用比例-绩效',
  `acquisition_fee` float DEFAULT NULL COMMENT '费用比例-展业费',
  `incentive_fee` float DEFAULT NULL COMMENT '费用比例-激励费用',
  `config_code` bigint DEFAULT NULL COMMENT '配置权限代码',
  `channel_code` bigint DEFAULT NULL COMMENT '渠道代码',
  `product_name` varchar(50) DEFAULT NULL COMMENT '产品名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `product` */

insert  into `product`(`id`,`product_code`,`poundage`,`performance`,`acquisition_fee`,`incentive_fee`,`config_code`,`channel_code`,`product_name`) values (1,1234456,3.5,3.6,3.7,3.2,20200810587595,123123,'测试'),(2,1234456,1.5,2.3,3.2,2.2,20200812878985,123123,'测试2'),(3,1234456,1.2,1.3,1.4,1.5,20200812598745,123123,'测试3');

/*Table structure for table `product_config` */

DROP TABLE IF EXISTS `product_config`;

CREATE TABLE `product_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `config_code` bigint NOT NULL COMMENT '配置权限代码',
  `config_name` varchar(20) NOT NULL COMMENT '权限配置名称',
  `auth_start_time` date DEFAULT NULL COMMENT '生效时间',
  `auth_end_time` date DEFAULT NULL COMMENT '失效时间',
  `message` varchar(100) DEFAULT NULL COMMENT '备注',
  `creater` varchar(10) DEFAULT NULL COMMENT '配置人',
  `create_time` date DEFAULT NULL COMMENT '配置时间',
  `status` int DEFAULT NULL COMMENT '状态0：已下线 1：已上线 2：待上线 3：暂存中',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `product_config` */

insert  into `product_config`(`id`,`config_code`,`config_name`,`auth_start_time`,`auth_end_time`,`message`,`creater`,`create_time`,`status`) values (1,20200810587595,'测试配置','2020-08-10','2020-08-20','测试','111','2020-08-10',0),(2,20200812878985,'测试配置2','2020-08-12','2020-08-13','测试2','111','2020-08-12',0),(3,20200812598745,'测试配置3','2020-08-12','2020-08-21','测试3','111','2020-08-12',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
