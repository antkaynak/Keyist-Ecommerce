package com.commerce.backend.security;

import com.commerce.backend.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private SecurityConstants securityConstants;

    @Bean
    public OpaqueTokenIntrospector introspector() {
        return new CustomAuthoritiesOpaqueTokenIntrospector(
                restTemplateBuilder,
                securityConstants.getClientId(),
                securityConstants.getClientPassword(),
                securityConstants.getConnectionTimeout(),
                securityConstants.getReadTimeout(),
                securityConstants.getAuthUrl(),
                securityConstants.getAuthUsername()
        );
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(securityConstants.getWhitelist()).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(opaqueToken -> opaqueToken
                                .introspector(introspector())
                        )
                );
    }
}
