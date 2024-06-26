package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.services.repositories.ChangelogsRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
import com.tecknobit.pandorocore.records.Changelog;
import com.tecknobit.pandorocore.records.Changelog.ChangelogEvent;
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
     * {@code changelogsRepository} instance for the changelog repository
     */
    @Autowired
    private ChangelogsRepository changelogsRepository;

    /**
     * {@code membersRepository} instance for the members of a group repository
     */
    @Autowired
    private GroupMembersRepository membersRepository;

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
     * @param groupId: the group identifier where leave if is a {@link ChangelogEvent#INVITED_GROUP}
     */
    public void deleteChangelog(String changelogId, String ownerId, String groupId) throws IllegalAccessException {
        Changelog changelog = changelogsRepository.getChangelog(changelogId, ownerId);
        if (changelog != null) {
            if (changelog.getChangelogEvent() == ChangelogEvent.INVITED_GROUP) {
                if (groupId == null)
                    throw new IllegalAccessException();
                membersRepository.leaveGroup(ownerId, groupId);
            }
            changelogsRepository.deleteChangelog(ownerId, changelogId);
        } else
            throw new IllegalAccessException();
    }

}
