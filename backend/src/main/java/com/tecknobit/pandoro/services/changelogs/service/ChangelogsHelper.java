package com.tecknobit.pandoro.services.changelogs.service;

import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import com.tecknobit.pandoro.services.changelogs.entity.Changelog;
import com.tecknobit.pandoro.services.changelogs.repository.ChangelogsRepository;
import com.tecknobit.pandoro.services.groups.repositories.GroupMembersRepository;
import com.tecknobit.pandorocore.enums.ChangelogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandorocore.enums.ChangelogEvent.INVITED_GROUP;

/**
 * The {@code ChangelogsHelper} class is useful to manage all the changelogs database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Service
public class ChangelogsHelper {

    /**
     * {@code changelogsRepository} instance for the changelog project_repository
     */
    @Autowired
    private ChangelogsRepository changelogsRepository;

    /**
     * {@code membersRepository} instance for the members of a group project_repository
     */
    @Autowired
    private GroupMembersRepository membersRepository;

    /**
     * Method to get the user's changelogs list
     *
     * @param ownerId The owner identifier
     * @return the changelogs list as {@link PaginatedResponse} of {@link Changelog}
     */
    public long getUnreadChangelogsCount(String ownerId) {
        return changelogsRepository.getUnreadChangelogsCount(ownerId);
    }

    /**
     * Method to get the user's changelogs list
     *
     * @param ownerId The owner identifier
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @return the changelogs list as {@link PaginatedResponse} of {@link Changelog}
     */
    public PaginatedResponse<Changelog> getChangelogs(String ownerId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Changelog> changelogs = changelogsRepository.getChangelogs(ownerId, pageable);
        long changelogsCount = changelogsRepository.getChangelogsCount(ownerId);
        return new PaginatedResponse<>(changelogs, page, pageSize, changelogsCount);
    }

    /**
     * Method to check whether a changelog exists
     *
     * @param changelogId The changelog identifier
     * @return whether a changelog exists as boolean
     */
    public boolean changelogExists(String changelogId) {
        return changelogsRepository.findById(changelogId).isPresent();
    }

    /**
     * Method to mark as red a changelog
     *
     * @param changelogId The changelog identifier
     * @param ownerId:     the owner identifier
     */
    public void markAsRed(String changelogId, String ownerId) {
        changelogsRepository.markAsRed(ownerId, changelogId);
    }

    /**
     * Method to delete a changelog
     *
     * @param changelogId The changelog identifier
     * @param ownerId:     the owner identifier
     * @param groupId The group identifier where leave if is a {@link ChangelogEvent#INVITED_GROUP}
     */
    public void deleteChangelog(String changelogId, String ownerId, String groupId) throws IllegalAccessException {
        Changelog changelog = changelogsRepository.getChangelog(changelogId, ownerId);
        if (changelog != null) {
            if (changelog.getChangelogEvent() == INVITED_GROUP) {
                if (groupId == null)
                    throw new IllegalAccessException();
                membersRepository.leaveGroup(ownerId, groupId);
            }
            changelogsRepository.deleteChangelog(ownerId, changelogId);
        } else
            throw new IllegalAccessException();
    }

}
