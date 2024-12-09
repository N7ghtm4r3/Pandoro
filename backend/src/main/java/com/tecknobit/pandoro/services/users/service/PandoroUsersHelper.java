package com.tecknobit.pandoro.services.users.service;

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser;
import com.tecknobit.equinoxbackend.environment.services.users.service.EquinoxUsersHelper;
import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import com.tecknobit.pandoro.services.groups.repositories.GroupMembersRepository;
import com.tecknobit.pandoro.services.notes.repository.NotesRepository;
import com.tecknobit.pandoro.services.projects.repositories.ProjectsRepository;
import com.tecknobit.pandoro.services.projects.repositories.UpdatesRepository;
import com.tecknobit.pandoro.services.users.dto.CandidateMember;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandoro.services.users.repository.PandoroUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class PandoroUsersHelper extends EquinoxUsersHelper<PandoroUser, PandoroUsersRepository> {

    /**
     * {@code membersRepository} instance for the members of a group project_repository
     */
    @Autowired
    private GroupMembersRepository membersRepository;

    /**
     * {@code projectsRepository} instance for the projects project_repository
     */
    @Autowired
    private ProjectsRepository projectsRepository;

    /**
     * {@code updatesRepository} instance for the updates project_repository
     */
    @Autowired
    private UpdatesRepository updatesRepository;

    /**
     * {@code notesRepository} instance for the notes project_repository
     */
    @Autowired
    private NotesRepository notesRepository;

    /**
     * Method to get the candidates user list
     *
     * @param id       The user identifier
     * @param page     The page requested
     * @param pageSize The size of the items to insert in the page
     * @return the candidates list as {@link PaginatedResponse} of {@link PandoroUser}
     */
    public PaginatedResponse<CandidateMember> getCandidateMembers(String id, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<PandoroUser> users = usersRepository.getCandidates(id, pageable);
        ArrayList<CandidateMember> candidateMembers = new ArrayList<>();
        for (PandoroUser user : users)
            candidateMembers.add(user.convertToRelatedDTO());
        long totalCandidates = usersRepository.count() - 1;
        return new PaginatedResponse<>(candidateMembers, page, pageSize, totalCandidates);
    }

    /**
     * Method to change the profile pic of the {@link EquinoxUser}
     *
     * @param profilePic The profile pic resource
     * @param userId     The identifier of the user
     */
    @Override
    public String changeProfilePic(MultipartFile profilePic, String userId) throws IOException {
        String profilePicPath = super.changeProfilePic(profilePic, userId);
        membersRepository.changeProfilePic(userId, profilePicPath);
        return profilePicPath;
    }

    /**
     * Method to change the email of the {@link EquinoxUser}
     *
     * @param newEmail The new email of the user
     * @param userId   The identifier of the user
     */
    @Override
    public void changeEmail(String newEmail, String userId) {
        super.changeEmail(newEmail, userId);
        membersRepository.changeEmail(userId, newEmail);
    }

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
