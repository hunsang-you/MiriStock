package com.udteam.miristock.service;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.repository.FinancialstatementRepository;
import com.udteam.miristock.util.RSSFeedParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        StringBuilder sb = new StringBuilder("https://news.google.com/rss/search?q=");
        sb.append(keywordEncord)
                .append("+before:").append(endDateEncord, 0, 4).append("/")
                .append(endDateEncord,4,6).append("/")
                .append(endDateEncord,6,8)
                .append("+after:").append(startDateEncord, 0, 4).append("/")
                .append(startDateEncord,4,6).append("/")
                .append(startDateEncord,6,8)
                .append("&hl=ko&gl=KR&ceid=KR:ko");

        System.out.println(sb.toString());
        RSSFeedParser parser = new RSSFeedParser(sb.toString());
        NewsResponseDto newsResponseDto = parser.readFeed();
        System.out.println("NEWS output :" + newsResponseDto);
        return newsResponseDto;
    }

    private static String AddDate(String strDate, int year, int month, int day) {

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
