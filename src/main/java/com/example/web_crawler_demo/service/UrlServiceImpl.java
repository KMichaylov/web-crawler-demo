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
import java.util.concurrent.*;


/**
 * Implementation of the UrlService that uses Jsoup to crawl web pages.
 */
@Service
public class UrlServiceImpl implements UrlService {
    private final Set<String> visitedPages;
    private String domain;
    private final static int MAX_NUMBER_OF_PAGES = 1000;

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
            crawlPageAsynchronous(url, pages);
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
        if (!baseUrl.startsWith(domain) || !visitedPages.add(baseUrl)) {
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

        while (!queue.isEmpty() && visitedPages.size() < MAX_NUMBER_OF_PAGES) {
            String currentUrl = queue.poll();

            if (!currentUrl.startsWith(domain) || !visitedPages.add(currentUrl)) {
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
     * An asynchronous crawl starting from the given URL.
     *
     * @param startUrl the URL to start crawling from
     * @param urls     the list to store collected URLs
     */
    private void crawlPageAsynchronous(String startUrl, List<String> urls) {
        int MAX_THREADS = 16;

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<>());
        Set<String> collectedUrls = Collections.newSetFromMap(new ConcurrentHashMap<>());

        queue.offer(startUrl);

        while (!queue.isEmpty() && visited.size() < MAX_NUMBER_OF_PAGES) {
            List<Future<?>> futures = new ArrayList<>();

            while (!queue.isEmpty()) {
                String currentUrl = queue.poll();

                if (currentUrl == null || !currentUrl.startsWith(domain) || !visited.add(currentUrl)) {
                    continue;
                }

                futures.add(executor.submit(() -> {
                    List<String> linksOnSameDomain = fetchAndExtractLinks(currentUrl, visited);
                    collectedUrls.add(currentUrl);
                    queue.addAll(linksOnSameDomain);
                }));
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    System.err.println("Something went wrong: " + e.getMessage());
                }
            }
        }

        executor.shutdown();

        synchronized (urls) {
            urls.clear();
            urls.addAll(collectedUrls);
        }
    }


    /**
     * Invokes the fetchAndExtractLinks with the existing set of visited pages.
     *
     * @param url the URL to fetch and parse
     * @return @return a list of links found on the page from the same domain,
     * or an empty list if an error occurs
     */
    private List<String> fetchAndExtractLinks(String url) {
        return fetchAndExtractLinks(url, visitedPages);
    }

    /**
     * Fetches a page from the given URL and extracts internal links.
     *
     * @param url          the URL to fetch and parse
     * @param visitedPages the set of currently visited pages
     * @return a list of links found on the page from the same domain,
     * or an empty list if an error occurs
     */
    private List<String> fetchAndExtractLinks(String url, Set<String> visitedPages) {
        try {
            int TIMEOUT = 5000;
            Document doc = Jsoup.connect(url)
                    .ignoreHttpErrors(true)
                    .followRedirects(true)
                    .timeout(TIMEOUT)
                    .get();


            Elements linksOnPage = doc.select("a[href]");
            List<String> embeddedLinks = new ArrayList<>();
            for (Element link : linksOnPage) {
                String absoluteUrl = link.absUrl("href");
                if (absoluteUrl.startsWith(domain)) {
                    embeddedLinks.add(absoluteUrl);
                }
            }

            return embeddedLinks;
        } catch (IOException e) {
            System.err.println("Something went wrong with processing " + url + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }


}
