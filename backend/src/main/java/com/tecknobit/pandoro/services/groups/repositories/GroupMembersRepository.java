package com.tecknobit.pandoro.services.groups.repositories;

import com.tecknobit.pandoro.services.users.models.GroupMember;
import com.tecknobit.pandorocore.enums.InvitationStatus;
import com.tecknobit.pandorocore.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code GroupMembersRepository} interface is useful to manage the queries for the members of the groups
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see GroupMember
 */
@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMember, String> {

    /**
     * Method to execute the query to add a member in a {@link com.tecknobit.pandoro.services.groups.model.Group}
     *
     * @param memberId:         the identifier of the member to add
     * @param name:             the name of the member
     * @param email:            the email of the member
     * @param profilePic:       the profile pic of the member
     * @param surname:          the surname of the member
     * @param role:             the role of the member
     * @param invitationStatus: the invitation status of the member
     * @param groupId:          the identifier of the group where add the member
     */
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
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(NAME_KEY) String name,
            @Param(EMAIL_KEY) String email,
            @Param(PROFILE_PIC_KEY) String profilePic,
            @Param(SURNAME_KEY) String surname,
            @Param(MEMBER_ROLE_KEY) Role role,
            @Param(INVITATION_STATUS_KEY) InvitationStatus invitationStatus,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    /**
     * Method to execute the query to change the user's profile pic
     *
     * @param userId:     the user identifier
     * @param profilePic: the profile pic chosen by the user to set as the new profile pic
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + GROUP_MEMBERS_TABLE + " SET " + PROFILE_PIC_KEY + "=:" + PROFILE_PIC_KEY + " WHERE "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void changeProfilePic(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(PROFILE_PIC_KEY) String profilePic
    );

    /**
     * Method to execute the query to change the user's email
     *
     * @param userId: the user identifier
     * @param email:  the new user email to set
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + GROUP_MEMBERS_TABLE + " SET " + EMAIL_KEY + "=:" + EMAIL_KEY + " WHERE "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void changeEmail(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(EMAIL_KEY) String email
    );

    /**
     * Method to execute the query to select a {@link GroupMember} by its email
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     * @param email: the email of the member
     * @return the group member as {@link GroupMember}
     */
    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY
                    + " AND " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY + " AND " + IDENTIFIER_KEY + "=:"
                    + IDENTIFIER_KEY,
            nativeQuery = true
    )
    GroupMember getGroupMemberByEmail(
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(GROUP_MEMBER_KEY) String groupId,
            @Param(EMAIL_KEY) String email
    );

    /**
     * Method to execute the query to select a {@link GroupMember} by its id
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     * @return the group member as {@link GroupMember}
     */
    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY,
            nativeQuery = true
    )
    GroupMember getGroupMember(
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    /**
     * Method to execute the query to accept a group invitation
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + GROUP_MEMBERS_TABLE + " SET " + INVITATION_STATUS_KEY + "=" + "'JOINED'"
                    + " WHERE " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void acceptGroupInvitation(
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    /**
     * Method to execute the query to change the role of a group member
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     * @param role: the role of the member
     */
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
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(GROUP_MEMBER_KEY) String groupId,
            @Param(MEMBER_ROLE_KEY) Role role
    );

    /**
     * Method to execute the query to select the admins of a group
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     *
     * @return the list of the admin of a group as {@link List} of {@link GroupMember}
     */
    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + MEMBER_ROLE_KEY + "= 'ADMIN'"
                    + " AND " + IDENTIFIER_KEY + "!=:" + IDENTIFIER_KEY + " AND " + GROUP_MEMBER_KEY + "=:"
                    + GROUP_MEMBER_KEY,
            nativeQuery = true
    )
    List<GroupMember> getGroupAdmins(
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    /**
     * Method to execute the query to select the members of a group
     *
     * @param groupId: the group identifier
     *
     * @return the list the of members of a group as {@link List} of {@link GroupMember}
     */
    @Query(
            value = "SELECT * FROM " + GROUP_MEMBERS_TABLE + " WHERE " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY
                    + " AND " + INVITATION_STATUS_KEY + " = 'JOINED'",
            nativeQuery = true
    )
    List<GroupMember> getGroupMembers(@Param(GROUP_MEMBER_KEY) String groupId);

    /**
     * Method to execute the query to leave from a group
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + GROUP_MEMBERS_TABLE + " WHERE " + GROUP_MEMBER_KEY + "=:" + GROUP_MEMBER_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void leaveGroup(
            @Param(IDENTIFIER_KEY) String memberId,
            @Param(GROUP_MEMBER_KEY) String groupId
    );

    /**
     * Method to execute the query to delete the user's account
     *
     * @param memberId: the user identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + GROUP_MEMBERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteMember(@Param(IDENTIFIER_KEY) String memberId);

}
