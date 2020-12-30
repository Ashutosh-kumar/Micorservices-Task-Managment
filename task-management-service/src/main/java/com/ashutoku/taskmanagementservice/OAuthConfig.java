package com.ashutoku.taskmanagementservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;



//@EnableOAuth2Client
@Configuration
class OAuthConfig{

	@Value("${authserver.client.client-id:web}")
	private String clientId ;//clientId
	@Value("${authserver.client.clientSecret:webpass}")
	private String clientSecret ;//client secret
	@Value("${authserver.client.accessTokenUri:http://localhost:8282/oauth/token}")
	private String tokenUrl ;
	
	@Value("${authserver.user.username:krish}")
	private String username ;
	
	@Value("${authserver.user.password:krishpass}")
	private String password ;
	
	@Autowired(required = false)
	ClientHttpRequestFactory clientHttpRequestFactory;
	
	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	/*
    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
        ResourceOwnerPasswordResourceDetails resource;
        resource = new ResourceOwnerPasswordResourceDetails();
        //resource.setClientAuthenticationScheme(AuthenticationScheme.form);
        resource.setClientAuthenticationScheme(AuthenticationScheme.header);
        List<String>  scopes =  new ArrayList<String>();
        scopes.add("WRITE");
        scopes.add("READ");
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId("web");
        resource.setClientSecret(passwordEncoder.encode("webpass"));
        resource.setGrantType("password");
        resource.setScope(scopes);
        resource.setUsername("krish");
        resource.setPassword(passwordEncoder.encode("krishpass"));
        return resource;
    }

    @Bean
    public OAuth2RestTemplate restTemplate() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        
        OAuth2RestTemplate template = new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(
				new DefaultAccessTokenRequest()));
        prepareTemplate(template, false);
        
        template.setAccessTokenProvider(userAccessTokenProvider());
		return template;
		
       // return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
    }
    
    public OAuth2RestTemplate prepareTemplate(OAuth2RestTemplate template, boolean isClient) {
		template.setRequestFactory(getClientHttpRequestFactory());
		if (isClient) {
			template.setAccessTokenProvider(clientAccessTokenProvider());
		} else {
			template.setAccessTokenProvider(userAccessTokenProvider());
		}
		
		return template;
	}
    
    @Bean
	public AccessTokenProvider clientAccessTokenProvider() {
		ClientCredentialsAccessTokenProvider accessTokenProvider = new ClientCredentialsAccessTokenProvider();
		accessTokenProvider.setRequestFactory(getClientHttpRequestFactory());
		return accessTokenProvider;
	}
    
    @Bean
	public AccessTokenProvider userAccessTokenProvider() {
		ResourceOwnerPasswordAccessTokenProvider accessTokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
		accessTokenProvider.setRequestFactory(getClientHttpRequestFactory());
		return accessTokenProvider;
	}
    
    @Bean
    public ClientHttpRequestFactory getClientHttpRequestFactory() {
		if (clientHttpRequestFactory == null) {
			clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		}
		return clientHttpRequestFactory;
	}*/
    
    
}