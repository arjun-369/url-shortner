package com.indus.url_shortening_service.service;

import com.indus.url_shortening_service.model.Url;
import com.indus.url_shortening_service.model.UrlDTO;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    public Url generateShortLink(UrlDTO urlDTO);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
}
