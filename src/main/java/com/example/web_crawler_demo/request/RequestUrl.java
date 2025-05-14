package com.example.web_crawler_demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class RequestUrl {

    @NotBlank(message = "URL must not be blank")
    @URL(message = "Invalid URL format")
    private String url;

    public RequestUrl(String url) {
        this.url = url;
    }

}
