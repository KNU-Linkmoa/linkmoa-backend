package com.knu.linkmoa.domain.member.service;

import com.knu.linkmoa.auth.itself.dto.request.SignUpRequestDto;
import com.knu.linkmoa.auth.itself.dto.response.TokenResponseDto;
import com.knu.linkmoa.auth.jwt.service.JwtService;
import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.error.MemberErrorCode;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.global.error.custom.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.knu.linkmoa.domain.member.entity.Role.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public TokenResponseDto signUp(SignUpRequestDto signUpRequest) {

        Optional<Member> optionalExistingMember = memberRepository.findByEmail(signUpRequest.email());

        // 이메일이 중복되면 오류 발생
        if (optionalExistingMember.isPresent()) {
            throw new MemberException(MemberErrorCode.MEMBER_ALREADY_EXISTS_EMAIL);
        }

        Member member = Member.builder()
                .nickName(signUpRequest.nickName())
                .email(signUpRequest.email())
                .role(ROLE_USER)
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();

        memberRepository.save(member);

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(jwtService.createAccessToken(member.getEmail(), ROLE_USER.name()))
                .refreshToken(jwtService.createRefreshToken())
                .build();

        return tokenResponseDto;
    }

    public Member saveOrUpdate(Member member) {
        Optional<Member> optionalExistingMember = memberRepository.findByEmail(member.getEmail());

        if (optionalExistingMember.isPresent()) {
            Member existingMember = optionalExistingMember.get();
            existingMember.updateMember(member);

            return memberRepository.save(existingMember);
        } else {
            return memberRepository.save(member);
        }
    }

    public Member findMemberByEmail(String email) throws UsernameNotFoundException {
        // 예외처리 해야함
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다"));

        return member;
    }

    public void saveRefresh(String email, String refreshToken) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email에 해당하는 유저가 없습니다"));

        member.updateRefreshToken(refreshToken);

        memberRepository.save(member);
    }

    public boolean checkDuplicateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

}
