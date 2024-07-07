package com.tecknobit.pandoro.services;

import com.tecknobit.equinox.environment.helpers.services.EquinoxUsersHelper;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
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
        membersRepository.deleteMember(id);
        super.deleteUser(id);
    }

}
