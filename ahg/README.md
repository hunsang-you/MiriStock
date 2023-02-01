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

## 2023-01-20

- 백 엔드 구조 작성 및 스켈레톤 코드 작성
- Security 테스트

```java
package com.udteam.miristock.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(CustomOAuth2UserService);

//                .and()
//                .oauth2Login()
//                .authorizationEndpoint()
//                .baseUri("/oauth2/authorization")
//                .authorizationRequestRepository()
//                .and()
//                .redirectionEndpoint()
//                .baseUri("/*/oauth2/code/*")
//                .and()
//                .userInfoEndpoint()
//                .userService()
//                .and()
//                .successHandler()
//                .failureHandler();

    }


}
```

Controller

```java
package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberservice;

    @GetMapping
    public ResponseEntity<List<MemberDto>> selectAllMember(){
        return ResponseEntity.ok().body(memberservice.selectAllMember());
    }
}

```

Service

```java
package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberrepository;

    public List<MemberDto> selectAllMember(){
        return memberrepository.findAll()
                .stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }
}
```

repository

```java
package com.udteam.miristock.repository;

import com.udteam.miristock.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long>{

}

```

entity

```java
package com.udteam.miristock.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class MemberEntity {

    @Id
    @Column(name="member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(name="member_email",nullable = false,length = 320)
    private String memberEmail;

    @Column(name="member_nickname",nullable= false, length = 20)
    private String memberNickname;

    @Column(name="member_totalasset")
    @ColumnDefault("50000000")
    private Long memberTotalasset;

    @Column(name="member_current_tile",nullable=false)
    private int memberCurrentTime;

    private Role role;

}

```

DTO

```java
package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberEntity;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private long memberNo;
    private String memberEmail;
    private String memberNickname;
    private Long memberTotalasset;
    private int memberCurrentTime;

    public static MemberDto of(MemberEntity member){
        return MemberDto.builder()
                .memberNo(member.getMemberNo())
                .memberEmail(member.getMemberEmail())
                .memberNickname(member.getMemberNickname())
                .memberTotalasset(member.getMemberTotalasset())
                .memberCurrentTime(member.getMemberCurrentTime())
                .build();
    }

}

```

## 2023-01-25

- OAuth2.0 인증 구현

CustomOAuth2UserService.java

```java
package com.udteam.miristock.service.auth;

import com.udteam.miristock.dto.auth.OAuth2Attribute;
import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.lang.reflect.Member;
import java.util.Collections;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2Service 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // userRequest에 있는 access Token으로 정보 얻기
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        //	userRequest에 있는 registrationId ( security에서 제공하는 구분값 ) 과 userNameAttributeName ( 반환해주는 JSON값에서 원하는 파트만 필터링 )을 가져온다
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId = {}", registrationId);
        // naver는 response, kakao는 kakao_account 안에 필요한 정보가 들어가 있으니 해당 내용을 application-oauth.yml에 미리 설정해둠
        log.info("userNameAttributeName = {}", userNameAttributeName);

        // 얻어온 값들로 oAuth2Attribute 객체 생성 ( oAuth2User.getAttributes() 에는 반환받은 JSON 값이 들어가있음 )
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 만들어진 객체를 map 형식으로 변환 후 OAuth2User 기본객체인 DefaultOAuth2User 생성후 리턴 ( 권한, 유저 데이터, nameattributeKey ( 사용자 식별을 하기위한 키 ))
        Map<String,Object> memberAttribute = oAuth2Attribute.convertToMap();
        log.info("login email = {}", (String) memberAttribute.get("email"));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                memberAttribute, "email");
    }
}
```

OAuth2SuccessHandler.java

```java
package com.udteam.miristock.service.auth;

import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.RefreshTokenEntity;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenservice;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User = {} ", oAuth2User);

        log.info("DB 등록 확인");
        MemberEntity member = memberRepository.findByMemberEmail((String) oAuth2User.getAttribute("email"));
        if (oAuth2User != null && member == null) {
            log.info("유저를 찾을 수 없습니다. 유저 정보를 등록합니다.");
            createMember(oAuth2User);
        }
        if(member!=null && !member.getMemberProvider().equals(oAuth2User.getAttribute("provider"))){
            log.info("다른 소셜에 등록된 회원입니다");
            getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString("/error")
                    .build().toUriString());
            return;
        }
        log.info("토큰 발행 시작");


        if (refreshTokenRepository.findByEmail((String) oAuth2User.getAttribute("email")) == null) {
            log.info("refresh token이 존재하지 않습니다. refresh token 생성");

            // 테이블 생성 고려해 봐야할듯
            updatetoken((String) oAuth2User.getAttribute("email"), tokenservice.generateToken(oAuth2User.getAttribute("email"), "MEMBER", "REFRESH"));
        }

        String accesstoken = tokenservice.generateToken(oAuth2User.getAttribute("email"), "MEMBER", "ACCESS");
        log.info("accecss_Token = {}", accesstoken);

        // if문으로 만약 닉네임이 null 이면 닉네임 설정 페이지로 아니면 로그인 처리 후 메인으로로
       getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString("/")
                .queryParam("accesstoken", accesstoken)
                .build().toUriString());
    }

    private RefreshTokenEntity updatetoken(String email, String token) {
        return refreshTokenRepository.save(RefreshTokenEntity.builder()
                .email(email)
                .refreshToken(token)
                .build());
    }

    private MemberEntity createMember(OAuth2User oAuth2User) {
        return memberRepository.saveAndFlush(MemberEntity.builder()
                .memberEmail((String) oAuth2User.getAttribute("email"))
                .memberCurrentTime(20150101)
                .role(Role.MEMBER)
                .memberProvider(oAuth2User.getAttribute("provider"))
                .build());
    }
}
```

## 2023-01-27

- AWS EC2 로 Docker를 받아 스프링 + RDB를 테스트 배포하는 작업을 진행했음

docker-compose.yml 설정

```linux
version: "3"
services:

    web:
        container_name: nginx
        image: nginx
        ports:
          - 80:80
        volumes:
          - ./nginx/conf.d:/etc/nginx/conf.d
        depends_on:
          - application

    database:
        image: mysql
        restart: always
        container_name: mysqldb
        #        volumes:
                #          - ~/db/var/lib/mysql:/var/lib/mysql
        environment:
               MYSQL_DATABASE: [dbname]
               MYSQL_ROOT_PASSWORD: [password]
               MYSQL_USER: miristock
               MYSQL_PASSWORD: [userpassword]
        command: ['--character-set-server=utf8mb4', '--collation-server=utf8mb4_unicode_ci']
        ports:
          - 3306:3306

    application:
```

마운트 할 nginx conf파일 설정

```linux
server {
   listen 80;
   access_log off;

   location / {
        proxy_pass http://application:8080;
        proxy_set_header Host $host:$server_port;
        proxy_set_header X-Forwarded-Host $server_name;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
   }
}
```

### 2023-02-01

- git jenkins 연동
- ec2 jenkins 연동
- 쉘 스크립트 작성 중

deploy.sh

```
#!/bin/bash
echo "> 현재 구동중인 profile 확인"
CURRENT_PROFILE=$(curl -s http://localhost/utils/profile)
echo "> $CURRENT_PROFILE"

if [ $CURRENT_PROFILE == production-set1 ]
then
  IDLE_PROFILE=production-set2
  IDLE_PORT=9002
elif [ $CURRENT_PROFILE == production-set2 ]
then
  IDLE_PROFILE=production-set1
  IDLE_PORT=9001
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> set1을 할당합니다. IDLE_PROFILE: set1"
  IDLE_PROFILE=production-set1
  IDLE_PORT=9001
fi

IMAGE_NAME=app_server
TAG_ID=$(docker images | sort -r -k2 -h | grep "${IMAGE_NAME}" | awk 'BEGIN{tag = 1} NR==1{tag += $2} END{print tag}')

echo "> 도커 build 실행 : docker build --build-arg IDLE_PROFILE=${IDLE_PROFILE} -t ${IMAGE_NAME}:${TAG_ID} ."
docker build --build-arg IDLE_PROFILE=${IDLE_PROFILE} -t ${IMAGE_NAME}:${TAG_ID} /home/ubuntu/app-server

echo "> $IDLE_PROFILE 배포"
echo "> 도커 run 실행 :  sudo docker run --name $IDLE_PROFILE -d --rm -p $IDLE_PORT:${IDLE_PORT} ${IMAGE_NAME}:${TAG_ID}"
docker run --name $IDLE_PROFILE -d --rm -p $IDLE_PORT:${IDLE_PORT} ${IMAGE_NAME}:${TAG_ID}

echo "> $IDLE_PROFILE 10초 후 Health check 시작"
echo "> curl -s http://localhost:$IDLE_PORT/actuator/health "
sleep 10

for retry_count in {1..10}
do
  response=$(curl -s http://localhost:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ]
  then
    echo "> Health check 성공"
    break
  else
    echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
    echo "> Health check: ${response}"
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done

echo "> 스위칭을 시도합니다..."
sleep 5

/home/ubuntu/app-server/switch.sh
```

switch.sh

```
#!/bin/bash
echo "> 현재 구동중인 Port 확인"
CURRENT_PROFILE=$(curl -s http://localhost/utils/profile)

if [ $CURRENT_PROFILE == production-set1 ]
then
  CURRENT_PORT=9001
  IDLE_PORT=9002
elif [ $CURRENT_PROFILE == production-set2 ]
then
  CURRENT_PORT=9002
  IDLE_PORT=9001
else
  echo "> 일치하는 Profile이 없습니다. Profile:$CURRENT_PROFILE"
  echo "> 9001을 할당합니다."
  IDLE_PORT=9001
fi

echo "> 현재 구동중인 Port: $CURRENT_PORT"
echo "> 전환할 Port : $IDLE_PORT"
echo "> Port 전환"
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

echo "> ${CURRENT_PROFILE} 컨테이너 삭제"
sudo docker stop $CURRENT_PROFILE
sudo docker rm $CURRENT_PROFILE

echo "> Nginx Reload"

sudo service nginx reload
```
