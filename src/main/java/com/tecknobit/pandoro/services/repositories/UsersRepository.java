package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.users.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.*;

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
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY
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
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
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
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY,
            nativeQuery = true
    )
    User getUserByEmail(@Param(EMAIL_KEY) String email);

    /**
     * Method to execute the query to change the user's profile pic
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     * @param profilePic: the profile pic chosen by the user to set as the new profile pic
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_TABLE + " SET " + PROFILE_PIC_KEY + "=:" + PROFILE_PIC_KEY + " WHERE "
                    + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY + " AND " + TOKEN_KEY + "=:" + TOKEN_KEY,
            nativeQuery = true
    )
    void changeProfilePic(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token,
            @Param(PROFILE_PIC_KEY) String profilePic
    );

    /**
     * Method to execute the query to change the user's email
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     * @param email: the new user email to set
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_TABLE + " SET " + EMAIL_KEY + "=:" + EMAIL_KEY
                    + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY + " AND " + TOKEN_KEY + "=:" + TOKEN_KEY,
            nativeQuery = true
    )
    void changeEmail(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token,
            @Param(EMAIL_KEY) String email
    );

    /**
     * Method to execute the query to change the user's password
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     * @param password: the new user password to set
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_TABLE + " SET " + PASSWORD_KEY + "=:" + PASSWORD_KEY +
                    " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY + " AND " + TOKEN_KEY + "=:" + TOKEN_KEY,
            nativeQuery = true
    )
    void changePassword(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token,
            @Param(PASSWORD_KEY) String password
    );

    /**
     * Method to execute the query to delete the user's account
     *
     * @param userId: the user identifier
     * @param token: the token of the user
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + USERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + TOKEN_KEY + "=:" + TOKEN_KEY,
            nativeQuery = true
    )
    void deleteAccount(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token
    );

}
