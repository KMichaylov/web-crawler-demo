package com.example.web_crawler_demo;

import com.example.web_crawler_demo.controller.UrlController;
import com.example.web_crawler_demo.response.CrawlerResponse;
import com.example.web_crawler_demo.service.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UrlController.class)
public class WebCrawlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;


    @Test
    @DisplayName("Test controller get() request behaviour when passing correct url")
    void shouldReturnOkIfUrlIsValid() throws Exception {
        final String VALID_URL = "https://example.com";
        CrawlerResponse response = new CrawlerResponse(VALID_URL, List.of(VALID_URL + "/"));
        when(urlService.crawl(VALID_URL)).thenReturn(Optional.of(response));

        mockMvc.perform(get(String.format("/api/v1/url/pages?target=%s", VALID_URL)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.domain").value(VALID_URL))
                .andExpect(jsonPath("$.pages").value(VALID_URL + "/"));
    }

    @Test
    @DisplayName("Test controller get() request behaviour when passing wrong url")
    void shouldReturnBadRequestIfUrlIsInvalid() throws Exception {
        final String WRONG_URL = "https://exampe.com";
        when(urlService.crawl(WRONG_URL)).thenReturn(Optional.empty());

        mockMvc.perform(get(String.format("/api/v1/url/pages?target=%s", WRONG_URL)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid or malformed URL"));
    }

}
