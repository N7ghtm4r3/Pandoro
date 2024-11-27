package com.tecknobit.pandoro.services.projects.repositories;

import com.tecknobit.pandoro.services.projects.models.ProjectUpdate;
import com.tecknobit.pandoro.services.projects.models.ProjectUpdate.Status;
import com.tecknobit.pandoro.services.users.models.PandoroUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code UpdatesRepository} interface is useful to manage the queries for the updates of a project
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see ProjectUpdate
 */
@Service
@Repository
public interface UpdatesRepository extends JpaRepository<ProjectUpdate, String> {

    /**
     * Method to execute the query to select a {@link ProjectUpdate} by its target version
     *
     * @param projectId:     the project identifier
     * @param targetVersion: the target version of the update to fetch
     * @return the project update as {@link ProjectUpdate}
     */
    @Query(
            value = "SELECT * FROM " + UPDATES_KEY + " WHERE " + PROJECT_KEY + "=:" + PROJECT_KEY
                    + " AND " + UPDATE_TARGET_VERSION_KEY + "=:" + UPDATE_TARGET_VERSION_KEY,
            nativeQuery = true
    )
    ProjectUpdate getUpdateByVersion(
            @Param(PROJECT_KEY) String projectId,
            @Param(UPDATE_TARGET_VERSION_KEY) String targetVersion
    );

    /**
     * Method to execute the query to select a {@link ProjectUpdate} by its id
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier to fetch
     * @return the project update as {@link ProjectUpdate}
     */
    @Query(
            value = "SELECT * FROM " + UPDATES_KEY + " WHERE " + PROJECT_KEY + "=:" + PROJECT_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    ProjectUpdate getUpdateById(
            @Param(PROJECT_KEY) String projectId,
            @Param(IDENTIFIER_KEY) String updateId
    );

    /**
     * Method to execute the query to schedule a new {@link ProjectUpdate}
     *
     * @param updateId: the update identifier
     * @param targetVersion: the target version of the new update
     * @param createDate: the creation date of the update
     * @param updateStatus: the {@link Status#SCHEDULED} status
     * @param projectId: the project identifier
     * @param author: the author of the update
     */
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

    /**
     * Method to execute the query to start an existing {@link ProjectUpdate}
     *
     * @param updateId: the update identifier
     * @param startDate: the start date of the update
     * @param startedBy: who start the update
     */
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

    /**
     * Method to execute the query to publish an existing {@link com.tecknobit.pandoro.services.projects.models.ProjectUpdate}
     *
     * @param updateId: the update identifier
     * @param publishDate: the publishing date of the update
     * @param publishedBy: who publish the update
     */
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

    /**
     * Method to execute the query to delete an existing {@link ProjectUpdate}
     *
     * @param updateId: the update identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + UPDATES_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteUpdate(@Param(IDENTIFIER_KEY) String updateId);

    /**
     * Method to execute the query to remove the constraints between {@link PandoroUser} deleted and {@link ProjectUpdate}
     *
     * @param userId: the user identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + UPDATES_KEY + " SET "
                    + AUTHOR_KEY + "=NULL,"
                    + UPDATE_STARTED_BY_KEY + "=NULL,"
                    + UPDATE_PUBLISHED_BY_KEY + "=NULL"
                    + " WHERE " + AUTHOR_KEY + "=:" + IDENTIFIER_KEY
                    + " OR " + UPDATE_STARTED_BY_KEY + "=:" + IDENTIFIER_KEY
                    + " OR " + UPDATE_PUBLISHED_BY_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void removeUserConstraints(@Param(IDENTIFIER_KEY) String userId);

}
