package com.tecknobit.pandoro.services.repositories.projects;

import com.tecknobit.pandoro.records.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_MEMBER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.GROUP_MEMBERS_TABLE;
import static com.tecknobit.pandoro.services.UsersHelper.NAME_KEY;

@Service
@Repository
public interface ProjectsRepository extends JpaRepository<Project, String> {

    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " UNION SELECT " + PROJECTS_KEY + ".* FROM " + PROJECTS_KEY + " AS " + PROJECTS_KEY + " LEFT JOIN "
                    + PROJECTS_GROUPS_TABLE + " ON " + PROJECTS_KEY + "." + IDENTIFIER_KEY + " = "
                    + PROJECTS_GROUPS_TABLE + "." + PROJECT_IDENTIFIER_KEY + " LEFT JOIN "
                    + GROUPS_KEY + " ON " + PROJECTS_GROUPS_TABLE + "." + GROUP_IDENTIFIER_KEY + " = " + GROUPS_KEY + "."
                    + IDENTIFIER_KEY + " LEFT JOIN " + GROUP_MEMBERS_TABLE + " ON " + GROUPS_KEY + "." + IDENTIFIER_KEY
                    + " = " + GROUP_MEMBERS_TABLE + "." + GROUP_MEMBER_KEY + " WHERE " + GROUPS_KEY + "." + IDENTIFIER_KEY
                    + " =:" + AUTHOR_KEY + " AND " + GROUPS_KEY + "." + AUTHOR_KEY + " !=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    List<Project> getProjectsList(@Param(AUTHOR_KEY) String userId);

    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + NAME_KEY + "=:" + NAME_KEY,
            nativeQuery = true
    )
    Project getProjectByName(
            @Param(AUTHOR_KEY) String userId,
            @Param(NAME_KEY) String name
    );

    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    Project getProjectById(
            @Param(AUTHOR_KEY) String userId,
            @Param(IDENTIFIER_KEY) String projectId
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + PROJECTS_KEY
                    + "( "
                    + AUTHOR_KEY + ","
                    + IDENTIFIER_KEY + ","
                    + NAME_KEY + ","
                    + PROJECT_DESCRIPTION_KEY + ","
                    + PROJECT_SHORT_DESCRIPTION_KEY + ","
                    + PROJECT_VERSION_KEY + ","
                    + PROJECT_REPOSITORY_KEY + ") VALUES "
                    + "( "
                    + ":" + AUTHOR_KEY + ","
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + NAME_KEY + ","
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
            @Param(PROJECT_DESCRIPTION_KEY) String description,
            @Param(PROJECT_SHORT_DESCRIPTION_KEY) String shortDescription,
            @Param(PROJECT_VERSION_KEY) String version,
            @Param(PROJECT_REPOSITORY_KEY) String repository
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "SELECT " + GROUP_IDENTIFIER_KEY + " FROM " + PROJECTS_GROUPS_TABLE + " WHERE "
                    + PROJECT_IDENTIFIER_KEY + "=:" + PROJECT_IDENTIFIER_KEY,
            nativeQuery = true
    )
    List<String> getProjectGroupsIds(@Param(PROJECT_IDENTIFIER_KEY) String projectId);

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

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + PROJECTS_GROUPS_TABLE + " WHERE " + PROJECT_IDENTIFIER_KEY + "=:"
                    + PROJECT_IDENTIFIER_KEY + " AND " + GROUP_IDENTIFIER_KEY + "=:" + GROUP_IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteProjectGroup(
            @Param(PROJECT_IDENTIFIER_KEY) String projectId,
            @Param(GROUP_IDENTIFIER_KEY) String groupId
    );

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

    @Query(
            value = "SELECT * FROM " + PROJECTS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY + " AND "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY + " UNION SELECT " + PROJECTS_KEY + ".* FROM " + PROJECTS_KEY
                    + " AS " + PROJECTS_KEY + " LEFT JOIN " + PROJECTS_GROUPS_TABLE + " ON " + PROJECTS_KEY + "."
                    + IDENTIFIER_KEY + " =:" + IDENTIFIER_KEY + " LEFT JOIN "
                    + GROUPS_KEY + " ON " + PROJECTS_GROUPS_TABLE + "." + GROUP_IDENTIFIER_KEY + " = " + GROUPS_KEY + "."
                    + IDENTIFIER_KEY + " LEFT JOIN " + GROUP_MEMBERS_TABLE + " ON " + GROUPS_KEY + "." + IDENTIFIER_KEY
                    + " = " + GROUP_MEMBERS_TABLE + "." + GROUP_MEMBER_KEY + " WHERE " + GROUPS_KEY + "." + IDENTIFIER_KEY
                    + " =:" + AUTHOR_KEY + " AND " + GROUPS_KEY + "." + AUTHOR_KEY + " !=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    Project getProject(
            @Param(AUTHOR_KEY) String userId,
            @Param(IDENTIFIER_KEY) String projectId
    );

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

}
