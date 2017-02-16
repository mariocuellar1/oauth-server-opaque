package io.mcore.myapp.oauth.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.mcore.myapp.oauth.model.AppAccessToken;

@Repository
public interface AppAccessTokenRepository extends MongoRepository<AppAccessToken, String> {

	public AppAccessToken findByTokenId(String tokenId);

	public AppAccessToken findByRefreshToken(String refreshToken);

	public AppAccessToken findByAuthenticationId(String authenticationId);

	public List<AppAccessToken> findByClientIdAndUserName(String clientId, String userName);

	public List<AppAccessToken> findByClientId(String clientId);
}
