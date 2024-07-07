package com.knu.linkmoa.domain.sharepage.repository;

import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.sharepage.entity.MemberSharePage;
import com.knu.linkmoa.domain.sharepage.entity.SharePage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSharePageRepository extends JpaRepository<MemberSharePage, Long> {
    Optional<MemberSharePage> findByMemberAndSharePage(Member member, SharePage sharePage);
    boolean existsByMemberAndSharePage(Member member, SharePage sharePage);
}


