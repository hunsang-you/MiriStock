package com.udteam.miristock.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewsResponseDto {

    private String title;
    private String link;
    private String description;
    private String lastBuildDate;
    private List<NewsMessage> messages = new ArrayList<>();

    @Builder
    public NewsResponseDto(String title, String link, String description, String lastBuildDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.lastBuildDate = lastBuildDate;
    }

    public void addMessage(NewsMessage newsMessage){
        this.messages.add(newsMessage);
    }

}
