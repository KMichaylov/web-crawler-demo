package com.example.web_crawler_demo.service;

import com.example.web_crawler_demo.response.CrawlerResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


/**
 * Implementation of the UrlService that uses Jsoup to crawl web pages.
 */
@Service
public class UrlServiceImpl implements UrlService {
    private final Set<String> visitedPages;
    private String domain;

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

    /**
     * A recursive crawl of the given URL.
     *
     * @param baseUrl the URL to start from
     * @param urls    the list to collect visited URLs
     */
    private void crawlPage(String baseUrl, List<String> urls) {
        if (visitedPages.contains(baseUrl) || !baseUrl.startsWith(domain)) {
            return;
        }

        List<String> links = fetchAndExtractLinks(baseUrl);
        if (!visitedPages.contains(baseUrl)) {
            return;
        }

        urls.add(baseUrl);
        for (String link : links) {
            crawlPage(link, urls);
        }
    }


    /**
     * An iterative crawl starting from the given URL.
     *
     * @param startUrl the URL to start crawling from
     * @param urls     the list to store collected URLs
     */
    private void crawlPageIterative(String startUrl, List<String> urls) {
        Queue<String> queue = new LinkedList<>();
        queue.offer(startUrl);

        int MAX_NUMBER_OF_PAGES = 1000;
        while (!queue.isEmpty() && visitedPages.size() < MAX_NUMBER_OF_PAGES) {
            String currentUrl = queue.poll();

            if (visitedPages.contains(currentUrl) || !currentUrl.startsWith(domain)) {
                continue;
            }

            List<String> allLinks = fetchAndExtractLinks(currentUrl);

            if (!visitedPages.contains(currentUrl)) {
                continue;
            }

            urls.add(currentUrl);
            queue.addAll(allLinks);
        }
    }

    /**
     * Fetches a page from the given URL and extracts internal links.
     *
     * @param url the URL to fetch and parse
     * @return a list of links found on the page from the same domain,
     * or an empty list if an error occurs
     */
    private List<String> fetchAndExtractLinks(String url) {
        try {
            int TIMEOUT = 5000;
            Document doc = Jsoup.connect(url)
                    .timeout(TIMEOUT)
                    .get();

            visitedPages.add(url);

            Elements linksOnPage = doc.select("a[href]");
            List<String> embeddedLinksInPage = new ArrayList<>();
            for (Element link : linksOnPage) {
                String absoluteUrl = link.absUrl("href");
                if (!visitedPages.contains(absoluteUrl) && absoluteUrl.startsWith(domain)) {
                    embeddedLinksInPage.add(absoluteUrl);
                }
            }

            return embeddedLinksInPage;
        } catch (IOException e) {
            System.err.println("Something went wrong with processing " + url + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }


}
