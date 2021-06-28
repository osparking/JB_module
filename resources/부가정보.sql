-- address_road_gg.부가정보 definition

CREATE TABLE `부가정보` (
  `관리번호` decimal(25,0) NOT NULL,
  `시군구건물명` varchar(40) DEFAULT NULL,
  `공동주택여부` tinyint(4) DEFAULT NULL COMMENT '0:비공동주택, 1:공동주택',
  PRIMARY KEY (`관리번호`),
  CONSTRAINT `부가정보_FK` FOREIGN KEY (`관리번호`) REFERENCES `도로명주소` (`관리번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;