package com.project.prodstat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;


@Configuration
public class SpotifyWebClient {
    @Bean
    public WebClient spotifyWebCl(ClientRegistrationRepository clientRegistrationRepository,
                               OAuth2AuthorizedClientRepository authorizedClientRepository) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                        authorizedClientRepository);
        oauth2Filter.setDefaultClientRegistrationId("spotify-client");

        return WebClient.builder()
                .baseUrl("https://api.spotify.com/v1")
                .apply(oauth2Filter.oauth2Configuration())
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("spotify-client")
                .clientId("9220e8978adb4ad4a6ab2bed7a0ba0b6")
                .clientSecret("fea38c5706e147d9acf36932c9898d33")
                .tokenUri("https://accounts.spotify.com/api/token")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();

        return new InMemoryClientRegistrationRepository(Collections.singletonList(clientRegistration));
    }
}
