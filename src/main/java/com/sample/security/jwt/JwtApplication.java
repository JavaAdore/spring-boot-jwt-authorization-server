package com.sample.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class JwtApplication {

	@Value("${config.oauth2.privateKey}")
	 private String privateKey;

	@PostConstruct
	public void init()
	{



	}



	@Autowired
	AuthenticationManager authenticationManager;
	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);


	}

}

