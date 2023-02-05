package com.udteam.miristock.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponseDto {

    private String title;
    private String link;
    private String description;
    private String lastBuildDate;
    private List<NewsMessage> entries = new ArrayList<>();

    public List<NewsMessage> getMessages() {
        return entries;
    }
    @Builder
    public NewsResponseDto(String title, String link, String description, String lastBuildDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.lastBuildDate = lastBuildDate;
    }

    @Override
    public String toString() {
        return "NewsResponseDto{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", lastBuildDate='" + lastBuildDate + '\'' +
                ", entries=" + entries +
                '}';
    }
}
