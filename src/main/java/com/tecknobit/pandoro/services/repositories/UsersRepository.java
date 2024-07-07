package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.equinox.environment.records.EquinoxUser;
import com.tecknobit.pandorocore.records.users.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static com.tecknobit.equinox.environment.records.EquinoxUser.*;

/**
 * The {@code UsersRepository} interface is useful to manage the queries for the users
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see User
 */
@Service
@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    /**
     * Method to execute the query to select a {@link User} by its email and its password
     *
     * @param email:    the user email
     * @param password: the user password
     * @return the user, if exists, as {@link User}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY
                    + " AND " + PASSWORD_KEY + "=:" + PASSWORD_KEY,
            nativeQuery = true
    )
    User getUserByEmailAndPassword(
            @Param(EMAIL_KEY) String email,
            @Param(PASSWORD_KEY) String password
    );

    /**
     * Method to execute the query to select a {@link User} by its identifier and its token
     *
     * @param userId: the user identifier
     * @param token: the user token
     * @return the user, if exists, as {@link User}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + TOKEN_KEY + "=:" + TOKEN_KEY,
            nativeQuery = true
    )
    User getAuthorizedUser(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token
    );

    /**
     * Method to execute the query to select a {@link User} by its email
     *
     * @param email: the user email
     * @return the user, if exists, as {@link User}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY,
            nativeQuery = true
    )
    User getUserByEmail(
            @Param(EMAIL_KEY) String email
    );

    /**
     * Method to execute the query to find a {@link EquinoxUser} by email field
     *
     * @param email: the email to find the user
     * @return the user, if exists, as {@link EquinoxUser}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY,
            nativeQuery = true
    )
    User findUserByEmail(
            @Param(EMAIL_KEY) String email
    );

    /**
     * Method to execute the query to change the profile pic of the {@link EquinoxUser}
     *
     * @param profilePicUrl: the profile pic formatted as url
     * @param id:            the identifier of the user
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_KEY + " SET " + PROFILE_PIC_KEY + "=:" + PROFILE_PIC_KEY + " WHERE "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void changeProfilePic(
            @Param(PROFILE_PIC_KEY) String profilePicUrl,
            @Param(IDENTIFIER_KEY) String id
    );

    /**
     * Method to execute the query to change the email of the {@link EquinoxUser}
     *
     * @param newEmail: the new email of the user
     * @param id:       the identifier of the user
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_KEY + " SET " + EMAIL_KEY + "=:" + EMAIL_KEY + " WHERE "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void changeEmail(
            @Param(EMAIL_KEY) String newEmail,
            @Param(IDENTIFIER_KEY) String id
    );

    /**
     * Method to execute the query to change the password of the {@link EquinoxUser}
     *
     * @param newPassword: the new password of the user
     * @param id:          the identifier of the user
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_KEY + " SET " + PASSWORD_KEY + "=:" + PASSWORD_KEY + " WHERE "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void changePassword(
            @Param(PASSWORD_KEY) String newPassword,
            @Param(IDENTIFIER_KEY) String id
    );

    /**
     * Method to execute the query to change the language of the {@link EquinoxUser}
     *
     * @param newLanguage: the new language of the user
     * @param id:          the identifier of the user
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_KEY + " SET " + LANGUAGE_KEY + "=:" + LANGUAGE_KEY + " WHERE "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void changeLanguage(
            @Param(LANGUAGE_KEY) String newLanguage,
            @Param(IDENTIFIER_KEY) String id
    );

}
