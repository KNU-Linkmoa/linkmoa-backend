package com.knu.linkmoa.domain.sharepage.service;

import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.domain.sharepage.dto.request.SharePageCreateDto;
import com.knu.linkmoa.domain.sharepage.entity.MemberSharePage;
import com.knu.linkmoa.domain.sharepage.entity.SharePage;
import com.knu.linkmoa.domain.sharepage.repository.MemberSharePageRepository;
import com.knu.linkmoa.domain.sharepage.repository.SharePageRepository;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SharePageService {

    private final MemberRepository memberRepository;
    private final MemberSharePageRepository memberSharePageRepository;
    private final SharePageRepository sharePageRepository;

    public ApiResponseSpec createSharePage(SharePageCreateDto sharePageCreateDto,
                                           PrincipalDetails principalDetails){

        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 email에 해당하는 Member가 없습니다"));

        SharePage sharePage = SharePage.builder()
                .title(sharePageCreateDto.getTitle())
                .build();
        sharePageRepository.save(sharePage);

        MemberSharePage memberSharePage = MemberSharePage.builder()
                .member(member)
                .sharePage(sharePage)
                .build();
        memberSharePageRepository.save(memberSharePage);

        return new ApiResponseSpec(true, 200, "공유 페이지 생성 성공!");
    }

}
