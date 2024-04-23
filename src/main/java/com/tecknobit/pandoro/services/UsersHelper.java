package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.services.repositories.NotesRepository;
import com.tecknobit.pandoro.services.repositories.UsersRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
import com.tecknobit.pandoro.services.repositories.projects.ProjectsRepository;
import com.tecknobit.pandoro.services.repositories.projects.UpdatesRepository;
import com.tecknobit.pandorocore.records.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static com.tecknobit.apimanager.apis.APIRequest.SHA256_ALGORITHM;
import static com.tecknobit.apimanager.apis.APIRequest.base64Digest;
import static com.tecknobit.pandoro.helpers.ResourcesProvider.IMAGES_PATH;
import static com.tecknobit.pandoro.helpers.ResourcesProvider.PROFILE_PICS_PATH;
import static java.lang.System.currentTimeMillis;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

/**
 * The {@code UsersHelper} class is useful to manage all the users database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Service
public class UsersHelper {

    /**
     * {@code usersRepository} instance for the users repository
     */
    @Autowired
    private UsersRepository usersRepository;

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
     * Method to execute the signup action and store the new user details
     *
     * @param userId:   the user identifier
     * @param token:    the token of the user
     * @param name:     the name of the user
     * @param surname:  the surname of the user
     * @param email:    the email of the user
     * @param password: the password of the user
     * @param language: the language of the user
     * @throws NoSuchAlgorithmException when the hash of the password fails
     */
    public void signUp(String userId, String token, String name, String surname, String email, String password,
                       String language) throws NoSuchAlgorithmException {
        usersRepository.save(new User(
                userId,
                name,
                token,
                surname,
                email.toLowerCase(),
                hash(password),
                language
        ));
    }

    /**
     * Method to execute the sign in action check
     *
     * @param email: the email of the user
     * @param password: the password of the user
     * @return if the user correctly authenticated as {@link User} or null if not
     * @throws NoSuchAlgorithmException when the hash of the password fails
     */
    public User signIn(String email, String password) throws NoSuchAlgorithmException {
        return usersRepository.getUserByEmailAndPassword(email.toLowerCase(), hash(password));
    }

    /**
     * Method to execute the change of the user's profile pic
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     * @param profilePic: the profile pic chosen by the user to set as the new profile pic
     * @return the path of the profile pic
     * @throws IOException when the change operation fails
     */
    public String changeProfilePic(String userId, String token, MultipartFile profilePic) throws IOException {
        deleteProfilePic(userId);
        File file = new File(PROFILE_PICS_PATH + userId + currentTimeMillis() + "." + getSuffix(profilePic));
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(profilePic.getBytes());
        }
        String profilePicPath = file.getPath().replaceAll("\\\\", "/").replace(IMAGES_PATH, "");
        usersRepository.changeProfilePic(userId, token, profilePicPath);
        membersRepository.changeProfilePic(userId, profilePicPath);
        return profilePicPath;
    }

    /**
     * Method to get the suffix of the image uploaded
     *
     * @param profilePic: thw multipart file uploaded
     * @return the suffix of the image as {@link String}
     */
    private String getSuffix(MultipartFile profilePic) {
        String contentType = profilePic.getContentType();
        String suffix;
        switch (Objects.requireNonNull(contentType)) {
            case IMAGE_JPEG_VALUE -> suffix = "jpeg";
            case IMAGE_PNG_VALUE -> suffix = "png";
            default -> suffix = "jpg";
        }
        return suffix;
    }

    /**
     * Method to delete the user profile pic
     *
     * @param userId: the user id for the profile pic deletion
     */
    private void deleteProfilePic(String userId) throws IOException {
        File picsFolder = new File(PROFILE_PICS_PATH);
        for (File pic : Objects.requireNonNull(picsFolder.listFiles())) {
            if (pic.getName().contains(userId))
                if (!pic.delete())
                    throw new IOException();
        }
    }

    /**
     * Method to execute the change and store of the user's email
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     * @param newEmail: the new user email to set
     */
    public void changeEmail(String userId, String token, String newEmail) {
        newEmail = newEmail.toLowerCase();
        usersRepository.changeEmail(userId, token, newEmail);
        membersRepository.changeEmail(userId, newEmail);
    }

    /**
     * Method to execute the change and store of the user's password
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     * @param newPassword: the new user password to set
     */
    public void changePassword(String userId, String token, String newPassword) throws NoSuchAlgorithmException {
        usersRepository.changePassword(userId, token, hash(newPassword));
    }

    /**
     * Method to hash a sensitive user data
     *
     * @param valueToHash: the user value to hash
     * @throws NoSuchAlgorithmException when the hash of the user value fails
     */
    private String hash(String valueToHash) throws NoSuchAlgorithmException {
        return base64Digest(valueToHash, SHA256_ALGORITHM);
    }

    /**
     * Method to execute the change and store of the user's language
     *
     * @param userId:      the user identifier
     * @param token:       the token of the user
     * @param newLanguage: the new user language to set
     */
    public void changeLanguage(String userId, String token, String newLanguage) {
        usersRepository.changeLanguage(userId, token, newLanguage);
    }

    /**
     * Method to execute the deletion of the user account
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     */
    public void deleteAccount(String userId, String token) throws IOException {
        notesRepository.removeUserConstraints(userId);
        notesRepository.setGroupNotesAuthorAfterUserDeletion(userId);
        notesRepository.setGroupNotesMarkerAfterUserDeletion(userId);
        updatesRepository.removeUserConstraints(userId);
        projectsRepository.deleteProjects(userId);
        usersRepository.deleteAccount(userId, token);
        membersRepository.deleteMember(userId);
        deleteProfilePic(userId);
    }

}
