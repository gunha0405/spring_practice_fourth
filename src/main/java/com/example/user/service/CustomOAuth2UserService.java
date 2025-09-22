package com.example.user.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.user.model.SiteUser;
import com.example.user.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");


        SiteUser user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = SiteUser.builder()
                    .email(email)
                    .username(name)
                    .provider(provider.toUpperCase())
                    .providerId(providerId)
                    .build();
        } else {
            user.setUsername(name);
        }

        userRepository.save(user);

        return new CustomUserDetails(
                user,
                oAuth2User.getAuthorities(), 
                attributes
        );
    }
}