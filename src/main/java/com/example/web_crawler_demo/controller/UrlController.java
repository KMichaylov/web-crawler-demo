package com.example.web_crawler_demo.controller;

import com.example.web_crawler_demo.response.CrawlerResponse;
import com.example.web_crawler_demo.service.UrlService;
import netscape.javascript.JSObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/pages")
    ResponseEntity<CrawlerResponse> crawlForUrl(@RequestParam String target) {
        Optional<CrawlerResponse> crawlerOutput = urlService.crawl(target);

        return crawlerOutput.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
