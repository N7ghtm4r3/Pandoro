package com.tecknobit.pandoro.services.projects.dto;

import com.tecknobit.equinoxcore.annotations.DTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@DTO
public record ProjectDTO(MultipartFile icon, String name, String project_description, String project_version,
                         List<String> groups, String project_repository) {

    public List<String> groups() {
        if (groups == null)
            return Collections.emptyList();
        return groups;
    }

}
