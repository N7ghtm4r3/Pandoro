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
import java.util.Objects;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@Service
public class UsersHelper {

    public static final String PUBLIC_USERS_TABLE = "public_users";

    public static final String USERS_TABLE = "users";

    public static final String GROUP_MEMBERS_TABLE = "group_members";

    public static final String SERVER_ADDRESS_KEY = "server_address";

    public static final String NAME_KEY = "name";

    public static final String TOKEN_KEY = "token";

    public static final String COMPLETE_NAME_KEY = "completeName";

    public static final String SURNAME_KEY = "surname";

    public static final String PROFILE_PIC_KEY = "profile_pic";

    public static final String EMAIL_KEY = "email";

    public static final String PASSWORD_KEY = "password";

    public static final String UNREAD_CHANGELOGS_KEY = "unreadChangelogsNumber";

    public static final String ADMIN_GROUPS_KEY = "adminGroups";

    public static final String PROFILE_PICS_FOLDER = "profilePics/";

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
        String profilePicPath = file.getPath();
        usersRepository.changeProfilePic(userId, token, profilePicPath);
        return profilePicPath;
    }

    public String getProfilePic(String userId) {
        File picsFolder = new File(PROFILE_PICS_FOLDER);
        for (File pic : Objects.requireNonNull(picsFolder.listFiles())) {
            String picName = pic.getName();
            if (picName.contains(userId))
                return PROFILE_PICS_FOLDER + picName;
        }
        return DEFAULT_PROFILE_PIC;
    }

    public void changeEmail(String userId, String token, String newEmail) {
        usersRepository.changeEmail(userId, token, newEmail);
    }

    public void changePassword(String userId, String token, String newPassword) {
        usersRepository.changePassword(userId, token, newPassword);
    }

    public void deleteAccount(String userId, String token) {
        usersRepository.deleteAccount(userId, token);
    }

}
