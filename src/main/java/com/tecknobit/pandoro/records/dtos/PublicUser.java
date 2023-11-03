package com.tecknobit.pandoro.records.dtos;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;

import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.*;

@SqlResultSetMapping(
        name = "publicUserMapping",
        classes = {
                @ConstructorResult(
                        targetClass = PublicUser.class,
                        columns = {
                                @ColumnResult(name = IDENTIFIER_KEY, type = String.class),
                                @ColumnResult(name = NAME_KEY, type = String.class),
                                @ColumnResult(name = SURNAME_KEY, type = String.class),
                                @ColumnResult(name = EMAIL_KEY, type = String.class),
                                @ColumnResult(name = PROFILE_PIC_KEY, type = String.class)
                        }
                )
        }
)

public class PublicUser {


    private final String id;

    private final String name;

    private final String surname;

    private final String email;

    private final String profilePic;

    public PublicUser(String id, String name, String surname, String email, String profilePic) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.profilePic = profilePic;
    }

}
