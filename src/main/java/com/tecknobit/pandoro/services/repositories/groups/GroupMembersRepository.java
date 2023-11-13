package com.tecknobit.pandoro.services.repositories.groups;

import com.tecknobit.pandoro.records.users.GroupMember;
import com.tecknobit.pandoro.records.users.GroupMember.InvitationStatus;
import com.tecknobit.pandoro.records.users.GroupMember.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.*;

@Service
@Repository
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
                    + INVITATION_STATUS_KEY + ","
                    + GROUP_MEMBER_KEY + ") VALUES "
                    + "( "
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + NAME_KEY + ","
                    + ":" + EMAIL_KEY + ","
                    + ":" + PROFILE_PIC_KEY + ","
                    + ":" + SURNAME_KEY + ","
                    + ":#{#" + MEMBER_ROLE_KEY + ".name()},"
                    + ":#{#" + INVITATION_STATUS_KEY + ".name()},"
                    + ":" + GROUP_MEMBER_KEY + ")",
            nativeQuery = true
    )
    void insertMember(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(NAME_KEY) String name,
            @Param(EMAIL_KEY) String email,
            @Param(PROFILE_PIC_KEY) String profilePic,
            @Param(SURNAME_KEY) String surname,
            @Param(MEMBER_ROLE_KEY) Role role,
            @Param(INVITATION_STATUS_KEY) InvitationStatus invitationStatus,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY
                    + " AND " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    GroupMember getGroupMemberByEmail(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(GROUP_MEMBER_KEY) String groupId,
            @Param(EMAIL_KEY) String email
    );

    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY,
            nativeQuery = true
    )
    GroupMember getGroupMember(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + GROUP_MEMBERS_TABLE + " SET " + INVITATION_STATUS_KEY + "=" + "'JOINED'"
                    + " WHERE " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void acceptGroupInvitation(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + GROUP_MEMBERS_TABLE + " SET " + MEMBER_ROLE_KEY + "="
                    + ":#{#" + MEMBER_ROLE_KEY + ".name()}"
                    + " WHERE " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void changeMemberRole(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(GROUP_MEMBER_KEY) String groupId,
            @Param(MEMBER_ROLE_KEY) Role role
    );

    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + MEMBER_ROLE_KEY + "= 'ADMIN'"
                    + " AND " + IDENTIFIER_KEY + "!=:" + IDENTIFIER_KEY + " AND " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY,
            nativeQuery = true
    )
    List<GroupMember> getGroupAdmins(
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY
                    + " AND " + INVITATION_STATUS_KEY + " = 'JOINED'",
            nativeQuery = true
    )
    List<GroupMember> getGroupMembers(@Param(GROUP_MEMBER_KEY) String groupId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + GROUP_MEMBERS_TABLE + " WHERE " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void leaveGroup(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

}
