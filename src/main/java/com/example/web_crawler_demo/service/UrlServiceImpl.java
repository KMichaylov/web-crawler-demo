package com.example.web_crawler_demo.service;

import com.example.web_crawler_demo.response.CrawlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UrlServiceImpl implements UrlService {
    //    We need to crawl only pages from the same domain!!!
    private Set<String> visitedPages;
    private String domain;


    public UrlServiceImpl() {
        this.visitedPages = new HashSet<>();
        this.domain = "";
    }

    @Override
    public Optional<CrawlerResponse> crawl(String url) {

        return Optional.of(new CrawlerResponse("We are fine", List.of("blabal", "blabla")));
    }

    private void crawPage(String domainUrl, ArrayList<String> urls) {

    }

}
