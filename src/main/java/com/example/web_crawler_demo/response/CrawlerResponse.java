package com.example.web_crawler_demo.response;

import lombok.Data;

import java.util.List;

/**
 * Class used for composing the result of crawling.
 */
@Data
public class CrawlerResponse {
    String domain;
    List<String> pages;

    /**
     * Constructs a new UrlController with the given UrlService.
     *
     * @param domain the domain of the targeted URL
     * @param pages  the crawled pages belonging to the domain
     */
    public CrawlerResponse(String domain, List<String> pages) {
        this.domain = domain;
        this.pages = pages;
    }
}
