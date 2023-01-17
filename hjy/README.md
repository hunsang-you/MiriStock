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
