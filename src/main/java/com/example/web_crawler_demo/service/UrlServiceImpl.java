package com.example.web_crawler_demo.service;

import com.example.web_crawler_demo.response.CrawlerResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class UrlServiceImpl implements UrlService {
    private final Set<String> visitedPages;
    private String domain;
    private final int TIMEOUT = 5000;

    public UrlServiceImpl() {
        this.visitedPages = new HashSet<>();
        this.domain = "";
    }

    @Override
    public Optional<CrawlerResponse> crawl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null || uri.getHost() == null) {
                return Optional.empty();
            }
            domain = uri.getScheme() + "://" + uri.getHost();
            visitedPages.clear();
            List<String> pages = new ArrayList<>();
            crawlPageIterative(url, pages);
            return Optional.of(new CrawlerResponse(domain, pages));
        } catch (URISyntaxException e) {
            System.out.println("An syntax uri error occurred: " + e.getMessage());
            return Optional.empty();
        }
    }

    private void crawlPage(String baseUrl, List<String> urls) {
        if (visitedPages.contains(baseUrl) || !baseUrl.startsWith(domain)) {
            return;
        }
        try {
            // Get the whole html page for the link
            Document doc = Jsoup.connect(baseUrl)
                    .timeout(TIMEOUT)
                    .get();

            visitedPages.add(baseUrl);
            urls.add(baseUrl);

            // Go through each link on the page and nest inside it
            Elements linksOnPage = doc.select("a[href]");
            for (Element link : linksOnPage) {
                String absoluteUrl = link.absUrl("href");
                crawlPage(absoluteUrl, urls);
            }
        } catch (IOException e) {
            System.err.println("Something went wrong with processing" + baseUrl + ": " + e.getMessage());
        }
    }

    private void crawlPageIterative(String startUrl, List<String> urls) {
        Queue<String> queue = new LinkedList<>();
        queue.offer(startUrl);

        int MAX_NUMBER_OF_PAGES = 1000;
        while (!queue.isEmpty() && visitedPages.size() < MAX_NUMBER_OF_PAGES) {
            String currentUrl = queue.poll();

            if (visitedPages.contains(currentUrl) || !currentUrl.startsWith(domain)) {
                continue;
            }

            try {
                Document doc = Jsoup.connect(currentUrl)
                        .timeout(TIMEOUT)
                        .get();

                visitedPages.add(currentUrl);
                urls.add(currentUrl);

                Elements linksOnPage = doc.select("a[href]");
                for (Element link : linksOnPage) {
                    String absoluteUrl = link.absUrl("href");
                    if (!visitedPages.contains(absoluteUrl) && absoluteUrl.startsWith(domain)) {
                        queue.offer(absoluteUrl);
                    }
                }
            } catch (IOException e) {
                System.err.println("Something went wrong with processing " + currentUrl + ": " + e.getMessage());
            }
        }
    }

}
