package com.indus.url_shortening_service.controller;


import com.indus.url_shortening_service.model.UrlErrorResponseDTO;
import com.indus.url_shortening_service.model.UrlResponseDTO;
import com.indus.url_shortening_service.service.UrlServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.indus.url_shortening_service.model.UrlDTO;
import com.indus.url_shortening_service.model.Url;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {

    private static final Logger LOG = LoggerFactory.getLogger(UrlShorteningController.class);

    @Autowired
    private UrlServiceImpl urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDTO urlDTO){
        try {

            Url urlToRet = urlService.generateShortLink(urlDTO);

            if (urlToRet != null) {
                UrlResponseDTO urlResponseDTO = new UrlResponseDTO();
                urlResponseDTO.setOriginalUrl(urlDTO.getUrl());
                urlResponseDTO.setShortLink(urlToRet.getShortLink());
                urlResponseDTO.setExpirationDate(urlToRet.getExpirationDate());

                return new ResponseEntity<UrlResponseDTO>(urlResponseDTO, HttpStatus.OK);
            } else {
                throw new Exception("Short URL not generated");
            }
        } catch (Exception ex) {
            LOG.error("Error generating short link {}.", ex.getMessage());
            UrlErrorResponseDTO urlErrorResponseDTO = new UrlErrorResponseDTO();
            urlErrorResponseDTO.setStatus("500");
            urlErrorResponseDTO.setError("there was an error processing your request. please try again");

            return new ResponseEntity<UrlErrorResponseDTO>(urlErrorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectURL(@PathVariable String shortLink, HttpServletResponse response) throws IOException {

        if(StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDTO urlErrorResponseDTO = new UrlErrorResponseDTO();
            urlErrorResponseDTO.setError("Invalid short link");
            urlErrorResponseDTO.setStatus("400");
            return new ResponseEntity<>(urlErrorResponseDTO, HttpStatus.OK);
        }
        Url urlToRet = urlService.getEncodedUrl(shortLink);

        if(urlToRet == null) {
            UrlErrorResponseDTO urlErrorResponseDTO = new UrlErrorResponseDTO();
            urlErrorResponseDTO.setError("URL does not exist or it might be expired");
            urlErrorResponseDTO.setStatus("400");
            return new ResponseEntity<>(urlErrorResponseDTO, HttpStatus.OK);
        }

        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {
            UrlErrorResponseDTO urlErrorResponseDTO = new UrlErrorResponseDTO();
            urlErrorResponseDTO.setError("URL expired. Try generating a new short link");
            urlErrorResponseDTO.setStatus("200");
            return new ResponseEntity<>(urlErrorResponseDTO, HttpStatus.OK);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }
}
