package com.tecknobit.pandoro.services.users.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.equinoxcore.annotations.DTO;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.PROFILE_PIC_KEY;

@DTO
public record CandidateMember(String id, String name, String surname, String email,
                              @JsonGetter(PROFILE_PIC_KEY) String profilePic) {
}
