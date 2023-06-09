package com.udteam.miristock.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NewsMessage {

    private String title;
    private String link;
    private String pubDate;

}