package com.knu.linkmoa.auth.oauth2.service;

import com.knu.linkmoa.auth.oauth2.dto.response.GoogleResponse;
import com.knu.linkmoa.auth.oauth2.dto.response.NaverResponse;
import com.knu.linkmoa.auth.oauth2.dto.response.Oauth2Response;
import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.entity.Role;
import com.knu.linkmoa.domain.member.service.MemberService;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Oauth2Response oauth2Response = getOauth2Response(registrationId, oAuth2User);

        Member member = memberService.saveOrUpdate(Member.builder()
                .email(oauth2Response.getEmail())
                .role(Role.ROLE_USER)
                .build());

        PrincipalDetails principalDetails = new PrincipalDetails(member, oAuth2User.getAttributes());

        return principalDetails;
    }

    private static Oauth2Response getOauth2Response(String registrationId, OAuth2User oAuth2User) {
        if (registrationId.equals("google")) {
            return new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            return new NaverResponse(oAuth2User.getAttributes());
        }
        return null;
    }
}
