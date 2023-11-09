package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ProjectsRepository extends JpaRepository<Project, String> {
}
