package com.knu.linkmoa.domain.sharepage.controller;

import com.knu.linkmoa.domain.sharepage.dto.request.SharePageCreateDto;
import com.knu.linkmoa.domain.sharepage.dto.response.ApiSharePageResponse;
import com.knu.linkmoa.domain.sharepage.entity.SharePage;
import com.knu.linkmoa.domain.sharepage.service.SharePageService;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponseSpec> createSharePage(
            @RequestBody SharePageCreateDto sharePageCreateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails){
        ApiResponseSpec responseSpec = sharePageService.createSharePage(sharePageCreateDto, principalDetails);

        return ResponseEntity.ok().body(responseSpec);
    }

}
