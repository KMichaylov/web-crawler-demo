package com.example.web_crawler_demo.response;

import lombok.Data;

import java.util.List;

@Data
public class CrawlerResponse {
    String domain;
    List<String> pages;

    public CrawlerResponse(String domain, List<String> pages) {
        this.domain = domain;
        this.pages = pages;
    }
}
