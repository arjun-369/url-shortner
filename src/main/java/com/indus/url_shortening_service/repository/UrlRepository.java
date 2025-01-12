package com.indus.url_shortening_service.repository;

import com.indus.url_shortening_service.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends MongoRepository<Url, String>
{
    @Query("{shortLink:'?'}")
    public Url findByShortLink(String shortLink);
}
