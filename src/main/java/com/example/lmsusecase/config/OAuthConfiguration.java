package com.example.lmsusecase.config;

import com.example.lmsusecase.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;


@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

	static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 60 * 60;
	static final int REFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60;
	@Value("${user.oauth.clientId}")
	String clientId;
	@Value("${user.oauth.clientSecret}")
	String clientSecret;

	@Value("${user.oauth.redirectUris}")
	String redirectUri;
	@Autowired
	private AuthenticationManager authenticationManager;


	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(clientId).secret("{noop}" + clientSecret)
				.authorizedGrantTypes(Constants.AuthorizationGrants.authorizationCode,
						Constants.AuthorizationGrants.password, Constants.AuthorizationGrants.refreshToken)
				.authorities("ROLE_CLIENT").scopes(Constants.AuthorizationGrants.userInfoScope)
				.resourceIds(Constants.resourceId).accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
				.redirectUris(redirectUri);
	}

}
