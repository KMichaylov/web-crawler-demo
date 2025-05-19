# Web Crawler Repository
## Introduction
The current project is a demonstration for the working of a simple web crawler.
A web crawler is an automated program or bot that systematically searches websites for specific information.
Currently, our crawler has three variants:

* Iterative crawler
* Recursive crawler
* Asynchronous crawler

## Design
Below is a high-level architecture of the system:
![Image showing top-level architecture](Architecture.png "Top-level architecture")

## API Reference

#### Crawl pages for this URL

```http
    GET localhost:8080/api/v1/url/pages?target=example_url
```

| Parameter | Type     | Description                         |
| :-------- | :------- |:------------------------------------|
| `example_url` | `string` | **Required**. The URL to be crawled |

## Tools
* Java 22

* Spring Boot

* Jsoup (for HTML parsing)

* JUnit and MockMvc (for testing)

## Performance
In this section we examine the performance differences time-wise between the iterative crawler version,
the recursive and asynchronous one. The metrics are obtained by measuring the execution time of the ```crawl()``` method in the service layer,
as recorded by the controller.

| Algorithm    | Website | Time in seconds |
|--------------|-----|-----------------|
| Iterative    |https://crawler-test.com/| 210 seconds     |
| Recursive    |https://crawler-test.com/| 208 seconds     |
| Asynchonous  |https://crawler-test.com/| 17 seconds      |
| Iterative    |https://demo.cyotek.com/| 16 seconds      |
| Recursive    |https://demo.cyotek.com/| 14 seconds      |
| Asynchronous |https://demo.cyotek.com/| 2.5 seconds     |

**Observation**: The iterative and recursive approaches yield similar performance,
however the asynchronous crawler shows significant speed improvements, especially for larger websites.

## Project Structure
com.example.web_crawler_demo  
├── controller       → REST controller  
├── service          → Logic for crawling    
├── response         → Response model for the requests  
└── WebCrawlerDemoApplication.java → Class for starting Spring Boot application

## Running the Project
Below the steps for building and running the project are shown. It is important that Maven and Java 22 SDK is installed on your computer.

### Building Project
```bash
  git clone https://github.com/KMichaylov/web-crawler-demo.git
```

```bash
  cd web-crawler-demo
```

```bash
  mvn clean install
```
### Run Application

```bash
  mvn spring-boot:run
```