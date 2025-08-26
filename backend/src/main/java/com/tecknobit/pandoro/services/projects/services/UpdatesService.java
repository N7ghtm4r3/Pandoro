package com.tecknobit.pandoro.services.projects.services;

import com.tecknobit.equinoxbackend.annotations.TableColumns;
import com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper;
import com.tecknobit.pandoro.services.changelogs.helpers.ChangelogsNotifier;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.projects.repositories.UpdatesRepository;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController.generateIdentifier;
import static com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper.InsertCommand.INSERT_IGNORE_INTO;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.SCHEDULED;

// TODO: 26/08/2025 TO DOCU 1.2.0
@Service
public class UpdatesService extends EquinoxItemsHelper {

    /**
     * {@code updatesRepository} instance for the updates repository
     */
    private final UpdatesRepository updatesRepository;

    /**
     * {@code changelogsNotifier} instance used to notify a changelog event
     */
    private final ChangelogsNotifier changelogsNotifier;

    // TODO: 26/08/2025 TO DOCU 1.2.0
    private final UpdateEventsNotifier updateEventsNotifier;

    /**
     * Constructor used to init the service
     *
     * @param updatesRepository  The instance for the updates repository
     * @param changelogsNotifier The instance used to notify a changelog event
     */
    // TODO: 26/08/2025 TO DOCU 1.2.0
    @Autowired
    public UpdatesService(UpdatesRepository updatesRepository, ChangelogsNotifier changelogsNotifier,
                          UpdateEventsNotifier updateEventsNotifier) {
        this.updatesRepository = updatesRepository;
        this.changelogsNotifier = changelogsNotifier;
        this.updateEventsNotifier = updateEventsNotifier;
    }

    /**
     * Method to check whether an update with the target project_version inserted already exists
     *
     * @param projectId     The project identifier
     * @param targetVersion The target project_version to check
     * @return whether an update with the target project_version inserted already exists as boolean
     */
    public boolean targetVersionExists(String projectId, String targetVersion) {
        return updatesRepository.getUpdateByVersion(projectId, targetVersion) != null;
    }

    /**
     * Method to fetch and check if an update exists
     *
     * @param projectId The project identifier
     * @param updateId  The update identifier
     * @return project update, if exists, as {@link Update}, null if not
     */
    public Update updateExists(String projectId, String updateId) {
        return updatesRepository.getUpdateById(projectId, updateId);
    }

    /**
     * Method to schedule a new update
     *
     * @param updateId      The update identifier
     * @param targetVersion The target project_version of the new update
     * @param changeNotes   The change notes of the new update
     * @param user        The user who scheduled the update
     * @param project       The project owner of the update
     */
    public void scheduleUpdate(String updateId, String targetVersion, List<String> changeNotes,
                               Project project, PandoroUser user) {
        String projectId = project.getId();
        String userId = user.getId();
        Update update = new Update(updateId, user, System.currentTimeMillis(), targetVersion, SCHEDULED, project);
        updatesRepository.save(update);
        batchInsert(INSERT_IGNORE_INTO, NOTES_KEY, new EquinoxItemsHelper.BatchQuery<String>() {
            @Override
            public Collection<String> getData() {
                return changeNotes;
            }

            @Override
            @TableColumns(columns = {IDENTIFIER_KEY, AUTHOR_KEY, CONTENT_NOTE_KEY, CREATION_DATE_KEY, UPDATE_ESCAPED_KEY})
            public void prepareQuery(Query query, int index, Collection<String> changeNotes) {
                for (String changeNote : changeNotes) {
                    query.setParameter(index++, generateIdentifier());
                    query.setParameter(index++, userId);
                    query.setParameter(index++, changeNote);
                    query.setParameter(index++, System.currentTimeMillis());
                    query.setParameter(index++, updateId);
                }
            }

            @Override
            public String[] getColumns() {
                return new String[]{IDENTIFIER_KEY, AUTHOR_KEY, CONTENT_NOTE_KEY, CREATION_DATE_KEY, UPDATE_ESCAPED_KEY};
            }
        });
        updateEventsNotifier.updateScheduled(user, update);
        if (project.hasGroups())
            changelogsNotifier.scheduledNewUpdate(targetVersion, projectId, userId);
    }

    /**
     * Method to start an existing update
     *
     * @param project The project owner of the update
     * @param update  The update to start
     * @param userId  The user identifier who start the update
     */
    public void startUpdate(Project project, Update update, String userId) {
        String projectId = project.getId();
        String updateId = update.getId();
        updatesRepository.startUpdate(updateId, System.currentTimeMillis(), userId);
        if (project.hasGroups())
            changelogsNotifier.updateStarted(update.getTargetVersion(), projectId, userId);
    }

    /**
     * Method to publish an existing update
     *
     * @param project The project owner of the update
     * @param update  The update to publish
     * @param userId  The user identifier who publish the update
     */
    public void publishUpdate(Project project, Update update, String userId) {
        String projectId = project.getId();
        String updateId = update.getId();
        updatesRepository.publishUpdate(updateId, System.currentTimeMillis(), userId);
        if (project.hasGroups())
            changelogsNotifier.updatePublished(update.getTargetVersion(), projectId, userId);
    }

    /**
     * Method to delete an update
     *
     * @param project The project owner of the update
     * @param update  The update to delete
     * @param userId  The user identifier
     */
    public void deleteUpdate(Project project, Update update, String userId) {
        String projectId = project.getId();
        updatesRepository.deleteUpdate(update.getId());
        if (project.hasGroups())
            changelogsNotifier.updateDeleted(update.getTargetVersion(), projectId, userId);
    }

}
