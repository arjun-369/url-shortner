package com.indus.url_shortening_service.model;

public class UrlDTO {

    private String url;
    private String expirationDate;  // optional

    public UrlDTO(String url, String expirayDate) {
        this.url = url;
        this.expirationDate = expirayDate;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "UrlDTO{" +
                "url='" + url + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }
}
