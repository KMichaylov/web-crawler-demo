package com.example.web_crawler_demo.service;

import com.example.web_crawler_demo.response.CrawlerResponse;

import java.util.Optional;

public interface UrlService {
    Optional<CrawlerResponse> crawl(String url);
}
