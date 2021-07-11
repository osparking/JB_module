SELECT  A.기초구역번호 AS 새우편번호,	
	concat(
	B.시도명, ' ',
	if (B.시군구 = '', '', concat(B.시군구,' ')),
    case when B.읍면동구분 = 0 then concat(B.읍면동,' ') 
    else '' 
    end,
	concat(B.도로명,' '),
	case when A.지하여부 = 0 then '' 
		when A.지하여부 = 1 then '지하 ' 
		when A.지하여부 = 2 then '공중 ' end,
	A.건물본번,
	if (A.건물부번 = 0, '', concat('-',A.건물부번)),
	CASE WHEN (B.읍면동구분 = 0 AND D.공동주택여부 = 0) THEN ''
		WHEN (B.읍면동구분 = 0 AND D.공동주택여부 = 1) then
			case D.시군구건물명 
				when (D.시군구건물명 = '') then '' 
				else concat('(',D.시군구건물명,')') end 
		WHEN (B.읍면동구분 = 1 AND D.공동주택여부 = 0) 
			THEN concat('(',B.읍면동,')')
		WHEN (B.읍면동구분 = 1 AND D.공동주택여부 = 1) 
			THEN concat('(', B.읍면동
				, case when (D.시군구건물명 = '') then ''	
					   else concat(',', D.시군구건물명) end
				,')')			
   	end) AS 도로명주소
  FROM 도로명주소 A, 도로명코드 B, 부가정보 D 
 WHERE A.도로명코드     = B.도로명코드
   AND A.읍면동일련번호 = B.읍면동일련번호
   AND A.관리번호       = D.관리번호
   and (
  		B.도로명 LIKE concat('덕영대로','%')
   		AND A.건물본번 like '89%'	
   );

-- 입력 수(1 혹은 2)에 따른 조건절 부분
  
-- 1개. 입력(input) : 덕영대로895 혹은 세진
  /*
    B.도로명 LIKE concat('세진','%')
  	or D.시군구건물명 LIKE concat('세진', '%')
   */
    
-- 2개. 입력(input) : 덕영대로 89
  /*
    B.도로명 LIKE concat('덕영대로','%')
   	AND A.건물본번 like '89%'	
   */
  
--    AND B.도로명         LIKE concat('덕영대로895','%');
--    and D.시군구건물명         LIKE concat('세진', '%');
   
--    AND A.건물본번         like '897%';
   
--    AND (B.도로명         LIKE concat('덕영대로895','%'));
   
--    AND (B.도로명         LIKE concat('덕영대로','%')
--    AND A.건물본번         like '89%')
   
--    AND (B.도로명         LIKE concat('덕영대로895','%')
--    or D.시군구건물명         LIKE concat('덕영대로895', '%'))
   
--    AND (B.도로명         LIKE concat('세진브론','%')
--    or D.시군구건물명         LIKE concat('세진브론', '%'))


   
   
   
   
   
   
   
   
   