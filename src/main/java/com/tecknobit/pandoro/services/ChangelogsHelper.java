package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Changelog;
import com.tecknobit.pandoro.records.Changelog.ChangelogEvent;
import com.tecknobit.pandoro.services.repositories.ChangelogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The {@code ChangelogsHelper} class is useful to manage all the changelogs database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Service
public class ChangelogsHelper {

    /**
     * {@code CHANGELOG_IDENTIFIER_KEY} changelog identifier key
     */
    public static final String CHANGELOG_IDENTIFIER_KEY = "changelog_id";

    /**
     * {@code CHANGELOG_EVENT_KEY} changelog event key
     */
    public static final String CHANGELOG_EVENT_KEY = "changelog_event";

    /**
     * {@code CHANGELOG_TIMESTAMP_KEY} changelog timestamp key
     */
    public static final String CHANGELOG_TIMESTAMP_KEY = "timestamp";

    /**
     * {@code CHANGELOG_GROUP_IDENTIFIER_KEY} the group identifier attached to a changelog key
     */
    public static final String CHANGELOG_GROUP_IDENTIFIER_KEY = "group_id";

    /**
     * {@code CHANGELOG_EXTRA_CONTENT_KEY} extra content of the changelog key
     */
    public static final String CHANGELOG_EXTRA_CONTENT_KEY = "extra_content";

    /**
     * {@code CHANGELOG_RED_KEY} whether the changelog is red key
     */
    public static final String CHANGELOG_RED_KEY = "red";

    /**
     * {@code CHANGELOG_OWNER_KEY} owner of the changelog key
     */
    public static final String CHANGELOG_OWNER_KEY = "owner";

    /**
     * {@code changelogsRepository} instance for the changelog repository
     */
    @Autowired
    private ChangelogsRepository changelogsRepository;

    /**
     * Method to get the user's changelogs list
     *
     * @param ownerId: the owner identifier
     * @return the changelogs list as {@link List} of {@link Changelog}
     */
    public List<Changelog> getChangelogs(String ownerId) {
        return changelogsRepository.getChangelogs(ownerId);
    }

    /**
     * Method to check whether a changelog exists
     *
     * @param changelogId: the changelog identifier
     * @return whether a changelog exists as boolean
     */
    public boolean changelogExists(String changelogId) {
        return changelogsRepository.findById(changelogId).isPresent();
    }

    /**
     * Method to mark as red a changelog
     *
     * @param changelogId: the changelog identifier
     * @param ownerId:     the owner identifier
     */
    public void markAsRed(String changelogId, String ownerId) {
        changelogsRepository.markAsRed(ownerId, changelogId);
    }

    /**
     * Method to delete a changelog
     *
     * @param changelogId: the changelog identifier
     * @param ownerId:     the owner identifier
     */
    public void deleteChangelog(String changelogId, String ownerId) {
        changelogsRepository.deleteChangelog(ownerId, changelogId);
    }

    // TODO: 04/11/2023 IMPLEMENT CHANGELOG CREATOR
    //  INSERT INTO changelogs (changelog_id,changelog_event,extra_content,group_id,project_id,red,timestamp,owner)
    //  VALUES (1231,"LEFT_GROUP","gagga",NULL,NULL,false,1699102591,"f1fdab65e1494050ad5b29af5966f972")

    /**
     * Method to create a changelog
     *
     * @param ownerId: the owner identifier
     * @param event:   the changelog event
     */
    public void createChangelog(String ownerId, ChangelogEvent event) {

    }

}
