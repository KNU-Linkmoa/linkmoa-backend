package com.knu.linkmoa.domain.directory.repository;

import com.knu.linkmoa.domain.directory.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectoryRepository extends JpaRepository<Directory,Long> {
}
