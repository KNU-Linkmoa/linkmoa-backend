package com.knu.linkmoa.domain.sharepage.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SharePageInviteDto {
    private List<String> emails;
}
