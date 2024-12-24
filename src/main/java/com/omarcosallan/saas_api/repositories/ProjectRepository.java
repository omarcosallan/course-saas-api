package com.omarcosallan.saas_api.repositories;

import com.omarcosallan.saas_api.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
