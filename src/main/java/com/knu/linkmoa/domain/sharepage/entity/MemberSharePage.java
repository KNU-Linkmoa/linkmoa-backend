package com.knu.linkmoa.domain.sharepage.entity;

import com.knu.linkmoa.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSharePage {

    // Member <-> SharePage 사이의 중간 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_share_page_id")
    private Long id;

    private boolean accept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_page_id")
    private SharePage sharePage;

    @Builder
    public MemberSharePage(Long id, boolean accept, Member member, SharePage sharePage) {
        this.id = id;
        this.accept = accept;
        this.member = member;
        this.sharePage = sharePage;
        sharePage.getMemberSharePages().add(this);
        member.getMemberSharePages().add(this);
    }

    public void updateAccept(){
        this.accept = true;
    }
}
