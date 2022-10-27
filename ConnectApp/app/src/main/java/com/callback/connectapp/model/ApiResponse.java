package com.callback.connectapp.model;

public class ApiResponse {
    private String message = "Not Found";
    private Integer status = 404;

    public ApiResponse() {
    }

    public ApiResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
