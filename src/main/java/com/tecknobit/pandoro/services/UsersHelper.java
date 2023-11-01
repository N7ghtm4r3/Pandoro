package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.User;
import com.tecknobit.pandoro.services.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersHelper {

    public static final String USERS_TABLE = "users";

    public static final String USER_KEY = "user";

    public static final String SERVER_ADDRESS_KEY = "serverAddress";

    public static final String NAME_KEY = "name";

    public static final String TOKEN_KEY = "token";

    public static final String SURNAME_KEY = "surname";

    public static final String PROFILE_PIC_KEY = "profilePic";

    public static final String EMAIL_KEY = "email";

    public static final String PASSWORD_KEY = "password";

    public static final String CHANGELOGS_KEY = "changelogs";

    public static final String GROUPS_KEY = "groups";

    public static final String PROJECTS_KEY = "projects";

    public static final String NOTES_KEY = "notes";

    // TODO: 31/10/2023 CHANGE WITH THE REAL DEFAULT ICON
    public static final String DEFAULT_PROFILE_PIC = "https://sb.ecobnb.net/app/uploads/sites/2/2022/03/delfini-copertina.jpg";

    @Autowired
    private UsersRepository usersRepository;

    public void signUp(String userId, String token, String name, String surname, String email, String password) {
        usersRepository.save(new User(
                userId,
                name,
                token,
                surname,
                email,
                password
        ));
    }

    public User signIn(String email, String password) {
        return usersRepository.getUserByEmailAndPassword(email, password);
    }

    public User getProfileDetails(String userId) {
        // TODO: 31/10/2023 FETCH FROM SQL THEN
        return new User();
    }

    public void changeEmail(String userId, String newEmail) {
        // TODO: 31/10/2023 EDIT IN SQL THEN
    }

    public void changePassword(String userId, String newPassword) {
        // TODO: 31/10/2023 EDIT IN SQL THEN
    }

    public void delete(String userId) {
        // TODO: 31/10/2023 EDIT IN SQL THEN
    }

}
