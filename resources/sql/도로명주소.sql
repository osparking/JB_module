-- address_road_gg.도로명주소 definition

CREATE TABLE `도로명주소` (
  `관리번호` decimal(25,0) NOT NULL,
  `도로명코드` decimal(12,0) NOT NULL COMMENT '413904397016',
  `읍면동일련번호` tinyint(2) NOT NULL COMMENT '00',
  `지하여부` tinyint(1) DEFAULT NULL COMMENT '0:지상, 1:지하',
  `건물본번` mediumint(5) DEFAULT NULL,
  `건물부번` mediumint(5) DEFAULT NULL,
  `기초구역번호` mediumint(5) DEFAULT NULL COMMENT '우편번호',
  PRIMARY KEY (`관리번호`),
  KEY `도로명주소_FK3` (`도로명코드`,`읍면동일련번호`),
  CONSTRAINT `도로명주소_FK` FOREIGN KEY (`도로명코드`, `읍면동일련번호`) REFERENCES `도로명코드` (`도로명코드`, `읍면동일련번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
