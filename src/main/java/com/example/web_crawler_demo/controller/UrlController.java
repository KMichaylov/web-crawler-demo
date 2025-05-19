package com.example.web_crawler_demo.controller;

import com.example.web_crawler_demo.response.CrawlerResponse;
import com.example.web_crawler_demo.response.ErrorResponse;
import com.example.web_crawler_demo.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller that exposes endpoints for crawling URLs.
 */
@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    private final UrlService urlService;

    /**
     * Initializes the UrlController with the specified UrlService.
     *
     * @param urlService the service containing logic for crawling URLs
     */
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Handles a GET request to crawl the specified URL.
     *
     * @param target the URL to crawl
     * @return crawl result as a CrawlerResponse that is automatically converted to JSON on client side,
     * or an error message if the URL is invalid
     */
    @GetMapping("/pages")
    ResponseEntity<?> crawlForUrl(@RequestParam String target) {
        Optional<CrawlerResponse> crawlerOutput = urlService.crawl(target);
        return crawlerOutput
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.badRequest().body(new ErrorResponse("Invalid or malformed URL")));
    }

}
