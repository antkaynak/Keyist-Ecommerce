package com.commerce.backend.security;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.client.RestOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class CustomAuthoritiesOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OpaqueTokenIntrospector delegate;
    private final String authUsername;

    public CustomAuthoritiesOpaqueTokenIntrospector(RestTemplateBuilder restTemplateBuilder,
                                                    String clientId,
                                                    String clientPassword,
                                                    String connectionTimeout,
                                                    String readTimeout,
                                                    String authUrl,
                                                    String authUsername) {
        this.authUsername = authUsername;
        RestOperations rest = restTemplateBuilder
                .basicAuthentication(clientId, clientPassword)
                .setConnectTimeout(Duration.ofSeconds(Integer.parseInt(connectionTimeout)))
                .setReadTimeout(Duration.ofSeconds(Integer.parseInt(readTimeout)))
                .build();

        delegate = new NimbusOpaqueTokenIntrospector(authUrl, rest);
    }

    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getAttribute(authUsername), principal.getAttributes(), extractAuthorities(principal));
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        List<String> scopes = principal.getAttribute(OAuth2IntrospectionClaimNames.SCOPE);
        return Objects.requireNonNull(scopes).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}