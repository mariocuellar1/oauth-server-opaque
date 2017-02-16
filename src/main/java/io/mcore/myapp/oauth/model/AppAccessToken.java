package io.mcore.myapp.oauth.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Document(collection = "oauth_tokens")
public class AppAccessToken {

	@Id
	private String id;
	private Date creationDate;
	private String tokenId;
	private OAuth2AccessToken accessToken;
	private String authenticationId;
	private String userName;
	private String clientId;
	private OAuth2Authentication authentication;
	private String refreshToken;

	public AppAccessToken(final OAuth2AccessToken accessToken,
			final OAuth2Authentication authentication, final String authenticationId) {
		this.id = UUID.randomUUID().toString();
		this.creationDate = new Date();
		this.tokenId = accessToken.getValue();
		this.accessToken = accessToken;
		this.authenticationId = authenticationId;
		this.userName = authentication.getName();
		this.clientId = authentication.getOAuth2Request().getClientId();
		this.authentication = authentication;
		this.refreshToken = accessToken.getRefreshToken().getValue();
	}

	public String getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getTokenId() {
		return tokenId;
	}

	public OAuth2AccessToken getAccessToken() {
		return accessToken;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public String getUserName() {
		return userName;
	}

	public String getClientId() {
		return clientId;
	}

	public OAuth2Authentication getAuthentication() {
		return authentication;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {			
			return true;
		}
		if (obj instanceof AppAccessToken) {			
			AppAccessToken that = (AppAccessToken) obj;
			return id.equals(that.id);
		}
		return false;
	}
}
