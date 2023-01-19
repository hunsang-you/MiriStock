## 2023-01-16
- 요구사항 명세서 작성
- 사용 기술스택 명세서 작성
- 재무제표 API 데이터 가져와서 필터링하고 추출하는 코드 작성 ( 연결재무제표,재무제표 어떤 데이터가 맞는지 확인 필요, 분기별 보고서 제한 생각해둬야함 (요청건수 제한이 1만건 언저리인데 요청건수가 10만건 가까이 되기 떄문))
```java
package com.example.test;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.DTO.DartCorp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main {
    public static final String SERVICE_KEY = "API_KEY";
    public static ArrayList<DartCorp> dartlist = new ArrayList<>();
    public static String[] year = new String[]{"2015","2016","2017","2018","2019","2020","2021","2022"};
    public static String[] repocode = new String[]{"11011","11013","11012","11014"};
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        //getDartList();
        //dartlist.forEach(o -> System.out.println("법인명 : " + o.getCorpName() + "\n" + "고유 코드 : " + o.getCorpCode() + "\n" + "종목코드 : " + o.getStock_code() + "\n" ));
        //System.out.println("총 " + dartlist.size());
        StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/fnlttSinglAcnt.json");
        urlBuilder.append("?" + URLEncoder.encode("crtfc_key", "UTF-8") + "=" + SERVICE_KEY); /* Service Key */
        urlBuilder.append("&" + URLEncoder.encode("corp_code", "UTF-8") + "=" + URLEncoder.encode("00126380", "UTF-8")); /* 페이지번호 */
        urlBuilder.append("&" + URLEncoder.encode("bsns_year", "UTF-8") + "=" + URLEncoder.encode("2022", "UTF-8")); /* 한 페이지 결과 수 */
        urlBuilder.append("&" + URLEncoder.encode("reprt_code", "UTF-8") + "=" + URLEncoder.encode("11014", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        if(conn.getResponseCode() != 200){
            System.out.println("에러발생");
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
    }


    public static void getDartList() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse("C:\\CORPCODE.xml");

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        NodeList n_list = root.getElementsByTagName("list");
        Element el;

        for (int i = 0; i < n_list.getLength(); i++) {
            el = (Element) n_list.item(i);
            if (!getTagValue("stock_code", el).equals(" ")) {
                dartlist.add(DartCorp.builder()
                        .corpCode(getTagValue("corp_code", el))
                        .corpName(getTagValue("corp_name", el))
                        .stock_code(getTagValue("stock_code", el))
                        .modifyDate(getTagValue("modify_date", el))
                        .build());
            }

        }

    }

    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }
}

```

---
### 2023-01-17
- 기능 명세서 작성
- 코딩 컨벤션 회의

java stream을 이용한 제무재표 종목 필터링, XML 파일 파싱 , json 데이터 파싱 으로 원하는 데이터 추출하는 코드 작성
```java
package com.example.test;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.DTO.Corporation;
import com.example.DTO.DartCorp;
//import org.json.JSONArray;
//import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main {
    public static final String SERVICE_KEY = "API_KEY";
    public static final String SERVICE_KEY2 = "API_KEY";
    public static ArrayList<DartCorp> dartlist = new ArrayList<>();
    private static ArrayList<Corporation> corp = new ArrayList<>();
    public static String[] year = new String[]{"2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022"};
    public static String[] repocode = new String[]{"11011", "11013", "11012", "11014"};

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        getCorpList();
        getDartList();
        int k = 1;

//        dartlist.forEach(o -> System.out.println( "법인명 : " + o.getCorpName() + "\n" + "고유 코드 : " + o.getCorpCode() + "\n" + "종목코드 : " + o.getStock_code() + "\n" ));
        System.out.println("총 " + dartlist.size());
        System.out.println("총 " + corp.size());

        List<DartCorp> newdart = dartlist.stream()
                .filter(dart -> corp.stream().anyMatch(c -> dart.getStock_code().equals(c.getSrtnCd())))
                .collect(Collectors.toList());
        newdart.forEach(n -> System.out.println(n.getCorpName() + " " + n.getStock_code()));
        System.out.println(newdart.size());
//        for (int i = 0; i < dartlist.size(); i++){
//            System.out.println(corp.get(2280).getSrtnCd());
//            if ( corp.get(2280).getSrtnCd().equals(dartlist.get(i).getStock_code())){
//                System.out.println(dartlist.get(i).getCorpName() + " " + i);
//                break;
//            }
//        }
        String cCode = "";
        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < 1; j++) {
                System.out.println(dartlist.get(i).getCorpCode());
                StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/fnlttSinglAcnt.json");
                urlBuilder.append("?" + URLEncoder.encode("crtfc_key", "UTF-8") + "=" + SERVICE_KEY); /* Service Key */
                urlBuilder.append("&" + URLEncoder.encode("corp_code", "UTF-8") + "=" + URLEncoder.encode("00126380", "UTF-8")); /* 페이지번호 */
                urlBuilder.append("&" + URLEncoder.encode("bsns_year", "UTF-8") + "=" + URLEncoder.encode(year[i], "UTF-8")); /* 한 페이지 결과 수 */
                urlBuilder.append("&" + URLEncoder.encode("reprt_code", "UTF-8") + "=" + URLEncoder.encode("11011", "UTF-8"));
                System.out.println(urlBuilder.toString());
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                if (conn.getResponseCode() != 200) {
                    System.out.println("에러발생");
                }
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                    JSONObject item = new JSONObject(line);
                    if (item.get("status").toString().equals("000")){
                        JSONArray list = item.getJSONArray("list");
                        for (int b= 0 ; b < list.length() ; b++){
                            if ( (!list.getJSONObject(b).get("fs_nm").equals("연결재무제표")) &&(list.getJSONObject(b).get("account_nm").toString().equals("매출액") || list.getJSONObject(b).get("account_nm").toString().equals("당기순이익") || list.getJSONObject(b).get("account_nm").toString().equals("영업이익"))){
                                System.out.println(list.getJSONObject(b).get("account_nm")+ "  " + list.getJSONObject(b).get("thstrm_amount"));
                            }
                        }
                    }else{
                        System.out.println("해당년도의 데이터가 존재하지 않습니다");
                    };
                }
            }
        }
    }


    public static void getDartList() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse("C:\\CORPCODE.xml");

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        NodeList n_list = root.getElementsByTagName("list");
        Element el;

        for (int i = 0; i < n_list.getLength(); i++) {
            el = (Element) n_list.item(i);
            if (!getTagValue("stock_code", el).equals(" ")) {
                dartlist.add(DartCorp.builder()
                        .corpCode(getTagValue("corp_code", el))
                        .corpName(getTagValue("corp_name", el))
                        .stock_code(getTagValue("stock_code", el))
                        .modifyDate(getTagValue("modify_date", el))
                        .build());
            }

        }

    }

    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    private static void getCorpList() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1160100/service/GetKrxListedInfoService/getItemInfo");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY2); /* Service Key */
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=1000000"); /* 한 페이지 결과 수 */
        urlBuilder.append("&" + URLEncoder.encode("resultType", "UTF-8") + "=json");
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        if (conn.getResponseCode() != 200) {
            System.out.println("에러발생");
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        JSONArray item = null;
        while ((line = rd.readLine()) != null) {
            JSONObject json = new JSONObject(line);
            JSONObject res = json.getJSONObject("response");
            JSONObject body = res.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            item = items.getJSONArray("item");
        }

        for (int i = 0; i < item.length(); i++) {
//            System.out.println(i + "  번쨰");
//           System.out.println("회사명 : " + item.getJSONObject(i).get("corpNm"));
//            System.out.println("종목 코드 : " +  item.getJSONObject(i).get("srtnCd").toString().substring(1) );
            //if(item.getJSONObject(i).get("basDt").equals("20230116")) {
            corp.add(new Corporation.Builder()
                    .setBasDt(item.getJSONObject(i).get("basDt").toString())
                    .setSrtnCd(item.getJSONObject(i).get("srtnCd").toString().substring(1))
                    .setIsinCd(item.getJSONObject(i).get("isinCd").toString())
                    .setMrktCtg(item.getJSONObject(i).get("mrktCtg").toString())
                    .setItmsNm(item.getJSONObject(i).get("itmsNm").toString())
                    .setCrno(item.getJSONObject(i).get("crno").toString())
                    .setCorpNm(item.getJSONObject(i).get("corpNm").toString())
                    .build()
            );
//            }else{
//                System.out.println("에러 발생");
//                System.out.println(i);
//                System.out.println("회사명 : " + item.getJSONObject(i).get("corpNm"));
//                System.out.println(item.getJSONObject(i).get("basDt"));
//                System.out.println(item.getJSONObject(i).get("srtnCd"));
//                System.out.println(item.getJSONObject(i).get("isinCd"));
//                System.out.println(item.getJSONObject(i).get("mrktCtg"));
//                System.out.println(item.getJSONObject(i).get("itmsNm"));
//           }
        }
    }

}
```
출력 내용
```json
https://opendart.fss.or.kr/api/fnlttSinglAcnt.json?crtfc_key=API_KEY&corp_code=00126380&bsns_year=2018&reprt_code=11011
{"status":"000","message":"정상","list":[{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"174,697,424,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"146,982,464,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"141,429,704,000,000","ord":"1","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"164,659,820,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"154,769,626,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"120,744,620,000,000","ord":"3","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"339,357,244,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"301,752,090,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"262,174,324,000,000","ord":"5","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"69,081,510,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"67,175,114,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"54,704,095,000,000","ord":"7","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"22,522,557,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"20,085,548,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"14,507,196,000,000","ord":"9","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"91,604,067,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"87,260,662,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"69,211,291,000,000","ord":"11","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"13","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"242,698,956,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"215,811,200,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"193,086,317,000,000","ord":"17","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"247,753,177,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"214,491,428,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"192,963,033,000,000","ord":"21","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"243,771,415,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"239,575,376,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"201,866,745,000,000","ord":"23","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"58,886,669,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"53,645,038,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"29,240,672,000,000","ord":"25","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"61,159,958,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"56,195,967,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"30,713,652,000,000","ord":"27","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"44,344,857,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"42,186,747,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"22,726,092,000,000","ord":"29","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"80,039,455,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"70,155,189,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"69,981,128,000,000","ord":"2","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"138,981,902,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"128,086,171,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"104,821,831,000,000","ord":"4","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"219,021,357,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"198,241,360,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"174,802,959,000,000","ord":"6","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"43,145,053,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"44,495,084,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"34,076,122,000,000","ord":"8","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"2,888,179,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"2,176,501,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"3,180,075,000,000","ord":"10","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"46,033,232,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"46,671,585,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"37,256,197,000,000","ord":"12","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"15","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"166,555,532,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"150,928,724,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"140,747,574,000,000","ord":"19","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 50 기","thstrm_dt":"2018.12.31 현재","thstrm_amount":"172,988,125,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.12.31 현재","frmtrm_amount":"151,569,775,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.12.31 현재","bfefrmtrm_amount":"137,546,762,000,000","ord":"22","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"170,381,870,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"161,915,007,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"133,947,204,000,000","ord":"24","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"43,699,451,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"34,857,091,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"13,647,436,000,000","ord":"26","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"44,398,855,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"36,533,552,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"14,725,074,000,000","ord":"28","currency":"KRW"},{"rcept_no":"20190401004781","reprt_code":"11011","bsns_year":"2018","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 50 기","thstrm_dt":"2018.01.01 ~ 2018.12.31","thstrm_amount":"32,815,127,000,000","frmtrm_nm":"제 49 기","frmtrm_dt":"2017.01.01 ~ 2017.12.31","frmtrm_amount":"28,800,837,000,000","bfefrmtrm_nm":"제 48 기","bfefrmtrm_dt":"2016.01.01 ~ 2016.12.31","bfefrmtrm_amount":"11,579,749,000,000","ord":"30","currency":"KRW"}]}
매출액  170,381,870,000,000
영업이익  43,699,451,000,000
당기순이익  32,815,127,000,000
00247939
https://opendart.fss.or.kr/api/fnlttSinglAcnt.json?crtfc_key=API_KEY&corp_code=00126380&bsns_year=2019&reprt_code=11011
{"status":"000","message":"정상","list":[{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"181,385,260,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"174,697,424,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"146,982,464,000,000","ord":"1","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"171,179,237,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"164,659,820,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"154,769,626,000,000","ord":"3","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"352,564,497,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"339,357,244,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"301,752,090,000,000","ord":"5","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"63,782,764,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"69,081,510,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"67,175,114,000,000","ord":"7","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"25,901,312,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"22,522,557,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"20,085,548,000,000","ord":"9","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"89,684,076,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"91,604,067,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"87,260,662,000,000","ord":"11","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"13","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"254,582,894,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"242,698,956,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"215,811,200,000,000","ord":"17","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"262,880,421,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"247,753,177,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"214,491,428,000,000","ord":"21","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"230,400,881,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"243,771,415,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"239,575,376,000,000","ord":"23","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"27,768,509,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"58,886,669,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"53,645,038,000,000","ord":"25","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"30,432,189,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"61,159,958,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"56,195,967,000,000","ord":"27","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"21,738,865,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"44,344,857,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"42,186,747,000,000","ord":"29","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"72,659,080,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"80,039,455,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"70,155,189,000,000","ord":"2","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"143,521,840,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"138,981,902,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"128,086,171,000,000","ord":"4","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"216,180,920,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"219,021,357,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"198,241,360,000,000","ord":"6","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"36,237,164,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"43,145,053,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"44,495,084,000,000","ord":"8","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"2,073,509,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"2,888,179,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"2,176,501,000,000","ord":"10","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"38,310,673,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"46,033,232,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"46,671,585,000,000","ord":"12","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"15","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"172,288,326,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"166,555,532,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"150,928,724,000,000","ord":"19","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 51 기","thstrm_dt":"2019.12.31 현재","thstrm_amount":"177,870,247,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.12.31 현재","frmtrm_amount":"172,988,125,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.12.31 현재","bfefrmtrm_amount":"151,569,775,000,000","ord":"22","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"154,772,859,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"170,381,870,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"161,915,007,000,000","ord":"24","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"14,115,067,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"43,699,451,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"34,857,091,000,000","ord":"26","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"19,032,469,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"44,398,855,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"36,533,552,000,000","ord":"28","currency":"KRW"},{"rcept_no":"20200330003851","reprt_code":"11011","bsns_year":"2019","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 51 기","thstrm_dt":"2019.01.01 ~ 2019.12.31","thstrm_amount":"15,353,323,000,000","frmtrm_nm":"제 50 기","frmtrm_dt":"2018.01.01 ~ 2018.12.31","frmtrm_amount":"32,815,127,000,000","bfefrmtrm_nm":"제 49 기","bfefrmtrm_dt":"2017.01.01 ~ 2017.12.31","bfefrmtrm_amount":"28,800,837,000,000","ord":"30","currency":"KRW"}]}
매출액  154,772,859,000,000
영업이익  14,115,067,000,000
당기순이익  15,353,323,000,000
00359614
https://opendart.fss.or.kr/api/fnlttSinglAcnt.json?crtfc_key=API_KEY&corp_code=00126380&bsns_year=2020&reprt_code=11011
{"status":"000","message":"정상","list":[{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"198,215,579,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"181,385,260,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"174,697,424,000,000","ord":"1","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"180,020,139,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"171,179,237,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"164,659,820,000,000","ord":"3","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"378,235,718,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"352,564,497,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"339,357,244,000,000","ord":"5","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"75,604,351,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"63,782,764,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"69,081,510,000,000","ord":"7","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"26,683,351,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"25,901,312,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"22,522,557,000,000","ord":"9","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"102,287,702,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"89,684,076,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"91,604,067,000,000","ord":"11","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"13","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"271,068,211,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"254,582,894,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"242,698,956,000,000","ord":"17","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"275,948,016,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"262,880,421,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"247,753,177,000,000","ord":"21","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"236,806,988,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"230,400,881,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"243,771,415,000,000","ord":"23","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"35,993,876,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"27,768,509,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"58,886,669,000,000","ord":"25","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"36,345,117,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"30,432,189,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"61,159,958,000,000","ord":"27","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"26,407,832,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"21,738,865,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"44,344,857,000,000","ord":"29","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"73,798,549,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"72,659,080,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"80,039,455,000,000","ord":"2","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"155,865,878,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"143,521,840,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"138,981,902,000,000","ord":"4","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"229,664,427,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"216,180,920,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"219,021,357,000,000","ord":"6","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"44,412,904,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"36,237,164,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"43,145,053,000,000","ord":"8","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"1,934,799,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"2,073,509,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"2,888,179,000,000","ord":"10","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"46,347,703,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"38,310,673,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"46,033,232,000,000","ord":"12","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"15","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"178,284,102,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"172,288,326,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"166,555,532,000,000","ord":"19","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 52 기","thstrm_dt":"2020.12.31 현재","thstrm_amount":"183,316,724,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.12.31 현재","frmtrm_amount":"177,870,247,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.12.31 현재","bfefrmtrm_amount":"172,988,125,000,000","ord":"22","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"166,311,191,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"154,772,859,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"170,381,870,000,000","ord":"24","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"20,518,974,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"14,115,067,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"43,699,451,000,000","ord":"26","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"20,451,923,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"19,032,469,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"44,398,855,000,000","ord":"28","currency":"KRW"},{"rcept_no":"20210309000744","reprt_code":"11011","bsns_year":"2020","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 52 기","thstrm_dt":"2020.01.01 ~ 2020.12.31","thstrm_amount":"15,615,018,000,000","frmtrm_nm":"제 51 기","frmtrm_dt":"2019.01.01 ~ 2019.12.31","frmtrm_amount":"15,353,323,000,000","bfefrmtrm_nm":"제 50 기","bfefrmtrm_dt":"2018.01.01 ~ 2018.12.31","bfefrmtrm_amount":"32,815,127,000,000","ord":"30","currency":"KRW"}]}
매출액  166,311,191,000,000
영업이익  20,518,974,000,000
당기순이익  15,615,018,000,000
00153551
https://opendart.fss.or.kr/api/fnlttSinglAcnt.json?crtfc_key=API_KEY&corp_code=00126380&bsns_year=2021&reprt_code=11011
{"status":"000","message":"정상","list":[{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"218,163,185,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"198,215,579,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"181,385,260,000,000","ord":"1","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"208,457,973,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"180,020,139,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"171,179,237,000,000","ord":"3","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"426,621,158,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"378,235,718,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"352,564,497,000,000","ord":"5","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"88,117,133,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"75,604,351,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"63,782,764,000,000","ord":"7","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"33,604,094,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"26,683,351,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"25,901,312,000,000","ord":"9","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"121,721,227,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"102,287,702,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"89,684,076,000,000","ord":"11","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"13","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"293,064,763,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"271,068,211,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"254,582,894,000,000","ord":"17","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"304,899,931,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"275,948,016,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"262,880,421,000,000","ord":"21","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"279,604,799,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"236,806,988,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"230,400,881,000,000","ord":"23","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"51,633,856,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"35,993,876,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"27,768,509,000,000","ord":"25","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"53,351,827,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"36,345,117,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"30,432,189,000,000","ord":"27","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"CFS","fs_nm":"연결재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"39,907,450,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"26,407,832,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"21,738,865,000,000","ord":"29","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동자산","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"73,553,416,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"73,798,549,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"72,659,080,000,000","ord":"2","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동자산","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"177,558,768,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"155,865,878,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"143,521,840,000,000","ord":"4","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자산총계","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"251,112,184,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"229,664,427,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"216,180,920,000,000","ord":"6","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"유동부채","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"53,067,303,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"44,412,904,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"36,237,164,000,000","ord":"8","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"비유동부채","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"4,851,149,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"1,934,799,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"2,073,509,000,000","ord":"10","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"부채총계","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"57,918,452,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"46,347,703,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"38,310,673,000,000","ord":"12","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본금","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"897,514,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"897,514,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"897,514,000,000","ord":"15","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"이익잉여금","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"188,774,335,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"178,284,102,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"172,288,326,000,000","ord":"19","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"BS","sj_nm":"재무상태표","account_nm":"자본총계","thstrm_nm":"제 53 기","thstrm_dt":"2021.12.31 현재","thstrm_amount":"193,193,732,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.12.31 현재","frmtrm_amount":"183,316,724,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.12.31 현재","bfefrmtrm_amount":"177,870,247,000,000","ord":"22","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"매출액","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"199,744,705,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"166,311,191,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"154,772,859,000,000","ord":"24","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"영업이익","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"31,993,162,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"20,518,974,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"14,115,067,000,000","ord":"26","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"법인세차감전 순이익","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"38,704,492,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"20,451,923,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"19,032,469,000,000","ord":"28","currency":"KRW"},{"rcept_no":"20220308000798","reprt_code":"11011","bsns_year":"2021","corp_code":"00126380","stock_code":"005930","fs_div":"OFS","fs_nm":"재무제표","sj_div":"IS","sj_nm":"손익계산서","account_nm":"당기순이익","thstrm_nm":"제 53 기","thstrm_dt":"2021.01.01 ~ 2021.12.31","thstrm_amount":"30,970,954,000,000","frmtrm_nm":"제 52 기","frmtrm_dt":"2020.01.01 ~ 2020.12.31","frmtrm_amount":"15,615,018,000,000","bfefrmtrm_nm":"제 51 기","bfefrmtrm_dt":"2019.01.01 ~ 2019.12.31","bfefrmtrm_amount":"15,353,323,000,000","ord":"30","currency":"KRW"}]}
매출액  199,744,705,000,000
영업이익  31,993,162,000,000
당기순이익  30,970,954,000,000
00344746
https://opendart.fss.or.kr/api/fnlttSinglAcnt.json?crtfc_key=API_KEY&corp_code=00126380&bsns_year=2022&reprt_code=11011
{"status":"013","message":"조회된 데이타가 없습니다."}
해당년도의 데이터가 존재하지 않습니다

종료 코드 0(으)로 완료된 프로세스

```
스켈레톤코드. DB 설계와 같이 파싱할 데이터 확정하고 완성할 예정

---
### 2023-01-18

- DB 테이블 작성

```sql
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- -------------------
-- member TABLE
-- -------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE IF NOT EXISTS `member`(
	`member_no` int NOT NULL auto_increment,
    `member_email` VARCHAR(320) NOT NULL,
    `member_nickname` VARCHAR(20) NOT NULL,
    `member_totalasset` BIGINT default 50000000,
    `member_current_time` DATE NOT NULL,
    PRIMARY KEY(`member_no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- stock TABLE
-- -------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE IF NOT EXISTS `stock`(
	`stock_code` VARCHAR(6) NOT NULL,
    `stock_name` VARCHAR(40) NOT NULL,
    PRIMARY KEY (`stock_code`,`stock_name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- stockbuy TABLE
-- -------------------
DROP TABLE IF EXISTS `stockbuy`;
CREATE TABLE IF NOT EXISTS `stockbuy`(
	`stockbuy_no` INT NOT NULL AUTO_INCREMENT,
    `stock_code` VARCHAR(6) NULL DEFAULT NULL,
    `member_no` int NULL DEFAULT NULL,
    `stockbuy_date` DATE NOT NULL,
    `stockbuy_closing_price` BIGINT NOT NULL ,
    `stockbuy_amount` INT NOT NULL,
    PRIMARY KEY (`stockbuy_no`),
    CONSTRAINT `stockbuy_to_stock_stock_code_fk`
		FOREIGN KEY (`stock_code`)
		REFERENCES `stock` (`stock_code`),
    CONSTRAINT `stockbuy_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
		REFERENCES `member` (`member_no`)
        ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- stocksell TABLE
-- -------------------
DROP TABLE IF EXISTS `stocksell`;
CREATE TABLE IF NOT EXISTS `stocksell`(
	`stocksell_no` INT NOT NULL AUTO_INCREMENT,
	`stock_code` VARCHAR(6) NULL DEFAULT NULL,
    `member_no` int NULL DEFAULT NULL,
    `stocksell_date` DATE NOT NULL,
    `stocksell_closing_price` BIGINT NOT NULL ,
    `stocksell_amount` INT NOT NULL,
    PRIMARY KEY (`stocksell_no`),
    CONSTRAINT `stocksell_to_stock_stock_code_fk`
		FOREIGN KEY (`stock_code`)
		REFERENCES `stock` (`stock_code`),
    CONSTRAINT `stocksell_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
		REFERENCES `member` (`member_no`)
        ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- memberasset TABLE
-- -------------------
DROP TABLE IF EXISTS `memberasset`;
CREATE TABLE IF NOT EXISTS `memberasset`(
	`memberasset_no` INT NOT NULL AUTO_INCREMENT,
    `member_no` int NULL DEFAULT NULL,
    `memberasset_total_asset` BIGINT NOT NULL,
    `memberasset_available_asset` BIGINT NOT NULL,
    `memberasset_stock_asset` BIGINT NOT NULL,
    PRIMARY KEY (`memberasset_no`),
    CONSTRAINT `memberasset_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
		REFERENCES `member` (`member_no`)
        ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- financialstatement TABLE
-- -------------------
DROP TABLE IF EXISTS `financialstatement`;
CREATE TABLE IF NOT EXISTS `financialstatement`(
	`financialstatement_no` INT NOT NULL AUTO_INCREMENT,
	`stock_code` VARCHAR(6) NULL DEFAULT NULL,
    `net_income` BIGINT NOT NULL,
    `year` INT NOT NULL,
    `sales_revenue` BIGINT NOT NULL,
    `operating_profit` BIGINT NOT NULL,
    PRIMARY KEY (`financialstatement_no`),
    CONSTRAINT `financialstatement_to_stock_stock_code_fk`
		FOREIGN KEY (`stock_code`)
		REFERENCES `stock` (`stock_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- stockdata TABLE
-- -------------------
DROP TABLE IF EXISTS `stockdata`;
CREATE TABLE IF NOT EXISTS `stockdata`(
	`stockdata_no` INT NOT NULL AUTO_INCREMENT,
	`stock_code` VARCHAR(6) NULL DEFAULT NULL,
    `stock_name` varchar(40) NULL DEFAULT NULL,
    `stockdata_date` DATE NOT NULL,
    `stockdata_closing_price` INT NOT NULL,
    `stockdata_amount` BIGINT NOT NULL,
    `stockdata_flucauation_rate` FLOAT NOT NULL,
    PRIMARY KEY (`stockdata_no`),
    CONSTRAINT `stockdata_to_stock_stock_code_fk`
		FOREIGN KEY (`stock_code`)
		REFERENCES `stock` (`stock_code`),
    CONSTRAINT `stockdata_to_stock_stock_name_fk`
		FOREIGN KEY (`stock_name`)
		REFERENCES `stock` (`stock_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- interest TABLE
-- -------------------
DROP TABLE IF EXISTS `interest`;
CREATE TABLE IF NOT EXISTS `interest`(
	`interest_no` INT NOT NULL AUTO_INCREMENT,
	`stock_code` VARCHAR(6) NULL DEFAULT NULL,
    `member_no` int NULL DEFAULT NULL,
    PRIMARY KEY (`interest_no`),
    CONSTRAINT `interest_to_stock_stock_code_fk`
		FOREIGN KEY (`stock_code`)
        REFERENCES `stock` (`stock_code`),
	CONSTRAINT `interest_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
        REFERENCES `member` (`member_no`)
        ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- limitpricesellorder TABLE
-- -------------------
DROP TABLE IF EXISTS `limitpricesellorder`;
CREATE TABLE IF NOT EXISTS `limitpricesellorder`(
	`limitpricesellorder_no` INT NOT NULL AUTO_INCREMENT,
	`stock_code` VARCHAR(6) NULL DEFAULT NULL,
    `member_no` int NULL DEFAULT NULL,
    `reservation_price` INT NOT NULL,
    `stocksell_amount` INT NOT NULL,
        PRIMARY KEY (`limitpricesellorder_no`),
    CONSTRAINT `limitpricesellorder_to_stock_stock_code_fk`
		FOREIGN KEY (`stock_code`)
        REFERENCES `stock` (`stock_code`),
	CONSTRAINT `limitpricesellorder_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
        REFERENCES `member` (`member_no`)
        ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- limitpricebuyorder TABLE
-- -------------------
DROP TABLE IF EXISTS `limitpricebuyorder`;
CREATE TABLE IF NOT EXISTS `limitpricebuyorder`(
	`limitpricebuyorder_no` INT NOT NULL AUTO_INCREMENT,
	`stock_code` VARCHAR(6) NULL DEFAULT NULL,
    `member_no` int NULL DEFAULT NULL,
    `reservation_price` INT NOT NULL,
    `stockbuy_amount` INT NOT NULL,
        PRIMARY KEY (`limitpricebuyorder_no`),
    CONSTRAINT `limitpricebuyorder_to_stock_stock_code_fk`
		FOREIGN KEY (`stock_code`)
        REFERENCES `stock` (`stock_code`),
	CONSTRAINT `limitpricebuyorder_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
        REFERENCES `member` (`member_no`)
        ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- article TABLE
-- -------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article`(
	`article_no` INT NOT NULL AUTO_INCREMENT,
    `member_no` int NULL DEFAULT NULL,
    `article_title` VARCHAR(100) NOT NULL,
    `article_content` VARCHAR(1000) NOT NULL,
    `article_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_no`),
    CONSTRAINT `article_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
        REFERENCES `member` (`member_no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -------------------
-- comment TABLE
-- -------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment`(
	`comment_no` INT NOT NULL AUTO_INCREMENT,
	`article_no` INT NULL DEFAULT NULL,
    `member_no` int NULL DEFAULT NULL,
    `comment_content` VARCHAR(300) NOT NULL,
    `comment_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`comment_no`),
    CONSTRAINT `comment_to_article_article_no_fk`
		FOREIGN KEY (`article_no`)
        REFERENCES `article` (`article_no`),
	CONSTRAINT `comment_to_member_member_no_fk`
		FOREIGN KEY (`member_no`)
        REFERENCES `member` (`member_no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
```
---
### 2023-01-19


- 종목명,종목코드 SQL , 연도별 사업보고서기준 매출액,당기순이익,영업이익 SQL 생성 코드
```java
package com.example.test;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.DTO.Corp2;
import com.example.DTO.Corporation;
import com.example.DTO.DartCorp;
import com.example.DTO.Financial;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main {
    public static final String SERVICE_KEY = "587afc333f77de19b08c7971d93cc48706b90822";
    public static final String SERVICE_KEY2 = "87fABOEIcdWtsvwjsYXjoVbKfO7Oms4Jap9J8psfwew3kVvfO5hmZo8TSM9qqBS49RD%2BV8S3rukAg3J9M%2FE%2Blg%3D%3D";
    public static ArrayList<DartCorp> dartlist = new ArrayList<>();
    private static ArrayList<String> corp;
    private static ArrayList<String> corpname;
    public static String[] year = new String[]{"2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022"};
    public static String[] repocode = new String[]{"11011", "11013", "11012", "11014"};
    public static ArrayList<Financial> finlist = new ArrayList<Financial>();
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        //getCorpList();
        getDartList();
        int k = 1;
        corp=new Corp2().getCorplist();
        corpname = new Corp2().getCorpname();
//        dartlist.forEach(o -> System.out.println( "법인명 : " + o.getCorpName() + "\n" + "고유 코드 : " + o.getCorpCode() + "\n" + "종목코드 : " + o.getStock_code() + "\n" ));
        System.out.println("총 " + dartlist.size());
        System.out.println("총 " + corp.size());
        File f1 = new File("c:\\stocksql.txt");
        FileOutputStream op = new FileOutputStream (f1);
        String sql = "";
        for (int i =0; i < corp.size() ; i++){
            sql += "INSERT INTO `stock` " +
                    "VALUES (\"" + corp.get(i) + "\",\"" + corpname.get(i) + "\" );" + "\n";
        }
        byte[] by =sql.getBytes();
        op.write(by);
        op.close();
        List<DartCorp> newdart = dartlist.stream()
                .filter(dart -> corp.stream().anyMatch(c -> dart.getStock_code().equals(c)))
                .collect(Collectors.toList());
        newdart.forEach(n -> System.out.println(n.getCorpName() + " " + n.getStock_code()));
        System.out.println(newdart.size());
//        for (int i = 0; i < dartlist.size(); i++){
//            System.out.println(corp.get(2280).getSrtnCd());
//            if ( corp.get(2280).getSrtnCd().equals(dartlist.get(i).getStock_code())){
//                System.out.println(dartlist.get(i).getCorpName() + " " + i);
//                break;
//            }
//        }
        String cCode = "";
        int count =0;
        String str="";
        OutputStream output = new FileOutputStream("C:\\financialstatement.txt");
        File file =new File("C:\\financialstatement.txt");
        FileWriter fw =new FileWriter(file,true);
        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < newdart.size(); j++) {
                count++;
                System.out.println(count + " 번째 요청");
                System.out.println("현재 위치 :" + newdart.get(j).getCorpCode() + "회사 " + year[i] + "년" );
                if( count % 80 == 0){
                    Thread.sleep(1000 * 60);
                }
                //System.out.println(dartlist.get(i).getCorpCode());
                StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/fnlttSinglAcnt.json");
                urlBuilder.append("?" + URLEncoder.encode("crtfc_key", "UTF-8") + "=" + SERVICE_KEY); /* Service Key */
                urlBuilder.append("&" + URLEncoder.encode("corp_code", "UTF-8") + "=" + URLEncoder.encode(newdart.get(j).getCorpCode(), "UTF-8")); /* 페이지번호 */
                urlBuilder.append("&" + URLEncoder.encode("bsns_year", "UTF-8") + "=" + URLEncoder.encode(year[i], "UTF-8")); /* 한 페이지 결과 수 */
                urlBuilder.append("&" + URLEncoder.encode("reprt_code", "UTF-8") + "=" + URLEncoder.encode("11011", "UTF-8"));
                //System.out.println(urlBuilder.toString());
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                if (conn.getResponseCode() != 200) {
                    System.out.println("에러발생");
                }
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;


                while ((line = rd.readLine()) != null) {
                    //System.out.println(line);
                    JSONObject item = new JSONObject(line);
                    if (item.get("status").toString().equals("000")){
                        JSONArray list = item.getJSONArray("list");
                        Financial f = new Financial();

                        for (int b= 0 ; b < list.length() ; b++){
                            if (!list.getJSONObject(b).get("fs_nm").equals("연결재무제표")){
                              //  System.out.println(list.getJSONObject(b).get("stock_code"));
                              //  System.out.println(list.getJSONObject(b).get("bsns_year"));
                             //   System.out.println(list.getJSONObject(b).get("account_nm")+ "  " + list.g         etJSONObject(b).get("thstrm_amount"));

                                f.setStockCode(list.getJSONObject(b).get("stock_code").toString());
                                f.setYear(list.getJSONObject(b).get("bsns_year").toString());
                                if(list.getJSONObject(b).get("account_nm").toString().equals("매출액")){
                                    f.setSalesRevenue(list.getJSONObject(b).get("thstrm_amount").toString().replaceAll(",",""));
                                }
                                if(list.getJSONObject(b).get("account_nm").toString().equals("당기순이익")){
                                    f.setNetIncome(list.getJSONObject(b).get("thstrm_amount").toString().replaceAll(",",""));
                                }
                                if(list.getJSONObject(b).get("account_nm").toString().equals("영업이익")){
                                    f.setOperating_profit(list.getJSONObject(b).get("thstrm_amount").toString().replaceAll(",",""));
                                }

                            }
                        }
                        if (f.getYear() != null  && f.getNetIncome() !=null && f.getStockCode()!= null && f.getOperating_profit() != null ) {
                            finlist.add(f);
                            System.out.println(i);
                            System.out.println("종목코드 " + f.getStockCode());
                            System.out.println(f.getYear() + "년");
                            System.out.println("매출액" + f.getSalesRevenue());
                            System.out.println("영업이익" + f.getOperating_profit());
                            System.out.println("당기순이익" + f.getNetIncome());
                            fw.write("INSERT INTO financialstatement (`stock_code`,`net_income`,`year`,`sales_revenue`,`operating_profit`) "+
                                    "VALUES " + "(" + "\"" + f.getStockCode() + "\"," + f.getNetIncome() + "," + f.getYear() + "," + f.getSalesRevenue()+ "," + f.getOperating_profit()+ ");" + "\n");
                        }
                    }else{
                        //System.out.println("해당년도의 데이터가 존재하지 않습니다");
                    };
                }
            }
        }
        fw.flush();
//        byte[] by=str.getBytes();
//        output.write(by);
    }


    public static void getDartList() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse("C:\\CORPCODE.xml");

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        NodeList n_list = root.getElementsByTagName("list");
        Element el;

        for (int i = 0; i < n_list.getLength(); i++) {
            el = (Element) n_list.item(i);
            if (!getTagValue("stock_code", el).equals(" ")) {
                dartlist.add(DartCorp.builder()
                        .corpCode(getTagValue("corp_code", el))
                        .corpName(getTagValue("corp_name", el))
                        .stock_code(getTagValue("stock_code", el))
                        .modifyDate(getTagValue("modify_date", el))
                        .build());
            }

        }

    }

    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }


}

```
