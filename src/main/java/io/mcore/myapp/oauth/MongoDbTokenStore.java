package io.mcore.myapp.oauth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import io.mcore.myapp.oauth.model.AppAccessToken;
import io.mcore.myapp.oauth.model.AppRefreshToken;
import io.mcore.myapp.oauth.repository.AppAccessTokenRepository;
import io.mcore.myapp.oauth.repository.AppRefreshTokenRepository;

@Service
public class MongoDbTokenStore implements TokenStore {

    private final AppAccessTokenRepository oAuth2AccessTokenRepository;

    private final AppRefreshTokenRepository oAuth2RefreshTokenRepository;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Autowired
    public MongoDbTokenStore(final AppAccessTokenRepository oAuth2AccessTokenRepository,
                                      final AppRefreshTokenRepository oAuth2RefreshTokenRepository) {
        this.oAuth2AccessTokenRepository = oAuth2AccessTokenRepository;
        this.oAuth2RefreshTokenRepository = oAuth2RefreshTokenRepository;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String tokenId) {
        return oAuth2AccessTokenRepository.findByTokenId(tokenId).getAuthentication();
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        AppAccessToken oAuth2AuthenticationAccessToken = new AppAccessToken(token,
                authentication, authenticationKeyGenerator.extractKey(authentication));
        oAuth2AccessTokenRepository.save(oAuth2AuthenticationAccessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        AppAccessToken token = oAuth2AccessTokenRepository.findByTokenId(tokenValue);
        if(token == null) {
            return null; 
        }
        OAuth2AccessToken accessToken = token.getAccessToken();
		return accessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        AppAccessToken accessToken = oAuth2AccessTokenRepository.findByTokenId(token.getValue());
        if(accessToken != null) {
            oAuth2AccessTokenRepository.delete(accessToken);
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
         oAuth2RefreshTokenRepository.save(new AppRefreshToken(refreshToken, authentication));
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        return oAuth2RefreshTokenRepository.findByTokenId(tokenValue).getRefreshToken();
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return oAuth2RefreshTokenRepository.findByTokenId(token.getValue()).getAuthentication();
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
    	AppRefreshToken toDelete = oAuth2RefreshTokenRepository.findByTokenId(token.getValue());
    	if (toDelete != null) {    		
    		oAuth2RefreshTokenRepository.delete(toDelete);
    	}
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
    	AppAccessToken toDelete = oAuth2AccessTokenRepository.findByRefreshToken(refreshToken.getValue());
    	if (toDelete != null) {    		
    		oAuth2AccessTokenRepository.delete(toDelete);
    	}
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        AppAccessToken token =  oAuth2AccessTokenRepository.findByAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        return token == null ? null : token.getAccessToken();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<AppAccessToken> tokens = oAuth2AccessTokenRepository.findByClientId(clientId);
        return extractAccessTokens(tokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<AppAccessToken> tokens = oAuth2AccessTokenRepository.findByClientIdAndUserName(clientId, userName);
        return extractAccessTokens(tokens);
    }

    private Collection<OAuth2AccessToken> extractAccessTokens(List<AppAccessToken> tokens) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
        for(AppAccessToken token : tokens) {
            accessTokens.add(token.getAccessToken());
        }
        return accessTokens;
    }

}
