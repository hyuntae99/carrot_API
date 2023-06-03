package com.hyunn.carrot.login;

import com.hyunn.carrot.entity.Member;
import com.hyunn.carrot.repository.MemberRepository;
import com.hyunn.carrot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class LoginController {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    private String index() {
        return "index";
    }

    private static final String authorizationRequestBaseUri = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/login")
    public String getLoginPage(Model model) throws Exception {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        assert clientRegistrations != null;
        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);

        return "login";
    }

    @GetMapping("/login/{oauth2}")
    public String loginGoogle(@PathVariable String oauth2, HttpServletResponse httpServletResponse) {
        return "redirect:/oauth2/authorization/" + oauth2;
    }


    @GetMapping("/login_normal")
    public String login_normal() {
        return "login_normal";
    }

    @PostMapping("/login_normal")
    public String handleMemberLogin(@RequestParam String email, @RequestParam String password,
                                    HttpSession session, Model model) {

        // 직접 로그인 처리
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            if (member.getEmail().equals(email) && member.getPassword().equals(password)) {
                httpSession.setAttribute("authenticated", true);
                session.setAttribute("authenticated", true);

                Optional<User> optionalUser = userRepository.findByEmail(email);
                User user;

                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                } else {
                    // 사용자 정보를 세션에 저장
                    user = new User();
                    user.setEmail(email);
                    user.setName(member.getName());
                    user.setRole(Role.ROLE_USER);
                    user.setSite("normal");
                    userRepository.save(user);
                }

                // 권한 부여
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);


                httpSession.setAttribute("user",user);
                session.setAttribute("user", user);
                model.addAttribute("message", "로그인되었습니다.");
                model.addAttribute("searchUrl", "/");
                return "message";
            }
        }

        model.addAttribute("message", "아이디 혹은 비밀번호가 틀렸습니다.");
        model.addAttribute("searchUrl", "/login_normal");
        return "message";
    }

    @GetMapping("/signup")
    private String member() {
        return "signup";
    }

    @PostMapping("/signup")
    public String create(Member member, Model model) {
        try {
            Long memberId = memberService.save(member);
            if (memberId != null) {
                model.addAttribute("message", "회원가입이 완료되었습니다.");
                model.addAttribute("searchUrl", "/");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("message", "회원가입에 실패하였습니다.");
            model.addAttribute("searchUrl", "/signup");
        }
        return "message";
    }


}

