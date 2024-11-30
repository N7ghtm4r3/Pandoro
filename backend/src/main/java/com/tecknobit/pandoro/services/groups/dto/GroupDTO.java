package com.tecknobit.pandoro.services.groups.dto;

import com.tecknobit.equinoxcore.annotations.DTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@DTO
public record GroupDTO(String name, MultipartFile logo, String group_description, List<String> members) {
}
