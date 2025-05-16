package com.example.web_crawler_demo;

import com.example.web_crawler_demo.response.CrawlerResponse;
import com.example.web_crawler_demo.service.UrlService;
import com.example.web_crawler_demo.service.UrlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebCrawlerServiceTest {
    private UrlService urlService;

    @BeforeEach
    void setUp() {
        urlService = new UrlServiceImpl();
    }

    @Test
    @DisplayName("The provided url is invalid, thus should return empty collection.")
    void shouldReturnInvalidUrl() {
        Optional<CrawlerResponse> response = urlService.crawl("blabla");
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("The provided url has empty scheme, so the crawler should return empty collection.")
    void shouldReturnEmptyForUrlWithoutScheme() {
        Optional<CrawlerResponse> response = urlService.crawl("www.google.com");
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("Simple valid url with only one link.")
    void shouldReturnValidResponseForProperUrl() {
        Optional<CrawlerResponse> response = urlService.crawl("https://example.com/");
        assertTrue(response.isPresent());
        assertEquals("https://example.com", response.get().getDomain());
        assertEquals("https://example.com/", response.get().getPages().getFirst());
    }
}
