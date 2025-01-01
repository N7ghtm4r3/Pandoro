package com.tecknobit.pandoro.services.groups.dto;

import com.tecknobit.equinoxcore.annotations.DTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DTO
public record GroupDTO(String groupId, String name, MultipartFile logo, String group_description, List<String> members,
                       ArrayList<String> projects) {

    @Override
    public List<String> members() {
        if (members == null)
            return Collections.emptyList();
        return members;
    }

    @Override
    public ArrayList<String> projects() {
        if (projects == null)
            return new ArrayList<>();
        return projects;
    }

}
