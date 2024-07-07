package com.knu.linkmoa.domain.directory.service;


import com.knu.linkmoa.domain.directory.dto.request.DirectorySaveReqeuest;
import com.knu.linkmoa.domain.directory.entity.Directory;
import com.knu.linkmoa.domain.directory.repository.DirectoryRepository;
import com.knu.linkmoa.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;

}
