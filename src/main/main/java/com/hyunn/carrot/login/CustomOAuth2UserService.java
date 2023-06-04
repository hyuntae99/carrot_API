package com.hyunn.carrot.login;

import com.hyunn.carrot.entity.Member;
import com.hyunn.carrot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 서비스 구분을 위한 작업 (구글 : google, 네이버 : naver, 카카오 : kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        String email;
        String name;
        String site;
        Map<String, Object> response = oAuth2User.getAttributes();

        if (registrationId.equals("naver")) {
            Map<String, Object> hash = (Map<String, Object>) response.get("response");
            email = (String) hash.get("email");
            name = (String) hash.get("name");
            site = "naver";

        } else if (registrationId.equals("google")) {
            email = (String) response.get("email");
            name = (String) response.get("name"); // 권한이 없음
            site = "google";
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            // 전화번호는 권한이 없어서 email에서 @ 이후의 부분을 제거하여 네이버 아이디와 차별점을 준다.
            int atIndex = email.indexOf("@");
            if (atIndex != -1) {
                email = email.substring(0, atIndex);
            }
            email += "@kakao.com";

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            name = (String) profile.get("nickname");
            site = "kakao";
        } else {
            throw new OAuth2AuthenticationException("허용되지 않은 인증입니다.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            if (name == null)
                user.setName("권한없음");
            else
                user.setName(name);
            user.setSite(site);
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);

            // 쇼셜 회원가입으로 회원 등록
            Member new_member = new Member();
            new_member.update(email, "asd123!@#", name, "유저", "01000000000",
                    "랜덤생성랜덤생성랜덤생성", 36, null, 0);
            new_member.setSite(site);
            memberRepository.save(new_member);
        }

        httpSession.setAttribute("user", user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }
}
