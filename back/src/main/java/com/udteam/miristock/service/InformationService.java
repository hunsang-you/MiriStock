package com.udteam.miristock.service;

import com.udteam.miristock.dto.FinancialstatementDto;
import com.udteam.miristock.dto.NewsMessage;
import com.udteam.miristock.dto.NewsRequestDto;
import com.udteam.miristock.dto.NewsResponseDto;
import com.udteam.miristock.repository.FinancialstatementRepository;
import com.udteam.miristock.util.RSSFeedParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InformationService {

    private final FinancialstatementRepository financialstatementRepository;

    public List<FinancialstatementDto> findAllFinancialstatement(String stockCode) {
        return financialstatementRepository.findByStockCode(stockCode)
                .stream()
                .map(FinancialstatementDto::new)
                .collect(Collectors.toList());
    }

    public NewsResponseDto findNews(NewsRequestDto newsRequestDto) {

        String keyword = newsRequestDto.getSearchKeyword();
        Integer startDate = newsRequestDto.getStartDate();
        Integer endDate = newsRequestDto.getEndDate();
        String tempstartDateEncord = startDate.toString();
        System.out.println("tempstartDateEncord : " + tempstartDateEncord);
        String endDateEncord = null;

        if(keyword == null || startDate == null){
            return null;
        } else if (endDate == null){
            endDateEncord = startDate.toString();
        } else {
            endDateEncord = endDate.toString();
        }

        String startDateEncord = AddDate(tempstartDateEncord, 0,0,-1);
        String keywordEncord = null;
        String originStartDateEncord = startDateEncord;
        String originEndDateEncord = endDateEncord;
        try {
            keywordEncord = URLEncoder.encode(newsRequestDto.getSearchKeyword(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        NewsResponseDto newsResponseDto = null;

        for (int i = 0; i <= 4; i++) {

            String url = createRssURL("https://news.google.com/rss/search?q=",keywordEncord, startDateEncord, endDateEncord);

            RSSFeedParser parser = new RSSFeedParser(url);
            newsResponseDto = parser.readFeed();

            log.debug("탐색 스타트 날짜 : {}", startDateEncord);
            log.debug("객체있는가? : {}", newsResponseDto != null);
            if(newsResponseDto != null) log.debug("리스트 갯수 : {}", newsResponseDto.getMessages().size());
            if(newsResponseDto == null || newsResponseDto.getMessages().size() < 35){
                startDateEncord =  AddDate(startDateEncord, 0,0,-7);
            } else {
                newsResponseDto.setLink(createRssURL("https://news.google.com/search?q=",keywordEncord, startDateEncord, endDateEncord));
                return newsResponseDto;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        try {
            String[] randKeyword = new String[]{"주식", "주가", "코스피"};

            double randVal = Math.random();
            int randNum = (int)(randVal*randKeyword.length);
            if(randNum >= randKeyword.length) {
                randNum = randKeyword.length-1;
            }
            log.debug("랜덤 값 :  {}" , randNum);
            log.debug("randkeyword0 : {}", randKeyword[0]);
            log.debug("randkeyword1 : {}", randKeyword[1]);
            log.debug("randkeyword2 : {}", randKeyword[2]);
            keywordEncord = URLEncoder.encode(randKeyword[randNum], "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.debug("=============");
        String rurl = createRssURL("https://news.google.com/rss/search?q=",keywordEncord, originStartDateEncord, originEndDateEncord);
        System.out.println(rurl);
        RSSFeedParser parser = new RSSFeedParser(rurl);
        newsResponseDto = parser.readFeed();
        // 뉴스 더보기 링크 설정 (구글 뉴스)
        newsResponseDto.setLink(createRssURL("https://news.google.com/search?q=",keywordEncord, originStartDateEncord, originEndDateEncord));
        log.debug("탐색 스타트 날짜 : {}", startDateEncord);
        log.debug("리스트 갯수 : {}", newsResponseDto.getMessages().size());
        return newsResponseDto;
    }

    public NewsResponseDto findNaverNews(NewsRequestDto newsRequestDto) {

        String keyword = newsRequestDto.getSearchKeyword();
        Integer startDate = newsRequestDto.getStartDate();
        Integer endDate = newsRequestDto.getEndDate();
        String tempstartDateEncord = startDate.toString();
        System.out.println("tempstartDateEncord : " + tempstartDateEncord);
        String endDateEncord = null;

        if(keyword == null || startDate == null){
            return null;
        } else if (endDate == null){
            endDateEncord = startDate.toString();
        } else {
            endDateEncord = endDate.toString();
        }

        String startDateEncord = AddDate(tempstartDateEncord, 0,0,-30);
//        String keywordEncord = null;
        String originStartDateEncord = startDateEncord;
        String originEndDateEncord = endDateEncord;
//        try {
//            keywordEncord = URLEncoder.encode(newsRequestDto.getSearchKeyword(), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }

        NewsResponseDto newsResponseDto = new NewsResponseDto();

        int newsStartNo = 1;
        for (int i = 0; i < 2; i++) {

            String url = createNaverNewsURL("https://search.naver.com/search.naver?where=news&sm=tab_opt&sort=0&photo=0&field=0&pd=3&docid=&related=0&mynews=0&office_type=0&office_section_code=0&news_office_checked=&is_sug_officeid=0&"
                    , keyword, startDateEncord, endDateEncord, newsStartNo);
            newsStartNo+=10;
            System.out.println(url);
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert doc != null;

            Elements element = doc.select("a.news_tit");
//            System.out.println(element);
//            System.out.println("elementsize");
//            System.out.println(element.size());

            String title = null;
            String link = null;

            for (Element el : element) {
                title = el.attr("title");
                link = el.attr("href");
                NewsMessage newsMessage = NewsMessage.builder().title(title).link(link).build();
                newsResponseDto.addMessage(newsMessage);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        String[] randKeyword = new String[]{"주식", "주가", "코스피"};
        for (int i = 0; i < randKeyword.length-1; i++) {
            if(keyword.equals(randKeyword[i])){
                String setUrl = createNaverNewsURL("https://search.naver.com/search.naver?where=news&sm=tab_opt&sort=0&photo=0&field=0&pd=3&docid=&related=0&mynews=0&office_type=0&office_section_code=0&news_office_checked=&is_sug_officeid=0&"
                        , keyword, startDateEncord, endDateEncord, 1);
                newsResponseDto.setLink(setUrl);
                return newsResponseDto;
            }
        }

        if(newsResponseDto.getMessages().size() < 20){

            int randNum = 0;
            try {
                double randVal = Math.random();
                randNum = (int)(randVal*randKeyword.length);
                if(randNum >= randKeyword.length) {
                    randNum = randKeyword.length-1;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            newsRequestDto.setSearchKeyword(randKeyword[randNum]);
            findNaverNews(newsRequestDto);
        }
        String setUrl = createNaverNewsURL("https://search.naver.com/search.naver?where=news&sm=tab_opt&sort=0&photo=0&field=0&pd=3&docid=&related=0&mynews=0&office_type=0&office_section_code=0&news_office_checked=&is_sug_officeid=0&"
                , keyword, startDateEncord, endDateEncord, 1);
        newsResponseDto.setLink(setUrl);
        return newsResponseDto;

    }

    // URL 생성기
    public static String createNaverNewsURL(String sourceUrl, String keywordEncord, String startDateEncord, String endDateEncord, int startArticleNo) {
        StringBuilder sb = new StringBuilder(sourceUrl);
        sb.append("&query=").append(keywordEncord)
                .append("&ds=").append(endDateEncord, 0, 4).append(".")
                .append(endDateEncord,4,6).append(".")
                .append(endDateEncord,6,8)
                .append("&de=").append(startDateEncord, 0, 4).append(".")
                .append(startDateEncord,4,6).append(".")
                .append(startDateEncord,6,8)
                .append("&start=").append(startArticleNo);
        return sb.toString();
    }


    // URL 생성기
    public static String createRssURL(String sourceUrl, String keywordEncord, String startDateEncord, String endDateEncord) {
        StringBuilder sb = new StringBuilder(sourceUrl);
        sb.append(keywordEncord)
                .append("+before:").append(endDateEncord, 0, 4).append("/")
                .append(endDateEncord,4,6).append("/")
                .append(endDateEncord,6,8)
                .append("+after:").append(startDateEncord, 0, 4).append("/")
                .append(startDateEncord,4,6).append("/")
                .append(startDateEncord,6,8)
                .append("&hl=ko&gl=KR&ceid=KR:ko");
        return sb.toString();
    }

    // 정수형에서 날짜 추가 모듈
    public static String AddDate(String strDate, int year, int month, int day) {

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date dt = null;

        try {
            dt = dtFormat.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        cal.setTime(dt);
        cal.add(Calendar.YEAR,  year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE,  day);

        return dtFormat.format(cal.getTime());
    }


}
