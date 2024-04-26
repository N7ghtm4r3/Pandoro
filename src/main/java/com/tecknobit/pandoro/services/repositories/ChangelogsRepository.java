package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandorocore.records.Changelog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandorocore.records.Changelog.*;
import static com.tecknobit.pandorocore.records.Group.GROUP_IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.records.Project.PROJECT_IDENTIFIER_KEY;

/**
 * The {@code ChangelogsRepository} interface is useful to manage the queries for the changelogs
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see Changelog
 */
@Service
@Repository
public interface ChangelogsRepository extends JpaRepository<Changelog, String> {

    /**
     * Method to execute the query to select the list of a {@link Changelog}
     *
     * @param owner: the owner of the changelogs
     * @return the list of changelogs as {@link List} of {@link Changelog}
     */
    @Query(
            value = "SELECT * FROM " + CHANGELOGS_KEY + " WHERE " + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY
                    + " ORDER BY " + CHANGELOG_TIMESTAMP_KEY + " DESC ",
            nativeQuery = true
    )
    List<Changelog> getChangelogs(@Param(CHANGELOG_OWNER_KEY) String owner);

    /**
     * Method to execute the query to select the a {@link Changelog}
     *
     * @param changelogId: the identifier of the changelog
     * @param owner:       the owner of the changelogs
     * @return the changelog as {@link Changelog}
     */
    @Query(
            value = "SELECT * FROM " + CHANGELOGS_KEY + " WHERE " + CHANGELOG_IDENTIFIER_KEY + "=:"
                    + CHANGELOG_IDENTIFIER_KEY + " AND " + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY,
            nativeQuery = true
    )
    Changelog getChangelog(
            @Param(CHANGELOG_IDENTIFIER_KEY) String changelogId,
            @Param(CHANGELOG_OWNER_KEY) String owner
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(

            value = "INSERT INTO " + CHANGELOGS_KEY
                    + "( "
                    + CHANGELOG_IDENTIFIER_KEY + ","
                    + CHANGELOG_EVENT_KEY + ","
                    + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + CHANGELOG_RED_KEY + ","
                    + CHANGELOG_TIMESTAMP_KEY + ","
                    + PROJECT_IDENTIFIER_KEY + ","
                    + CHANGELOG_OWNER_KEY + ") VALUES "
                    + "( "
                    + ":" + CHANGELOG_IDENTIFIER_KEY + ","
                    + ":#{#" + CHANGELOG_EVENT_KEY + ".name()},"
                    + ":" + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + "false,"
                    + ":" + CHANGELOG_TIMESTAMP_KEY + ","
                    + ":" + PROJECT_IDENTIFIER_KEY + ","
                    + ":" + CHANGELOG_OWNER_KEY + ")",
            nativeQuery = true
    )
    void addProjectChangelog(
            @Param(CHANGELOG_IDENTIFIER_KEY) String changelogId,
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
                    + CHANGELOG_IDENTIFIER_KEY + ","
                    + CHANGELOG_EVENT_KEY + ","
                    + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + CHANGELOG_RED_KEY + ","
                    + CHANGELOG_TIMESTAMP_KEY + ","
                    + GROUP_IDENTIFIER_KEY + ","
                    + CHANGELOG_OWNER_KEY + ") VALUES "
                    + "( "
                    + ":" + CHANGELOG_IDENTIFIER_KEY + ","
                    + ":#{#" + CHANGELOG_EVENT_KEY + ".name()},"
                    + ":" + CHANGELOG_EXTRA_CONTENT_KEY + ","
                    + "false,"
                    + ":" + CHANGELOG_TIMESTAMP_KEY + ","
                    + ":" + GROUP_IDENTIFIER_KEY + ","
                    + ":" + CHANGELOG_OWNER_KEY + ")",
            nativeQuery = true
    )
    void addGroupChangelog(
            @Param(CHANGELOG_IDENTIFIER_KEY) String changelogId,
            @Param(CHANGELOG_EVENT_KEY) ChangelogEvent changelogEvent,
            @Param(CHANGELOG_EXTRA_CONTENT_KEY) String extraContent,
            @Param(CHANGELOG_TIMESTAMP_KEY) long changelogTimestamp,
            @Param(GROUP_IDENTIFIER_KEY) String groupId,
            @Param(CHANGELOG_OWNER_KEY) String owner
    );

    /**
     * Method to execute the query to mark as red a {@link Changelog}
     *
     * @param owner:       the owner of the changelog
     * @param changelogId: the changelog identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + CHANGELOGS_KEY + " SET " + CHANGELOG_RED_KEY + "=true WHERE "
                    + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY + " AND "
                    + CHANGELOG_IDENTIFIER_KEY + "=:" + CHANGELOG_IDENTIFIER_KEY,
            nativeQuery = true
    )
    void markAsRed(
            @Param(CHANGELOG_OWNER_KEY) String owner,
            @Param(CHANGELOG_IDENTIFIER_KEY) String changelogId
    );

    /**
     * Method to execute the query to delete a {@link Changelog}
     *
     * @param owner: the owner of the changelog
     * @param changelogId: the changelog identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + CHANGELOGS_KEY + " WHERE "
                    + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY + " AND "
                    + CHANGELOG_IDENTIFIER_KEY + "=:" + CHANGELOG_IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteChangelog(
            @Param(CHANGELOG_OWNER_KEY) String owner,
            @Param(CHANGELOG_IDENTIFIER_KEY) String changelogId
    );

}
