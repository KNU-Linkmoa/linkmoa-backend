package com.knu.linkmoa.domain.sharepage.service;

import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.domain.sharepage.dto.request.SharePageCreateDto;
import com.knu.linkmoa.domain.sharepage.dto.request.SharePageInviteDto;
import com.knu.linkmoa.domain.sharepage.dto.response.ApiSharePageResponse;
import com.knu.linkmoa.domain.sharepage.entity.MemberSharePage;
import com.knu.linkmoa.domain.sharepage.entity.SharePage;
import com.knu.linkmoa.domain.sharepage.error.MemberSharePageErrorCode;
import com.knu.linkmoa.domain.sharepage.error.SharePageErrorCode;
import com.knu.linkmoa.domain.sharepage.repository.MemberSharePageRepository;
import com.knu.linkmoa.domain.sharepage.repository.SharePageRepository;
import com.knu.linkmoa.global.error.custom.MemberSharePageException;
import com.knu.linkmoa.global.error.custom.SharePageException;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SharePageService {

    private final MemberRepository memberRepository;
    private final MemberSharePageRepository memberSharePageRepository;
    private final SharePageRepository sharePageRepository;

    public ApiSharePageResponse<Long> createSharePage(SharePageCreateDto sharePageCreateDto,
                                                      PrincipalDetails principalDetails) {

        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 email에 해당하는 Member가 없습니다"));

        SharePage sharePage = SharePage.builder()
                .title(sharePageCreateDto.getTitle())
                .build();
        sharePageRepository.save(sharePage);

        MemberSharePage memberSharePage = MemberSharePage.builder()
                .accept(true)
                .member(member)
                .sharePage(sharePage)
                .build();
        memberSharePageRepository.save(memberSharePage);


        return ApiSharePageResponse.<Long>builder()
                .status(true)
                .code(200)
                .message("공유 페이지 생성에 성공하였습니다.")
                .data(sharePage.getId())
                .build();
    }

    public ApiResponseSpec inviteSharePage(SharePageInviteDto sharePageInviteDto, Long sharePageId, PrincipalDetails principalDetails) {

        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 email에 해당하는 Member가 없습니다"));

        SharePage sharePage = sharePageRepository.findById(sharePageId)
                .orElseThrow(() -> new SharePageException(SharePageErrorCode.SHARE_PAGE_NOT_FOUND_EXCEPTION));

        // 초대를 보낸 사람의 공유페이지가 존재하는지부터 확인
        if (memberSharePageRepository.existsByMemberAndSharePage(member, sharePage)) {
            throw new MemberSharePageException(MemberSharePageErrorCode.MEMBER_SHARE_PAGE_NOT_FOUND_EXCEPTION);
        }

        List<Member> membersToInvite = sharePageInviteDto.getEmails().stream()
                .map(email -> memberRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(email + "에 해당하는 Member가 없습니다"))
                )
                .toList();

        membersToInvite.forEach(findMember -> {
            if (memberSharePageRepository.existsByMemberAndSharePage(findMember, sharePage)) {
                MemberSharePage newMemberSharePage = MemberSharePage.builder()
                        .accept(false)
                        .sharePage(sharePage)
                        .member(findMember)
                        .build();

                memberSharePageRepository.save(newMemberSharePage);
            }
        });

        return new ApiResponseSpec(true, 200, "공유 페이지 초대를 성공하였습니다");
    }

    public ApiResponseSpec acceptSharePage(Long sharePageId, PrincipalDetails principalDetails){

        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 email에 해당하는 Member가 없습니다"));

        SharePage sharePage = sharePageRepository.findById(sharePageId)
                .orElseThrow(() -> new SharePageException(SharePageErrorCode.SHARE_PAGE_NOT_FOUND_EXCEPTION));

        MemberSharePage memberSharePage = memberSharePageRepository.findByMemberAndSharePage(member, sharePage)
                .orElseThrow(() -> new MemberSharePageException(MemberSharePageErrorCode.MEMBER_SHARE_PAGE_NOT_FOUND_EXCEPTION));

        if(memberSharePage.isAccept()){
            throw new MemberSharePageException(MemberSharePageErrorCode.ALREADY_SHARE_PAGE_ACCEPT_EXCEPTION);
        }

        memberSharePage.updateAccept();
        memberSharePageRepository.save(memberSharePage);

        return new ApiResponseSpec(true, 200, "공유 페이지 초대를 수락하였습니다");
    }

}
