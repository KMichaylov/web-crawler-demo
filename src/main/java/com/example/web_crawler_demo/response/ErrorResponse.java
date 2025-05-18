package com.example.web_crawler_demo.response;

import lombok.Getter;

/**
 * A simple class representing an error response.
 */
@Getter
public class ErrorResponse {
    private String error;

    /**
     * Constructs a new ErrorResponse with the specified error message.
     *
     * @param error the error message to return
     */
    public ErrorResponse(String error) {
        this.error = error;
    }

}
