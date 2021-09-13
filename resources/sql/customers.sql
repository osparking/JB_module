CREATE TABLE `전통고객` (
  `고객SN` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '고객 일련번호(Serial Number)',
  `고객ID` varchar(40) NOT NULL,
  `고객이름` varchar(40) NOT NULL,
  `password` binary(16) DEFAULT NULL COMMENT '해슁된 비밀번호 16바이트',
  `salt` binary(16) DEFAULT NULL COMMENT '비밀번호 생성 salt 16바이트',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '행 삭제 여부',
  `del_datetm` datetime DEFAULT current_timestamp() COMMENT '행 삭제 일시',
  PRIMARY KEY (`고객SN`),
  UNIQUE KEY `전통고객_ID_un` (`고객ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8