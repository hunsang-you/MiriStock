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
    public static final String SERVICE_KEY = "587afc333f77de19b08c7971d93cc48706b90822";
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
