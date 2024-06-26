package com.econovation.teamponyo.infrastructure.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//TODO: 굳이 팩토리를 만들 필요가 있을까
public class ClientRegistrationRepositoryFactory {
    private final ClientRegistrations clientRegistrations;
    public ClientRegistrationRepository create(){
        return new InMemoryClientRegistrationRepository(
                clientRegistrations.googleClientRegistration(),
                clientRegistrations.naverClientRegistration(),
                clientRegistrations.kakaoClientRegistration()
        );
    }
}
