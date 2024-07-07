package com.knu.linkmoa.domain.sharepage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharePage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_page_id")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "sharePage")
    List<MemberSharePage> memberSharePages = new ArrayList<>();

    @Builder
    public SharePage(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
