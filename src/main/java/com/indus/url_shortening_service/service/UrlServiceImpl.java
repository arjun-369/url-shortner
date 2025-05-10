package com.indus.url_shortening_service.service;

import com.google.common.hash.Hashing;
import com.indus.url_shortening_service.model.Url;
import com.indus.url_shortening_service.model.UrlDTO;
import com.indus.url_shortening_service.repository.UrlRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UrlServiceImpl implements UrlService {
    private static final Logger LOG = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDTO urlDTO) {
        if(StringUtils.isNoneEmpty(urlDTO.getUrl())){
            Url urlCheck = urlRepository.findByOriginalUrl(urlDTO.getUrl());
            if(urlCheck!= null) {
                return urlCheck;
            }
            
            LOG.info("Generate Shot link for URL: {}", urlDTO.getUrl());

            String encodedUrl = encodeUrl(urlDTO.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDTO.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDTO.getExpirationDate(), urlToPersist.getCreationDate()));
            Url urlToRet = persistShortLink(urlToPersist);

            if (urlToRet!=null)
                return urlToRet;
            return null;
        }
        return null;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {

        if (StringUtils.isBlank(expirationDate)){
            return creationDate.plusHours(2);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(expirationDate, formatter);
    }

    private String encodeUrl(String url) {
        String encodeUrl = "";
        LocalDateTime time = LocalDateTime.now();
        // why only this hash function?
        encodeUrl = Hashing.murmur3_32_fixed().
                hashString(url.concat(time.toString()), StandardCharsets.UTF_8).
                toString();
        return encodeUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        Url urlToRet = urlRepository.save(url);
        return urlToRet;
    }

    @Override
    public Url getEncodedUrl(String url) {
        Url urlToRet = urlRepository.findByShortLink(url);
        return urlToRet;
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}
