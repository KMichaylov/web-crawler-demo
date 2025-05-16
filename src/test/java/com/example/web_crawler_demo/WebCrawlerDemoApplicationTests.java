package com.example.web_crawler_demo;

import com.example.web_crawler_demo.controller.UrlController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebCrawlerDemoApplicationTests {

	@Autowired
	private UrlController urlController;

	@Test
	void contextLoads() {
		assertThat(urlController).isNotNull();
	}

}
