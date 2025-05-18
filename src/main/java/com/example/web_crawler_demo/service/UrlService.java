package com.example.web_crawler_demo.service;

import com.example.web_crawler_demo.response.CrawlerResponse;

import java.util.Optional;

/**
 * Service interface for crawling URLs and retrieving structured page data.
 */
public interface UrlService {

    /**
     * Crawls the given URL and returns a CrawlerResponse if successful.
     *
     * @param url the target URL to crawl (must be a valid and reachable URL)
     * @return an Optional containing the CrawlerResponse if the crawl succeeds,
     * or empty Optional if the URL is invalid or unreachable
     */
    Optional<CrawlerResponse> crawl(String url);
}
