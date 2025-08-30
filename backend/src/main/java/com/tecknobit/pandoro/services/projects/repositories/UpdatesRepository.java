package com.tecknobit.pandoro.services.projects.repositories;

import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.AUTHOR_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code UpdatesRepository} interface is useful to manage the queries for the updates of a project
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see Update
 */
@Repository
public interface UpdatesRepository extends JpaRepository<Update, String> {

    /**
     * Method to execute the query to select a {@link Update} by its target project_version
     *
     * @param projectId The project identifier
     * @param targetVersion The target project_version of the update to fetch
     * @return the project update as {@link Update}
     */
    @Query(
            value = "SELECT * FROM " + UPDATES_KEY + " WHERE " + PROJECT_KEY + "=:" + PROJECT_KEY
                    + " AND " + UPDATE_TARGET_VERSION_KEY + "=:" + UPDATE_TARGET_VERSION_KEY,
            nativeQuery = true
    )
    Update getUpdateByVersion(
            @Param(PROJECT_KEY) String projectId,
            @Param(UPDATE_TARGET_VERSION_KEY) String targetVersion
    );

    /**
     * Method to execute the query to select a {@link Update} by its id
     *
     * @param projectId The project identifier
     * @param updateId The update identifier to fetch
     * @return the project update as {@link Update}
     */
    @Query(
            value = "SELECT * FROM " + UPDATES_KEY + " WHERE " + PROJECT_KEY + "=:" + PROJECT_KEY
                    + " AND " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    Update getUpdateById(
            @Param(PROJECT_KEY) String projectId,
            @Param(IDENTIFIER_KEY) String updateId
    );

    /**
     * Method to execute the query to start an existing {@link Update}
     *
     * @param updateId The update identifier
     * @param startDate The start date of the update
     * @param startedBy Who start the update
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
     * Method to execute the query to publish an existing {@link Update}
     *
     * @param updateId The update identifier
     * @param publishDate The publishing date of the update
     * @param publishedBy Who publish the update
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
     * Method to execute the query to delete an existing {@link Update}
     *
     * @param updateId The update identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + UPDATES_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteUpdate(
            @Param(IDENTIFIER_KEY) String updateId
    );

    /**
     * Method to execute the query to remove the constraints between {@link PandoroUser} deleted and {@link Update}
     *
     * @param userId The user identifier
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
    void removeUserConstraints(
            @Param(IDENTIFIER_KEY) String userId
    );

}
