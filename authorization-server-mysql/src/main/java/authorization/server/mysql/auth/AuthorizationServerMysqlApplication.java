package authorization.server.mysql.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class AuthorizationServerMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerMysqlApplication.class, args);
	}

}
