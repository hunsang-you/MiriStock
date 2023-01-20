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

문제는 350만건을 한개식 insert쿼리문을 호출하여 넣기에는 너무 오래걸리며 db연결할때마다 부하가 많이 발생하기 때문에 데이터를 계속 문제가 발생할 가능성이 높다.

그래서 다음 대안을 생각했다.

1. 호출한 데이터를 파일로 저장해서 파일로 한꺼번에 db에 넣기

2. jdk내장객체인 batch를 이용하여 일정 데이터가 쌓이면 한번에 쿼리문을 날려서 db에 넣기

첫번째는 json이나 csv파일로 변환해서 저장해야하는데 이 저장된 파일을 다시 자바에서 가지고와서 변환해서 db에 넣는건 비효율적 같았다.

그래서 2번째 batch를 이용하여 db에 넣기로 하였다. 


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

# 1월 19일

## 상속관계 매핑

![image.png](TIL/hjy/readme-img/image1.png)

객체에는 상속관계가 있지만 관계형 데이터베이스는 상속 관계가 없다

관계형 데이터베이스에는 슈퍼타입 서브타입 관계라는 모델링 기법이 그나마 객체 상속과 유사하다.

상속관계 매핑: 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑하는 것이다.

- DB
    - 논리 모델과 물리 모델이 있다.
    - 논리 모델로 음반, 영화, 책을 구상한다.
    - 가격이나 이름 등 공통적인 속성은 물품에 두고 각각에 맞는 데이터는 아래에 둔다.
- 객체
    - 명확하게 상속 관계가 있다.
    - 아이템이라는 추상 타입을 만들고 그 아래에 상속 관계를 둔다.

**데이터베이스에서 `슈퍼타입 서브타입 논리 모델`을 `실제 물리 모델`로 구현하는 3가지 방법**

- 각각 테이블로 변환 → 조인 전략
- Item 테이블을 Album, Movie, Book 테이블과 조인한다
- 통합 테이블로 변환 → 단일 테이블 전략논리 모델을 하나의 테이블로 합친다.
- 서브타입 테이블로 변환 → 구현 클래스마다 테이블 전략
-데이터베이스 논리모델을 물리 모델로 구현하는 방법이 3가지지만, 객체 입장에서는 모두 동일하다.

**`조인 전략` 과 `단일 테이블 전략` 중에서만 고민하면 된다. (`구현 클래스마다 테이블 전략`은 사용하지 말것)**

- 비지니스적으로 중요하고 복잡한 경우 → 조인 전략
- 비지니스적으로 단순한 경우 → 단일 테이블 전략


**주요 어노테이션**

- @Inheritance(strategy=InheritanceType.XXX)
- @Inheritance(strategy=InheritanceType.JOINED) : 조인 전략
- @Inheritance(strategy=InheritanceType.SINGLE_TABLE) : 단일 테이블 전략
- @Inheritance(strategy=InheritanceType.TABLE_PER_CLASS) : 구현 클래스마다 테이블 전략
- @DiscriminatorColumn(name=“DTYPE”)운영상 DTYPE은 항상 있는게 좋다
- @DiscriminatorValue(“XXX”)

# 1월 20일

- 컨트롤러가 요청을 처리하기 전/후 처리
- 로깅 모니터링 정보 수집, 접근제어 처리등의 실제 비지니스 로직과는 분리되어 처리햐야 하는 기능들을 넣고 싶을 때 유용
- 인터셉터를 여러개 설정할 수 있다.

# 레거시

```java
public class ConfirmInterceptor implements HandlerInterceptor  {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		System.out.println("preHandle Check");
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		if(memberDto == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		return true;
	}
	
}
```

servlet-context.xml

```java
<!-- interceptor setting -->
<beans:bean id="confirmInterceptor" class="com.comp.interceptor.ConfirmInterceptor"></beans:bean>

<interceptors>
	<interceptor>
		<!-- <mapping path="/boead/*"/> -->
		<mapping path="/boead/write"/>
		<mapping path="/boead/view"/>
		<mapping path="/boead/modify"/>
		<mapping path="/boead/delete"/>
		<!-- exclude-mapping 은 해당 경로반 빼고 매핑해라  -->
		<!-- <exclude-mapping path=""/> -->
		<!-- 바깥에서 선언해도 되고 아래처럼 안에서 선언해도 된다-->
		<beans:ref bean="confirmInterceptor""/>

	</interceptor>
</interceptors>
```

# 부트

실습을 위한 예제 자바 파일 생성

```java
@Component
public class CertificationInterceptor implements HandlerInterceptor{
 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        UserVO loginVO = (UserVO) session.getAttribute("loginUser");
 
        if(ObjectUtils.isEmpty(loginVO)){
            response.sendRedirect("/moveLogin.go");
            return false;
        }else{
            session.setMaxInactiveInterval(30*60);
            return true;
        }
        
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        
    }
 
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        
    }
 
}
Col

```

HandlerInterceptor 인터페이스를 상속받으면, 아래 3개의 메소드를 오버라이드한다.

- preHandle : 클라이언트의 요청을 컨트롤러에 전달하기 전에 호출된다. 여기서 false를 리턴하면 다음 내용(Controller)을 실행하지 않는다.
- postHandle : 클라이언트의 요청을 처리한 뒤에 호출된다. 컨트롤러에서 예외가 발생되면 실행되지 않는다.
- afterCompletion : 클라이언트 요청을 마치고 클라이언트에서 뷰를 통해 응답을 전송한뒤 실행이 된다. 뷰를 생성할 때에 예외가 발생할 경우에도 실행이 된다.

인터셉터에 대한 설정은 spring boot의 main 메소드가 있는 클래스에 해도 작동한다.

하지만 @configuration 어노테이션을 사용한 configuration전용 클래스에 각종 자바로 해야하는 spring 설정들을 하는 것이 편리

```java
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

	private final List<String> patterns = Arrays.asList("/board/*", "/admin", "/user/list");

	@Autowired
	private ConfirmInterceptor confirmInterceptor;
    
  @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(confirmInterceptor).addPathPatterns(patterns);
	}
 
}
```
