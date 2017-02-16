package io.mcore.myapp.oauth.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Document(collection = "oauth_refresh_tokens")
public class AppRefreshToken {

	@Id
	private String id;
	private Date creationDate;
    private String tokenId;
    private OAuth2RefreshToken refreshToken;
    private OAuth2Authentication authentication;

    public AppRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
    	this.id = UUID.randomUUID().toString();
		this.creationDate = new Date();
    	this.refreshToken = refreshToken;
        this.authentication = authentication;
        this.tokenId = refreshToken.getValue();
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

    public OAuth2RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public OAuth2Authentication getAuthentication() {
        return authentication;
    }
    
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {			
			return true;
		}
		if (obj instanceof AppRefreshToken) {			
			AppRefreshToken that = (AppRefreshToken) obj;
			return id.equals(that.id);
		}
		return false;
	}
}
