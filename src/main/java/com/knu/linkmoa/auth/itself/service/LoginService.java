package com.knu.linkmoa.auth.itself.service;

import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다"));

        return new PrincipalDetails(member);
    }
}
