# 1월 16일

엔티티 매핑 소개

- 객체와 테이블 매핑: @Entity, @Table
- 필드와 컬럼 매핑: @Column
- 기본 키 매핑: @Id
- 연관관계 매핑: @ManyToOne,@JoinColumn

## @Entity

- JPA가 관리하는 객체. Entity라고 한다.
- JPA를 사용해서 테이블과 매핑할 클래스는 `@Entity`가 필수다.
- 기본 생성자를 필수로 구현해야 한다.
    - 파라미터가 없는 public 또는 protected 생성자
- final 클래스, enum, interface, inner 클래스에 사용할 수 없다.
- DB에 저장할 필드에는 final을 사용할 수 없다.

### name 속성

- JPA에서 사용할 Entity 이름을 지정한다.
- 기본값으로 클래스 이름을 그대로 사용한다.
- 같은 이름이 있는 게 아니라면 가급적 기본값을 사용한다.

```java
@Entity// JPA가 관리하게 된다.
@Table(name = "MBR") // MBR이라는 테이블과 매핑한다.
public class Member {
	…
}
```

## @Table

Entity와 매핑할 테이블을 지정한다

### **name 속성**

- 매핑할 테이블 이름을 지정한다.
- 실제 쿼리도 name에 지정된 테이블로 나간다.
- Entity 이름을 기본값으로 사용한다.

### **catalog 속성**

- 데이터베이스 catalog 매핑

### **schema 속성**

- 데이터베이스 schema 매핑

### **uniqueConstraints(DDL) 속성**

- DDL 생성 시에 유니크 제약 조건 생성

# 1월 17일

금일에는 프로젝트에 실제 db에 데이터를 넣는다고 가정하고 더미데이터 350만건을 db에 넣고 select를 수행했을 때 조회시간을 측정하였다. 
아래 쿼리문은 db생성, 테이블생성, 프로시저를 이용한 더미데이터 입력 쿼리이며 마지막에는 전체기간동안 한 종목을 불러올 때, 특정 기간 동안 한 종목을 불러올때를 측정하였다. 
```sql
create database stock;
# 오토커밋 off
SET autocommit = 0;

# 테이블생성
CREATE TABLE stock_temp
(   stock_no int unsigned auto_increment NOT NULL,
    stock_date char(8),
    stock_advalorem int unsigned,
    stock_volume int unsigned,
    stock_code char(6),
    stock_name VARCHAR(100),
    stock_rate int,
    PRIMARY KEY (stock_no)
);
drop table stock_temp;
select * from stock_temp;

# 더미데이터 입력
DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsert$$

CREATE PROCEDURE loopInsert()
BEGIN

    DECLARE stock_year_val INT DEFAULT 2018; # 년수 ~5
    DECLARE stock_month_val INT DEFAULT 1; # 일수 ~260
    DECLARE stock_day_val INT DEFAULT 1; # 일수 ~260
    DECLARE stock_code_val INT DEFAULT 1; # 주식종목 2690개
    
	WHILE stock_code_val <= 2690 DO
		SET stock_year_val = 2018;
		WHILE stock_year_val <= 2022 DO
			SET stock_month_val = 1;
            WHILE stock_month_val <= 12 DO
				
					INSERT INTO stock_temp(stock_date , stock_advalorem, stock_volume , stock_code, stock_name, stock_rate)
					VALUES (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '01'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '02'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '03'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '04'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '05'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '06'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '07'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '08'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '09'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '10'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '11'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '12'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '13'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '14'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '15'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '16'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '17'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '18'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '19'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '20'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100),(concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '21'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100), (concat(stock_year_val, LPAD(stock_month_val,'2','0'),  '22'), 1234567, 777777, LPAD(stock_code_val,'6','0'), concat('stockname', stock_code_val), 100);
			SET stock_month_val = stock_month_val + 1;
			END WHILE;
		SET stock_year_val = stock_year_val + 1;
		END WHILE;
	SET stock_code_val = stock_code_val + 1;
    END WHILE;
END$$
DELIMITER $$
CALL loopInsert;

select * from stock_temp;

# 오토커밋 활성화
SET autocommit = 1;

# 3550800 개.. 약 350만개
select count(*) from stock_temp;

# 특정 종목 코드로 전체 가져오기 (5년치)
# 2.546 sec 
select * from stock_temp
where stock_code = '000012';

# 특정 종목 이름으로 전체 가져오기(5년치)
# 2.5 sec
select * from stock_temp
where stock_name = 'stockname233';

# 특정종목 일년치 가져오기 
# 2.859sec
select * from stock_temp
where stock_date like '2018%' and stock_name = 'stockname233';

# 특정종목 한달치 가져오기 2.8sec 
# 2.844sec
select * from stock_temp
where stock_date like '201801__' and stock_name = 'stockname233';
```

성능개선이 필요하다. 

# 1월 18일 

ERD기반으로 db테이블을 만들고 API호출로 데이터를 가지고 와서 db에 넣으려고 시도하였다.

아래는 일별로 과거 데이터를 호출하는 코드이다. 



```java
package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

/**
 * @author HANJAEYOON
 * 국내주식기간별시세(일/주/월/년)[v1_국내주식-016]
 */
public class FHKST03010100 {
    public static int index_count = 1;
    public static void main(String[] args) throws IOException, InterruptedException {

        // 국내 주식 시세 조회
        String url = "";
        String tr_id = "FHKST03010100";
        String data = "{\n" +
                "    \"fid_cond_mrkt_div_code\": \"J\",\n" +
                "    \"fid_input_iscd\": \"005930\"\n" +
                "}";
        for (int i = 0; i < stockCode.size(); i++) {

            String code = stockCode.get(i);
            index_count = 1;
            for (int j = 0; j < startDateList.size(); j++) {
                String start = startDateList.get(j);
                String end = endDateList.get(j);
                httpPostBodyConnection(url,data,tr_id, code, start, end);
                Thread.sleep(2000);
            }
            System.out.println(code +" 종목 데이터 전체 호출 성공");
            Thread.sleep(3000);
        }
    }
    public static void httpPostBodyConnection(String UrlData, String ParamData,String TrId, String code, String start, String end) throws IOException, InterruptedException {

        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        URL url = null;
        HttpURLConnection conn = null;

        String responseData = "";
        BufferedReader br = null;

        StringBuffer sb = new StringBuffer();
        String returnData = "";

        try{
            url = new URL(totalUrl+"?fid_cond_mrkt_div_code=J&fid_input_date_1="+ start +"&fid_input_date_2="+end+"&fid_input_iscd="+code+"&fid_org_adj_prc=0&fid_period_div_code=D");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("authorization", "");
            conn.setRequestProperty("appkey", "");
            conn.setRequestProperty("appsecret", "");
            conn.setRequestProperty("tr_id", TrId);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte request_data[] = ParamData.getBytes("utf-8");
                os.write(request_data);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            conn.connect();
            System.out.println("http 요청 방식" + "GET");
            System.out.println("http 요청 타입" + "application/json");
            System.out.println("http 요청 주소" + UrlData);
            System.out.println("");

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        } catch (IOException e){
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        } finally {
            try {
                sb = new StringBuffer();
                while ((responseData = br.readLine()) != null) {
                    sb.append(responseData);
                }

                returnData = sb.toString();

                JSONObject jsonObject = new JSONObject(returnData);

                String responseCode = String.valueOf(conn.getResponseCode());
                System.out.println("http 응답 코드 : " + responseCode);
//                System.out.println("http 응답 데이터 : " + returnData);

                String rt_cd = jsonObject.getString("rt_cd");
                System.out.println("rt_cd : " + rt_cd);

                String msg_cd = jsonObject.getString("msg_cd");
                System.out.println("msg_cd : " + msg_cd);

                String msg1 = jsonObject.getString("msg1");
                System.out.println("msg1 : " + msg1);

                JSONObject output1Object = jsonObject.getJSONObject("output1");
                JSONArray output2Array = jsonObject.getJSONArray("output2");

                System.out.println("output1(Object) : " + output1Object);
                System.out.println("주식 종목 코드 : " + code);
                String hts_kor_isnm = output1Object.getString("hts_kor_isnm");
                System.out.println("주식 종목 이름 : " + hts_kor_isnm);

//                System.out.println("output2(Array) : " + output2Array);

                System.out.println("output2 배열 출력");
                int cnt = 1;
                for (int i = output2Array.length()-1; i >= 0; i--) {
                    JSONObject obj = output2Array.getJSONObject(i);
                    // 거래일
                    String date = obj.getString("stck_bsop_date");
                    // 오늘 종가
                    int clpr = obj.getInt("stck_clpr");
                    // 당일 거래량
                    BigInteger acml = obj.getBigInteger("acml_vol");
                    // 전일대비 증가량
                    String vrss = obj.getString("prdy_vrss");
                    // 어제 종가
                    int yesterday_clpr = clpr - Integer.parseInt(vrss);
                    // 전일대비 변동률
                    float rate = (clpr - yesterday_clpr) / (float)yesterday_clpr * 100;
                    System.out.println(index_count++ + "번째 출력 " + "거래일: " + date +" 종가: " + clpr + " 거래량: " + acml + " 전일대비 증가량 : " + yesterday_clpr +" 전일대비 변동률 : "+rate);
//                    System.out.println(cnt+"번째 출력 : " + obj);
                    cnt++;
                }

                if (br != null){
                    br.close();
                }

//                //db에 넣기
//                try {
//                    // 2. Driver Load
//                    Class.forName("packagePath.Driver");
//                    System.out.println("Driver load success!");
//
//                    // 3. connection DBMS
//                    String dburl = "jdbc:mysql://localhost:3306/stock?serverTimezone=UTC";
//                    String id = "root";
//                    String pw = "root";
//                    Connection dbconn = DriverManager.getConnection(dburl, id, pw);
//                    System.out.println("DBMS connection success!");
//                    System.out.println("dbconn: "+ dbconn);
//
//                    // 4. Statement
//                    // 코드, 네임, 날짜, 종가, 거래량, 등락률
//			        String sql = "insert into stockdata values (?, ?, ?)";
//                    PreparedStatement pstmt = dbconn.prepareStatement(sql);
//
//                    int cul =  1;
//                    pstmt.setString(cul++, "2");
//
//
//                    // 5. Execute the query
//                    int count = pstmt.executeUpdate();
//                    System.out.println("insert된 row 수 = " + count);
//
//                    // 6. release resources
//                    pstmt.close();
//                    dbconn.close();
//
//                } catch (ClassNotFoundException | SQLException e) {
//                    e.printStackTrace();
//                }


            } catch (IOException e){
                throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
            }

        }
    }
    public static List<String> startDateList = new ArrayList<>(Arrays.asList("20180101", "20180501", "20180901"));
    public static List<String> endDateList = new ArrayList<>(Arrays.asList("20180430", "20180831", "20181231"));

    // 넣을 종목 코드.. 엑셀에서 가져오기.
    public static List<String> stockCode = new ArrayList<>(Arrays.asList("005930"));


}
```
