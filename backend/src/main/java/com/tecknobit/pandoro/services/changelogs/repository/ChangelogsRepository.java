package com.tecknobit.pandoro.services.changelogs.repository;

import com.tecknobit.pandoro.services.changelogs.entity.Changelog;
import com.tecknobit.pandorocore.enums.ChangelogEvent;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code ChangelogsRepository} interface is useful to manage the queries for the changelogs
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see Changelog
 */
@Repository
public interface ChangelogsRepository extends JpaRepository<Changelog, String> {

    /**
     * Method to execute the query to count the {@link Changelog} yet to read
     *
     * @param owner The owner of the changelogs
     * @return the count of changelogs yet to read
     */
    @Query(
            value = "SELECT COUNT(*) FROM " + CHANGELOGS_KEY +
                    " WHERE " + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY +
                    " AND " + CHANGELOG_READ_KEY + "=" + false,
            nativeQuery = true
    )
    long getUnreadChangelogsCount(
            @Param(CHANGELOG_OWNER_KEY) String owner
    );

    /**
     * Method to execute the query to get the total number of the changelogs owned by the user
     *
     * @param owner The owner of the changelogs
     * @return the total number of the changelogs owned by the user
     */
    @Query(
            value = "SELECT COUNT(*) FROM " + CHANGELOGS_KEY +
                    " WHERE " + CHANGELOG_OWNER_KEY +
                    "=:" + CHANGELOG_OWNER_KEY,
            nativeQuery = true
    )
    long getChangelogsCount(
            @Param(CHANGELOG_OWNER_KEY) String owner
    );

    /**
     * Method to execute the query to select the list of a {@link Changelog}
     *
     * @param owner The owner of the changelogs
     * @param pageable  The parameters to paginate the query
     *
     * @return the list of changelogs as {@link List} of {@link Changelog}
     */
    @Query(
            value = "SELECT * FROM " + CHANGELOGS_KEY + " WHERE " + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY
                    + " ORDER BY " + CHANGELOG_TIMESTAMP_KEY + " DESC ",
            nativeQuery = true
    )
    List<Changelog> getChangelogs(
            @Param(CHANGELOG_OWNER_KEY) String owner,
            Pageable pageable
    );

    /**
     * Method to execute the query to select the a {@link Changelog}
     *
     * @param changelogId The identifier of the changelog
     * @param owner The owner of the changelogs
     * @return the changelog as {@link Changelog}
     */
    @Query(
            value = "SELECT * FROM " + CHANGELOGS_KEY + " WHERE " + IDENTIFIER_KEY + "=:"
                    + IDENTIFIER_KEY + " AND " + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY,
            nativeQuery = true
    )
    Changelog getChangelog(
            @Param(IDENTIFIER_KEY) String changelogId,
            @Param(CHANGELOG_OWNER_KEY) String owner
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(

            value = "INSERT INTO " + CHANGELOGS_KEY
                    + "( "
                    + IDENTIFIER_KEY + ","
                    + CHANGELOG_EVENT_KEY + ","
                    + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + CHANGELOG_READ_KEY + ","
                    + CHANGELOG_TIMESTAMP_KEY + ","
                    + PROJECT_IDENTIFIER_KEY + ","
                    + CHANGELOG_OWNER_KEY + ") VALUES "
                    + "( "
                    + ":" + IDENTIFIER_KEY + ","
                    + ":#{#" + CHANGELOG_EVENT_KEY + ".name()},"
                    + ":" + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + "false,"
                    + ":" + CHANGELOG_TIMESTAMP_KEY + ","
                    + ":" + PROJECT_IDENTIFIER_KEY + ","
                    + ":" + CHANGELOG_OWNER_KEY + ")",
            nativeQuery = true
    )
    void addProjectChangelog(
            @Param(IDENTIFIER_KEY) String changelogId,
            @Param(CHANGELOG_EVENT_KEY) ChangelogEvent changelogEvent,
            @Param(CHANGELOG_EXTRA_CONTENT_KEY) String extraContent,
            @Param(CHANGELOG_TIMESTAMP_KEY) long changelogTimestamp,
            @Param(PROJECT_IDENTIFIER_KEY) String projectId,
            @Param(CHANGELOG_OWNER_KEY) String owner
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(

            value = "INSERT INTO " + CHANGELOGS_KEY
                    + "( "
                    + IDENTIFIER_KEY + ","
                    + CHANGELOG_EVENT_KEY + ","
                    + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + CHANGELOG_READ_KEY + ","
                    + CHANGELOG_TIMESTAMP_KEY + ","
                    + GROUP_IDENTIFIER_KEY + ","
                    + CHANGELOG_OWNER_KEY + ") VALUES "
                    + "( "
                    + ":" + IDENTIFIER_KEY + ","
                    + ":#{#" + CHANGELOG_EVENT_KEY + ".name()},"
                    + ":" + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + "false,"
                    + ":" + CHANGELOG_TIMESTAMP_KEY + ","
                    + ":" + GROUP_IDENTIFIER_KEY + ","
                    + ":" + CHANGELOG_OWNER_KEY + ")",
            nativeQuery = true
    )
    void addGroupChangelog(
            @Param(IDENTIFIER_KEY) String changelogId,
            @Param(CHANGELOG_EVENT_KEY) ChangelogEvent changelogEvent,
            @Param(CHANGELOG_EXTRA_CONTENT_KEY) String extraContent,
            @Param(CHANGELOG_TIMESTAMP_KEY) long changelogTimestamp,
            @Param(GROUP_IDENTIFIER_KEY) String groupId,
            @Param(CHANGELOG_OWNER_KEY) String owner
    );

    /**
     * Method to execute the query to mark as read a {@link Changelog}
     *
     * @param owner The owner of the changelog
     * @param changelogId The changelog identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + CHANGELOGS_KEY + " SET " + CHANGELOG_READ_KEY + "=true WHERE "
                    + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY + " AND "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void markAsRead(
            @Param(CHANGELOG_OWNER_KEY) String owner,
            @Param(IDENTIFIER_KEY) String changelogId
    );

    /**
     * Method to execute the query to delete a {@link Changelog}
     *
     * @param owner The owner of the changelog
     * @param changelogId The changelog identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + CHANGELOGS_KEY + " WHERE "
                    + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY + " AND "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteChangelog(
            @Param(CHANGELOG_OWNER_KEY) String owner,
            @Param(IDENTIFIER_KEY) String changelogId
    );

}
