package com.indus.url_shortening_service.controller;


import com.indus.url_shortening_service.model.UrlErrorResponseDTO;
import com.indus.url_shortening_service.model.UrlResponseDTO;
import com.indus.url_shortening_service.service.UrlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.indus.url_shortening_service.model.UrlDTO;
import com.indus.url_shortening_service.model.Url;

@RestController
public class UrlShorteningController {

    @Autowired
    private UrlServiceImpl urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDTO urlDTO){

        Url urlToRet = urlService.generateShortLink(urlDTO);

        if (urlToRet!=null){
            UrlResponseDTO urlResponseDTO = new UrlResponseDTO();
            urlResponseDTO.setOriginalUrl(urlDTO.getUrl());
            urlResponseDTO.setShortLink(urlToRet.getShortLink());
            urlResponseDTO.setExpirationDate(urlToRet.getExpirationDate());

            return new ResponseEntity<UrlResponseDTO>(urlResponseDTO,HttpStatus.OK);

        }

        UrlErrorResponseDTO urlErrorResponseDTO = new UrlErrorResponseDTO();
        urlErrorResponseDTO.setStatus("500");
        urlErrorResponseDTO.setError("there was an error processing your request. please try again");

        return new ResponseEntity<UrlErrorResponseDTO>(urlErrorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
