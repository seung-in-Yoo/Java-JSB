package com.ll.jsbwtl.domain.user.service;

import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.global.oauth.OAuth2Attrs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {

        OAuth2User fetched = new DefaultOAuth2UserService().loadUser(req);

        String provider = req.getClientRegistration().getRegistrationId(); // google/kakao/...
        Map<String,Object> attrs = fetched.getAttributes();
        OAuth2Attrs.Profile p = OAuth2Attrs.extract(provider, attrs);


        //없으면 가입, 있으면 갱신
        User user = userService.upsertOAuthUser(provider, p);

        // 권한 구성
        Collection<? extends GrantedAuthority> auth =
                List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // 컨트롤러에서 편하게 쓰라고 우리 userId를 attributes에 얹기
        Map<String,Object> merged = new HashMap<>(attrs);
        merged.put("localUserId", user.getId());
        merged.put("username", user.getUsername());

        String nameAttr = req.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(auth, merged, nameAttr);
    }
}

