package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.repositories.UsersRepository;
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
import static com.tecknobit.pandoro.Launcher.IMAGES_PATH;
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
     * {@code PUBLIC_USERS_TABLE} public users table
     */
    public static final String PUBLIC_USERS_TABLE = "public_users";

    /**
     * {@code USERS_TABLE} users table
     */
    public static final String USERS_TABLE = "users";

    /**
     * {@code GROUP_MEMBERS_TABLE} group members table
     */
    public static final String GROUP_MEMBERS_TABLE = "group_members";

    /**
     * {@code NAME_KEY} name key
     */
    public static final String NAME_KEY = "name";

    /**
     * {@code TOKEN_KEY} token key
     */
    public static final String TOKEN_KEY = "token";

    /**
     * {@code COMPLETE_NAME_KEY} complete name key
     */
    public static final String COMPLETE_NAME_KEY = "completeName";

    /**
     * {@code SURNAME_KEY} surname key
     */
    public static final String SURNAME_KEY = "surname";

    /**
     * {@code PROFILE_PIC_KEY} profile pic key
     */
    public static final String PROFILE_PIC_KEY = "profile_pic";

    /**
     * {@code EMAIL_KEY} email key
     */
    public static final String EMAIL_KEY = "email";

    /**
     * {@code PASSWORD_KEY} password key
     */
    public static final String PASSWORD_KEY = "password";

    /**
     * {@code UNREAD_CHANGELOGS_KEY} unread changelogs number key
     */
    public static final String UNREAD_CHANGELOGS_KEY = "unreadChangelogsNumber";

    /**
     * {@code ADMIN_GROUPS_KEY} admin groups key
     */
    public static final String ADMIN_GROUPS_KEY = "adminGroups";

    /**
     * {@code PROFILE_PICS_FOLDER} the path of the folder where store the profile pics
     */
    public static final String PROFILE_PICS_FOLDER = IMAGES_PATH + "profiles/";

    /**
     * {@code usersRepository} instance for the users repository
     */
    @Autowired
    private UsersRepository usersRepository;

    /**
     * {@code DEFAULT_PROFILE_PIC} the default profile pic path when the user has not set own image
     */
    // TODO: 31/10/2023 CHANGE WITH THE REAL DEFAULT ICON
    public static final String DEFAULT_PROFILE_PIC = "https://sb.ecobnb.net/app/uploads/sites/2/2022/03/delfini-copertina.jpg";

    /**
     * Method to execute the signup action and store the new user details
     *
     * @param userId:   the user identifier
     * @param token:    the token of the user
     * @param name:     the name of the user
     * @param surname:  the surname of the user
     * @param email:    the email of the user
     * @param password: the password of the user
     * @throws NoSuchAlgorithmException when the hash of the password fails
     */
    public void signUp(String userId, String token, String name, String surname, String email, String password) throws NoSuchAlgorithmException {
        usersRepository.save(new User(
                userId,
                name,
                token,
                surname,
                email,
                hash(password)
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
        return usersRepository.getUserByEmailAndPassword(email, hash(password));
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
        File picsFolder = new File(PROFILE_PICS_FOLDER);
        for (File pic : Objects.requireNonNull(picsFolder.listFiles())) {
            if (pic.getName().contains(userId))
                if (!pic.delete())
                    throw new IOException();
        }
        String contentType = profilePic.getContentType();
        String suffix;
        switch (Objects.requireNonNull(contentType)) {
            case IMAGE_JPEG_VALUE -> suffix = "jpeg";
            case IMAGE_PNG_VALUE -> suffix = "png";
            default -> throw new IOException();
        }
        File file = new File(PROFILE_PICS_FOLDER + userId + "." + suffix);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(profilePic.getBytes());
        }
        String profilePicPath = file.getPath().replaceAll("\\\\", "/").replace(IMAGES_PATH, "");
        usersRepository.changeProfilePic(userId, token, profilePicPath);
        return profilePicPath;
    }

    /**
     * Method to execute the change and store of the user's email
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     * @param newEmail: the new user email to set
     */
    public void changeEmail(String userId, String token, String newEmail) {
        usersRepository.changeEmail(userId, token, newEmail);
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
     * Method to execute the deletion of the user account
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     */
    public void deleteAccount(String userId, String token) {
        usersRepository.deleteAccount(userId, token);
    }

}
