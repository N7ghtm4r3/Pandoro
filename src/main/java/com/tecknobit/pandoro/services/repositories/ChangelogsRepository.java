package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.Changelog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandoro.controllers.ChangelogsController.CHANGELOGS_KEY;
import static com.tecknobit.pandoro.services.ChangelogsHelper.*;

@Service
@Repository
public interface ChangelogsRepository extends JpaRepository<Changelog, String> {

    @Query(
            value = "SELECT * FROM " + CHANGELOGS_KEY + " WHERE " + CHANGELOG_OWNER_KEY + "=:" + CHANGELOG_OWNER_KEY,
            nativeQuery = true
    )
    List<Changelog> getChangelogs(@Param(CHANGELOG_OWNER_KEY) String owner);

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
