package com.example.web_crawler_demo.service;

import com.example.web_crawler_demo.response.CrawlerResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {
    @Override
    public Optional<CrawlerResponse> crawl(String url) {
        return Optional.of(new CrawlerResponse("We are fine", List.of("blabal", "blabla")));
    }

}
