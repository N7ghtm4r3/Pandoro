package com.tecknobit.pandoro.services.repositories.projects;

import com.tecknobit.pandorocore.records.Group;
import com.tecknobit.pandorocore.records.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;
import static com.tecknobit.pandorocore.records.Group.AUTHOR_KEY;
import static com.tecknobit.pandorocore.records.Group.CREATION_DATE_KEY;
import static com.tecknobit.pandorocore.records.Group.IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.records.Group.*;
import static com.tecknobit.pandorocore.records.Project.*;
import static com.tecknobit.pandorocore.records.users.User.GROUP_MEMBERS_TABLE;

/**
 * The {@code ProjectsRepository} interface is useful to manage the queries for the projects
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see Project
 */
@Service
@Repository
public interface ProjectsRepository extends JpaRepository<Project, String> {

    /**
     * Method to execute the query to select the list of a {@link Project}
     *
     * @param userId: the user identifier
     * @return the list of projects as {@link List} of {@link Project}
     * @apiNote also the projects of a group in which he is a member are returned
     */
    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " UNION SELECT " + PROJECTS_KEY + ".* FROM " + PROJECTS_KEY + " AS " + PROJECTS_KEY + " LEFT JOIN "
                    + PROJECTS_GROUPS_TABLE + " ON " + PROJECTS_KEY + "." + IDENTIFIER_KEY + " = "
                    + PROJECTS_GROUPS_TABLE + "." + PROJECT_IDENTIFIER_KEY + " LEFT JOIN "
                    + GROUPS_KEY + " ON " + PROJECTS_GROUPS_TABLE + "." + GROUP_IDENTIFIER_KEY + " = " + GROUPS_KEY + "."
                    + IDENTIFIER_KEY + " LEFT JOIN " + GROUP_MEMBERS_TABLE + " ON " + GROUPS_KEY + "." + IDENTIFIER_KEY
                    + " = " + GROUP_MEMBERS_TABLE + "." + GROUP_MEMBER_KEY + " WHERE " + GROUP_MEMBERS_TABLE + "."
                    + IDENTIFIER_KEY + " =:" + AUTHOR_KEY + " AND " + GROUP_MEMBERS_TABLE + "." + INVITATION_STATUS_KEY
                    + " = 'JOINED' AND " + GROUPS_KEY + "." + AUTHOR_KEY + " !=:" + AUTHOR_KEY
                    + " ORDER BY " + CREATION_DATE_KEY + " DESC ",
            nativeQuery = true
    )
    List<Project> getProjectsList(
            @Param(AUTHOR_KEY) String userId
    );

    /**
     * Method to execute the query to select a {@link Project} by its name
     *
     * @param userId: the user identifier
     * @param name:   the name of the project to fetch
     * @return the project as {@link Project}
     */
    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + NAME_KEY + "=:" + NAME_KEY,
            nativeQuery = true
    )
    Project getProjectByName(
            @Param(AUTHOR_KEY) String userId,
            @Param(NAME_KEY) String name
    );

    /**
     * Method to execute the query to select a {@link Project} by its id
     *
     * @param projectId: the identifier of the project to fetch
     * @return the project as {@link Project}
     */
    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    Project getProjectById(@Param(IDENTIFIER_KEY) String projectId);

    /**
     * Method to execute the query to add a new {@link Project}
     *
     * @param author: the author of the project
     * @param projectId: the project identifier
     * @param name: the name of the project
     * @param creationDate: the date when the project has been created
     * @param description: the description of the project
     * @param shortDescription: the short description of the project
     * @param version: the version of the project
     * @param repository: the GitHub or Gitlab repository url of the project
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + PROJECTS_KEY
                    + "( "
                    + AUTHOR_KEY + ","
                    + IDENTIFIER_KEY + ","
                    + NAME_KEY + ","
                    + CREATION_DATE_KEY + ","
                    + PROJECT_DESCRIPTION_KEY + ","
                    + PROJECT_SHORT_DESCRIPTION_KEY + ","
                    + PROJECT_VERSION_KEY + ","
                    + PROJECT_REPOSITORY_KEY + ") VALUES "
                    + "( "
                    + ":" + AUTHOR_KEY + ","
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + NAME_KEY + ","
                    + ":" + CREATION_DATE_KEY + ","
                    + ":" + PROJECT_DESCRIPTION_KEY + ","
                    + ":" + PROJECT_SHORT_DESCRIPTION_KEY + ","
                    + ":" + PROJECT_VERSION_KEY + ","
                    + ":" + PROJECT_REPOSITORY_KEY + ")",
            nativeQuery = true
    )
    void insertProject(
            @Param(AUTHOR_KEY) String author,
            @Param(IDENTIFIER_KEY) String projectId,
            @Param(NAME_KEY) String name,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(PROJECT_DESCRIPTION_KEY) String description,
            @Param(PROJECT_SHORT_DESCRIPTION_KEY) String shortDescription,
            @Param(PROJECT_VERSION_KEY) String version,
            @Param(PROJECT_REPOSITORY_KEY) String repository
    );

    /**
     * Method to execute the query to select the list of a {@link Group}'s id of a project
     *
     * @param projectId: the project from fetch the list
     * @return the list of group ids of a project as {@link List} of {@link String}
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "SELECT " + GROUP_IDENTIFIER_KEY + " FROM " + PROJECTS_GROUPS_TABLE + " WHERE "
                    + PROJECT_IDENTIFIER_KEY + "=:" + PROJECT_IDENTIFIER_KEY,
            nativeQuery = true
    )
    List<String> getProjectGroupsIds(@Param(PROJECT_IDENTIFIER_KEY) String projectId);

    /**
     * Method to execute the query to add a group to a project
     *
     * @param projectId: the project where add the group
     * @param groupId: the group to add to a project
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
    void addProjectGroup(
            @Param(PROJECT_IDENTIFIER_KEY) String projectId,
            @Param(GROUP_IDENTIFIER_KEY) String groupId
    );

    /**
     * Method to execute the query to remove a group from a project
     *
     * @param projectId: the project where remove the group
     * @param groupId: the group to remove from a project
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + PROJECTS_GROUPS_TABLE + " WHERE " + PROJECT_IDENTIFIER_KEY + "=:"
                    + PROJECT_IDENTIFIER_KEY + " AND " + GROUP_IDENTIFIER_KEY + "=:" + GROUP_IDENTIFIER_KEY,
            nativeQuery = true
    )
    void removeProjectGroup(
            @Param(PROJECT_IDENTIFIER_KEY) String projectId,
            @Param(GROUP_IDENTIFIER_KEY) String groupId
    );

    /**
     * Method to execute the query to edit an existing {@link Project}
     *
     * @param author: the author of the project
     * @param projectId: the project identifier
     * @param name: the name of the project
     * @param description: the description of the project
     * @param shortDescription: the short description of the project
     * @param version: the version of the project
     * @param repository: the GitHub or Gitlab repository url of the project
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + PROJECTS_KEY + " SET "
                    + NAME_KEY + "=:" + NAME_KEY + ","
                    + PROJECT_DESCRIPTION_KEY + "=:" + PROJECT_DESCRIPTION_KEY + ","
                    + PROJECT_SHORT_DESCRIPTION_KEY + "=:" + PROJECT_SHORT_DESCRIPTION_KEY + ","
                    + PROJECT_VERSION_KEY + "=:" + PROJECT_VERSION_KEY + ","
                    + PROJECT_REPOSITORY_KEY + "=:" + PROJECT_REPOSITORY_KEY
                    + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY + " AND "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void editProject(
            @Param(AUTHOR_KEY) String author,
            @Param(IDENTIFIER_KEY) String projectId,
            @Param(NAME_KEY) String name,
            @Param(PROJECT_DESCRIPTION_KEY) String description,
            @Param(PROJECT_SHORT_DESCRIPTION_KEY) String shortDescription,
            @Param(PROJECT_VERSION_KEY) String version,
            @Param(PROJECT_REPOSITORY_KEY) String repository
    );

    /**
     * Method to execute the query to update to the last published update version
     * the version of the {@link Project}
     *
     * @param author:    the author of the project
     * @param projectId: the project identifier
     * @param version:   the last version of the project
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + PROJECTS_KEY + " SET "
                    + PROJECT_VERSION_KEY + "=:" + PROJECT_VERSION_KEY
                    + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY + " AND "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void updateProjectVersion(
            @Param(AUTHOR_KEY) String author,
            @Param(IDENTIFIER_KEY) String projectId,
            @Param(PROJECT_VERSION_KEY) String version
    );

    /**
     * Method to execute the query to select an existing {@link Project}
     *
     * @param userId: the user identifier
     * @param projectId: the project identifier
     * @return the project as {@link Project}
     * @apiNote also the projects of a group in which he is a member is returned
     */
    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY + " AND "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY + " UNION SELECT " + PROJECTS_KEY + ".* FROM " + PROJECTS_KEY
                    + " AS " + PROJECTS_KEY + " LEFT JOIN " + PROJECTS_GROUPS_TABLE + " ON " + PROJECTS_KEY + "."
                    + IDENTIFIER_KEY + " =:" + IDENTIFIER_KEY + " LEFT JOIN " + GROUPS_KEY + " ON "
                    + PROJECTS_GROUPS_TABLE + "." + GROUP_IDENTIFIER_KEY + " = " + GROUPS_KEY + "." + IDENTIFIER_KEY
                    + " LEFT JOIN " + GROUP_MEMBERS_TABLE + " ON " + GROUPS_KEY + "." + IDENTIFIER_KEY + " = "
                    + GROUP_MEMBERS_TABLE + "." + GROUP_MEMBER_KEY + " WHERE " + GROUP_MEMBERS_TABLE + "."
                    + IDENTIFIER_KEY + " =:" + AUTHOR_KEY + " AND " + GROUP_MEMBERS_TABLE + "." + INVITATION_STATUS_KEY
                    + " = 'JOINED' AND " + GROUPS_KEY + "." + AUTHOR_KEY + " !=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    Project getProject(
            @Param(AUTHOR_KEY) String userId,
            @Param(IDENTIFIER_KEY) String projectId
    );

    /**
     * Method to execute the query to delete an existing {@link Project}
     *
     * @param userId: the user identifier
     * @param projectId: the project identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteProject(
            @Param(AUTHOR_KEY) String userId,
            @Param(IDENTIFIER_KEY) String projectId
    );

    /**
     * Method to execute the query to delete the current list of projects of an user
     *
     * @param userId: the user identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE pg FROM " + PROJECTS_GROUPS_TABLE + " pg LEFT JOIN " + PROJECTS_KEY
                    + " ON " + PROJECTS_KEY + "." + IDENTIFIER_KEY + "=" + "pg." + PROJECT_IDENTIFIER_KEY + " WHERE "
                    + PROJECTS_KEY + "." + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    void deleteProjects(@Param(AUTHOR_KEY) String userId);

}
