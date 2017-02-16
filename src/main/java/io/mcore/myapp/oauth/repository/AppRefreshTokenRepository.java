package io.mcore.myapp.oauth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.mcore.myapp.oauth.model.AppRefreshToken;

@Repository
public interface AppRefreshTokenRepository extends MongoRepository<AppRefreshToken, String> {

    public AppRefreshToken findByTokenId(String tokenId);
}
