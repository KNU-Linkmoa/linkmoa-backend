package com.knu.linkmoa.domain.sharepage.controller;

import com.knu.linkmoa.domain.sharepage.dto.request.SharePageCreateDto;
import com.knu.linkmoa.domain.sharepage.dto.request.SharePageInviteDto;
import com.knu.linkmoa.domain.sharepage.dto.response.ApiSharePageResponse;
import com.knu.linkmoa.domain.sharepage.entity.SharePage;
import com.knu.linkmoa.domain.sharepage.service.SharePageService;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/share-pages")
@RequiredArgsConstructor
public class SharePageController {

    private final SharePageService sharePageService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSharePageResponse<Long>> createSharePage(
            @RequestBody SharePageCreateDto sharePageCreateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        ApiSharePageResponse<Long> response = sharePageService.createSharePage(sharePageCreateDto, principalDetails);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{pageId}/share")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseSpec> inviteSharePage(
            @RequestBody SharePageInviteDto sharePageInviteDto,
            @PathVariable("pageId") Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        ApiResponseSpec apiResponseSpec = sharePageService.inviteSharePage(sharePageInviteDto, id, principalDetails);

        return ResponseEntity.ok().body(apiResponseSpec);
    }

    @PostMapping("/{pageId}/accept")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseSpec> acceptSharePage(
        @PathVariable("pageId") Long id,
        @AuthenticationPrincipal PrincipalDetails principalDetails){

        ApiResponseSpec apiResponseSpec = sharePageService.acceptSharePage(id, principalDetails);

        return ResponseEntity.ok().body(apiResponseSpec);
    }
}
