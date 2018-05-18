package com.commerce.backend.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource-server-rest-api";

    private static final String SECURED_PATTERN = "/api/secured/**";


//    Not using remote because resource and authorization server both use the same database.
//
//    @Primary
//    @Bean
//    public RemoteTokenServices tokenService() {
//        RemoteTokenServices tokenService = new RemoteTokenServices();
//        tokenService.setCheckTokenEndpointUrl(
//                "http://server/oauth/check_token");
//        tokenService.setClientId("clientId");
//        tokenService.setClientSecret("clientPassword");
//        return tokenService;
//    }

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/category/**", "/api/product/**", "/api/account/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(SECURED_PATTERN).authenticated()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}