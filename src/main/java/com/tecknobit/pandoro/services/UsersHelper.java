package com.tecknobit.pandoro.services;

import com.tecknobit.equinox.environment.helpers.services.EquinoxUsersHelper;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
import com.tecknobit.pandoro.services.repositories.projects.ProjectsRepository;
import com.tecknobit.pandoro.services.repositories.projects.UpdatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UsersHelper extends EquinoxUsersHelper {

    /**
     * {@code membersRepository} instance for the members of a group repository
     */
    @Autowired
    private GroupMembersRepository membersRepository;

    /**
     * {@code projectsRepository} instance for the projects repository
     */
    @Autowired
    private ProjectsRepository projectsRepository;

    /**
     * {@code updatesRepository} instance for the updates repository
     */
    @Autowired
    private UpdatesRepository updatesRepository;

    /**
     * {@code notesRepository} instance for the notes repository
     */
    @Autowired
    private NotesRepository notesRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(String id) {
        notesRepository.removeUserConstraints(id);
        notesRepository.setGroupNotesAuthorAfterUserDeletion(id);
        notesRepository.setGroupNotesMarkerAfterUserDeletion(id);
        updatesRepository.removeUserConstraints(id);
        projectsRepository.deleteProjects(id);
        membersRepository.deleteMember(id);
        super.deleteUser(id);
    }

}
