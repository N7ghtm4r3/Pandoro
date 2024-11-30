package com.tecknobit.pandoro.services.groups.repositories;

import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.users.entities.GroupMember;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code GroupsRepository} interface is useful to manage the queries for the projects_groups
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see Group
 */
@Repository
public interface GroupsRepository extends JpaRepository<Group, String> {

    /**
     * Method to execute the query to count the total projects_groups where the user is a {@link GroupMember}
     *
     * @param userId The user identifier
     * @return the total numbers of the projects_groups
     */
    @Query(
            value = "SELECT COUNT(*) FROM " + GROUPS_KEY + " AS projects_groups LEFT JOIN " + GROUP_MEMBERS_TABLE
                    + " ON projects_groups." + IDENTIFIER_KEY + " = group_members." + GROUP_MEMBER_KEY + " WHERE "
                    + GROUP_MEMBERS_TABLE + "." + IDENTIFIER_KEY + "=:" + AUTHOR_KEY + " AND "
                    + GROUP_MEMBERS_TABLE + "." + INVITATION_STATUS_KEY + " = " + "'JOINED'",
            nativeQuery = true
    )
    long getGroupsCount(
            @Param(AUTHOR_KEY) String userId
    );

    /**
     * Method to execute the query to select the list of a {@link Group}
     *
     * @param userId   The user identifier
     * @param pageable The parameters to paginate the query
     * @return the list of projects_groups as {@link List} of {@link Group}
     */
    @Query(
            value = "SELECT projects_groups.* FROM " + GROUPS_KEY + " AS projects_groups LEFT JOIN " + GROUP_MEMBERS_TABLE
                    + " ON projects_groups." + IDENTIFIER_KEY + " = group_members." + GROUP_MEMBER_KEY + " WHERE "
                    + GROUP_MEMBERS_TABLE + "." + IDENTIFIER_KEY + "=:" + AUTHOR_KEY + " AND "
                    + GROUP_MEMBERS_TABLE + "." + INVITATION_STATUS_KEY + " = " + "'JOINED'"
                    + " ORDER BY " + CREATION_DATE_KEY + " DESC ",
            nativeQuery = true
    )
    List<Group> getGroups(
            @Param(AUTHOR_KEY) String userId,
            Pageable pageable
    );

    /**
     * Method to execute the query to select the list of a {@link Group}
     *
     * @param userId The user identifier
     * @return the list of projects_groups as {@link List} of {@link Group}
     */
    @Query(
            value = "SELECT projects_groups.* FROM " + GROUPS_KEY + " AS projects_groups LEFT JOIN " + GROUP_MEMBERS_TABLE
                    + " ON projects_groups." + IDENTIFIER_KEY + " = group_members." + GROUP_MEMBER_KEY + " WHERE "
                    + GROUP_MEMBERS_TABLE + "." + IDENTIFIER_KEY + "=:" + AUTHOR_KEY + " AND "
                    + GROUP_MEMBERS_TABLE + "." + INVITATION_STATUS_KEY + " = " + "'JOINED'"
                    + " ORDER BY " + CREATION_DATE_KEY + " DESC ",
            nativeQuery = true
    )
    List<Group> getGroups(
            @Param(AUTHOR_KEY) String userId
    );

    /**
     * Method to execute the query to select a {@link Group} by its name
     *
     * @param userId The user identifier
     * @param name:   the name of the group to fetch
     * @return the group as {@link Group}
     */
    @Query(
            value = "SELECT * FROM " + GROUPS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + NAME_KEY + "=:" + NAME_KEY,
            nativeQuery = true
    )
    Group getGroupByName(
            @Param(AUTHOR_KEY) String userId,
            @Param(NAME_KEY) String name
    );

    /**
     * Method to execute the query to create a new {@link Group}
     *
     * @param author The author of the group
     * @param groupId The identifier of the new group
     * @param groupName The name of the group
     * @param creationDate The date when the group has been created
     * @param groupDescription The project_description of the group
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + GROUPS_KEY
                    + "( "
                    + IDENTIFIER_KEY + ","
                    + NAME_KEY + ","
                    + CREATION_DATE_KEY + ","
                    + GROUP_DESCRIPTION_KEY + ","
                    + AUTHOR_KEY + ") VALUES "
                    + "( "
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + NAME_KEY + ","
                    + ":" + CREATION_DATE_KEY + ","
                    + ":" + GROUP_DESCRIPTION_KEY + ","
                    + ":" + AUTHOR_KEY + ")",
            nativeQuery = true
    )
    void createGroup(
            @Param(AUTHOR_KEY) String author,
            @Param(IDENTIFIER_KEY) String groupId,
            @Param(NAME_KEY) String groupName,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(GROUP_DESCRIPTION_KEY) String groupDescription
    );

    /**
     * Method to execute the query to select a {@link Group} by its id
     *
     * @param userId The user identifier
     * @param groupId The group identifier
     * @return the group as {@link Group}
     */
    @Query(
            value = "SELECT projects_groups.* FROM " + GROUPS_KEY + " AS projects_groups LEFT JOIN " + GROUP_MEMBERS_TABLE
                    + " ON projects_groups." + IDENTIFIER_KEY + " = group_members." + GROUP_MEMBER_KEY + " WHERE "
                    + GROUP_MEMBERS_TABLE + "." + GROUP_MEMBER_KEY + " =:" + GROUP_IDENTIFIER_KEY
                    + " AND " + GROUP_MEMBERS_TABLE + "." + IDENTIFIER_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    Group getGroup(
            @Param(AUTHOR_KEY) String userId,
            @Param(GROUP_IDENTIFIER_KEY) String groupId
    );

    /**
     * Method to execute the query to select the list of a {@link Project}'s id of a group
     *
     * @param groupId The group from fetch the list
     * @return the list of projects ids of a group as {@link List} of {@link String}
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "SELECT " + PROJECT_IDENTIFIER_KEY + " FROM " + PROJECTS_GROUPS_TABLE + " WHERE "
                    + GROUP_IDENTIFIER_KEY + "=:" + GROUP_IDENTIFIER_KEY,
            nativeQuery = true
    )
    List<String> getGroupProjectsIds(@Param(GROUP_IDENTIFIER_KEY) String groupId);

    /**
     * Method to execute the query to add a project to a group
     *
     * @param projectId The project to add to a group
     * @param groupId The group where add the project
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + PROJECTS_GROUPS_TABLE + "("
                    + PROJECT_IDENTIFIER_KEY + ","
                    + GROUP_IDENTIFIER_KEY
                    + ") VALUES ("
                    + ":" + PROJECT_IDENTIFIER_KEY + ","
                    + ":" + GROUP_IDENTIFIER_KEY + ")",
            nativeQuery = true
    )
    void addGroupProject(
            @Param(PROJECT_IDENTIFIER_KEY) String projectId,
            @Param(GROUP_IDENTIFIER_KEY) String groupId
    );

    /**
     * Method to execute the query to remove a project from a group
     *
     * @param projectId The project to remove from a group
     * @param groupId The group where remove the project
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + PROJECTS_GROUPS_TABLE + " WHERE " + PROJECT_IDENTIFIER_KEY + "=:"
                    + PROJECT_IDENTIFIER_KEY + " AND " + GROUP_IDENTIFIER_KEY + "=:" + GROUP_IDENTIFIER_KEY,
            nativeQuery = true
    )
    void removeGroupProject(
            @Param(PROJECT_IDENTIFIER_KEY) String projectId,
            @Param(GROUP_IDENTIFIER_KEY) String groupId
    );

    /**
     * Method to execute the query to delete an existing {@link Group}
     *
     * @param groupId The group identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + GROUPS_KEY + " WHERE " + GROUPS_KEY + ".id=:" + GROUP_IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteGroup(@Param(GROUP_IDENTIFIER_KEY) String groupId);

}
