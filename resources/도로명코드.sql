CREATE TABLE `도로명코드` (
  `도로명코드` bigint(20) NOT NULL COMMENT '413904397016',
  `읍면동일련번호` tinyint(2) NOT NULL COMMENT '00',
  `시도명` varchar(20) DEFAULT NULL,
  `시군구` varchar(20) DEFAULT NULL,
  `읍면동구분` tinyint(1) DEFAULT NULL COMMENT '0: 읍면, 1:동, 2:미부여',
  `도로명` varchar(80) DEFAULT NULL,
  `읍면동` varchar(20) DEFAULT NULL,
  `읍면동코드` smallint(6) DEFAULT NULL COMMENT '132',
  PRIMARY KEY (`도로명코드`,`읍면동일련번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;