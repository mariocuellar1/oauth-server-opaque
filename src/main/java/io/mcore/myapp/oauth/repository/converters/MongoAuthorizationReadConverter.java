package io.mcore.myapp.oauth.repository.converters;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import com.mongodb.DBObject;

@ReadingConverter
public class MongoAuthorizationReadConverter implements Converter<DBObject, OAuth2Authentication> {

	@Override
	@SuppressWarnings("unchecked")
	public OAuth2Authentication convert(DBObject source) {
		DBObject storedRequest = (DBObject) source.get("storedRequest");
		OAuth2Request oAuth2Request = new OAuth2Request((Map<String, String>) storedRequest.get("requestParameters"),
				(String) storedRequest.get("clientId"), null, true, new HashSet<String>((List<String>) storedRequest.get("scope")),
				null, null, null, null);
		DBObject userAuthorization = (DBObject) source.get("userAuthentication");
		Authentication userAuthentication = null;
		if (userAuthorization != null) {			
			Object principal = userAuthorization.get("principal");
			Object _authorities = userAuthorization.get("authorities");
			List<Map<String, String>> authorities = (List<Map<String, String>>) _authorities;
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>(authorities.size());
			for (Map<String, String> authority : authorities) {
				grantedAuthorities.add(new SimpleGrantedAuthority(authority.get("role")));
			}
			if (principal instanceof DBObject) {
				DBObject principalDBObject = (DBObject) principal;
				principal = new User(principalDBObject.get("username").toString(), "[PROTECTED]",
						(Boolean) principalDBObject.get("enabled"), (Boolean) principalDBObject.get("accountNonExpired"),
						(Boolean) principalDBObject.get("credentialsNonExpired"),
						(Boolean) principalDBObject.get("accountNonLocked"), grantedAuthorities);
			}
			userAuthentication = new UsernamePasswordAuthenticationToken(principal,
					(String) userAuthorization.get("credentials"),
					grantedAuthorities);			
		}

		OAuth2Authentication authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);
		return authentication;
	}

}
