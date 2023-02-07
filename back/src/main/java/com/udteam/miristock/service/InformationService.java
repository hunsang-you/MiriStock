package com.udteam.miristock.service;

import com.udteam.miristock.dto.FinancialstatementDto;
import com.udteam.miristock.dto.NewsRequestDto;
import com.udteam.miristock.dto.NewsResponseDto;
import com.udteam.miristock.repository.FinancialstatementRepository;
import com.udteam.miristock.util.RSSFeedParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        try {
            keywordEncord = URLEncoder.encode(newsRequestDto.getSearchKeyword(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Integer newsDataSize = 0;
        NewsResponseDto newsResponseDto = null;
        int weekCount = 0;

            String url = createRssURL("https://news.google.com/rss/search?q=",keywordEncord, startDateEncord, endDateEncord);

            RSSFeedParser parser = new RSSFeedParser(url);
            newsResponseDto = parser.readFeed();

            // 뉴스 개수가 35개 이상일때까지 일주일씩 뒤로 탐색
//            newsDataSize = newsResponseDto.getMessages().size();

//            if(newsDataSize == null) newsDataSize = 0;
//            log.info("총 뉴스 메세지 개수 : {}", newsDataSize);
            log.info("탐색 스타트 날짜 : {}", startDateEncord);

//            startDateEncord =  AddDate(tempstartDateEncord, 0,0,-6);
//            weekCount++;

            newsResponseDto.setLink(createRssURL("https://news.google.com/search?q=",keywordEncord, startDateEncord, endDateEncord));

        // 뉴스 더보기 링크 설정 (구글 뉴스)

        return newsResponseDto;
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
