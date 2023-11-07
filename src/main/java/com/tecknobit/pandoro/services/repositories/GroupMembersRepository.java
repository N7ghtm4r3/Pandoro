package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.users.GroupMember;
import com.tecknobit.pandoro.records.users.GroupMember.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.MEMBER_ROLE_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.*;

@Service
public interface GroupMembersRepository extends JpaRepository<GroupMember, String> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + GROUP_MEMBERS_TABLE
                    + "( "
                    + IDENTIFIER_KEY + ","
                    + NAME_KEY + ","
                    + EMAIL_KEY + ","
                    + PROFILE_PIC_KEY + ","
                    + SURNAME_KEY + ","
                    + MEMBER_ROLE_KEY + ","
                    + GROUP_KEY + ") VALUES "
                    + "( "
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + NAME_KEY + ","
                    + ":" + EMAIL_KEY + ","
                    + ":" + PROFILE_PIC_KEY + ","
                    + ":" + SURNAME_KEY + ","
                    + ":#{#" + MEMBER_ROLE_KEY + ".name()},"
                    + ":" + GROUP_KEY + ")",
            nativeQuery = true
    )
    void insertMember(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(NAME_KEY) String name,
            @Param(EMAIL_KEY) String email,
            @Param(PROFILE_PIC_KEY) String profilePic,
            @Param(SURNAME_KEY) String surname,
            @Param(MEMBER_ROLE_KEY) Role role,
            @Param(GROUP_KEY) String groupId
    );

    /*/@Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + GROUP_KEY + "=:" + GROUP_KEY,
            nativeQuery = true
    )
    GroupMember getGroupMember(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(GROUP_KEY) String groupId
    );*/

}
