package com.tecknobit.pandoro.services.repositories.projects;

import com.tecknobit.pandoro.records.ProjectUpdate;
import com.tecknobit.pandoro.records.ProjectUpdate.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.*;

@Service
@Repository
public interface UpdatesRepository extends JpaRepository<ProjectUpdate, String> {

    @Query(
            value = "SELECT * FROM " + UPDATES_KEY + " WHERE " + PROJECT_KEY + "=:" + PROJECT_KEY
                    + " AND " + UPDATE_TARGET_VERSION_KEY + "=:" + UPDATE_TARGET_VERSION_KEY,
            nativeQuery = true
    )
    ProjectUpdate getUpdateByVersion(
            @Param(PROJECT_KEY) String projectId,
            @Param(UPDATE_TARGET_VERSION_KEY) String targetVersion
    );

    @Query(
            value = "SELECT * FROM " + UPDATES_KEY + " WHERE " + PROJECT_KEY + "=:" + PROJECT_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    ProjectUpdate getUpdateById(
            @Param(PROJECT_KEY) String projectId,
            @Param(IDENTIFIER_KEY) String updateId
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + UPDATES_KEY
                    + "( "
                    + IDENTIFIER_KEY + ","
                    + UPDATE_TARGET_VERSION_KEY + ","
                    + UPDATE_CREATE_DATE_KEY + ","
                    + UPDATE_PUBLISH_DATE_KEY + ","
                    + UPDATE_START_DATE_KEY + ","
                    + UPDATE_STATUS_KEY + ","
                    + PROJECT_KEY + ","
                    + AUTHOR_KEY + ","
                    + UPDATE_PUBLISHED_BY_KEY + ","
                    + UPDATE_STARTED_BY_KEY + ") VALUES "
                    + "( "
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + UPDATE_TARGET_VERSION_KEY + ","
                    + ":" + UPDATE_CREATE_DATE_KEY + ","
                    + "-1,"
                    + "-1,"
                    + ":#{#" + UPDATE_STATUS_KEY + ".name()},"
                    + ":" + PROJECT_KEY + ","
                    + ":" + AUTHOR_KEY + ","
                    + "NULL,"
                    + "NULL)",
            nativeQuery = true
    )
    void scheduleUpdate(
            @Param(IDENTIFIER_KEY) String updateId,
            @Param(UPDATE_TARGET_VERSION_KEY) String targetVersion,
            @Param(UPDATE_CREATE_DATE_KEY) long createDate,
            @Param(UPDATE_STATUS_KEY) Status updateStatus,
            @Param(PROJECT_KEY) String projectId,
            @Param(AUTHOR_KEY) String author
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + UPDATES_KEY + " SET "
                    + UPDATE_START_DATE_KEY + "=:" + UPDATE_START_DATE_KEY + ","
                    + UPDATE_STARTED_BY_KEY + "=:" + UPDATE_STARTED_BY_KEY + ","
                    + UPDATE_STATUS_KEY + "= 'IN_DEVELOPMENT'"
                    + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void startUpdate(
            @Param(IDENTIFIER_KEY) String updateId,
            @Param(UPDATE_START_DATE_KEY) long startDate,
            @Param(UPDATE_STARTED_BY_KEY) String startedBy
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + UPDATES_KEY + " SET "
                    + UPDATE_PUBLISH_DATE_KEY + "=:" + UPDATE_PUBLISH_DATE_KEY + ","
                    + UPDATE_PUBLISHED_BY_KEY + "=:" + UPDATE_PUBLISHED_BY_KEY + ","
                    + UPDATE_STATUS_KEY + "= 'PUBLISHED'"
                    + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void publishUpdate(
            @Param(IDENTIFIER_KEY) String updateId,
            @Param(UPDATE_PUBLISH_DATE_KEY) long publishDate,
            @Param(UPDATE_PUBLISHED_BY_KEY) String publishedBy
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + UPDATES_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteUpdate(@Param(IDENTIFIER_KEY) String updateId);

}
