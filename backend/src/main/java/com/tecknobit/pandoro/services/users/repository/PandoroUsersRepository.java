package com.tecknobit.pandoro.services.users.repository;

import com.tecknobit.equinoxbackend.environment.services.users.repository.EquinoxUsersRepository;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;

/**
 * The {@code UsersRepository} interface is useful to manage the queries for the users
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see EquinoxUsersRepository
 * @see PandoroUser
 */
@Primary
@Repository
public interface PandoroUsersRepository extends EquinoxUsersRepository<PandoroUser> {

    /**
     * Method to execute the query to select a {@link PandoroUser} by its email and its password
     *
     * @param email:    the user email
     * @param password: the user password
     * @return the user, if exists, as {@link PandoroUser}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY
                    + " AND " + PASSWORD_KEY + "=:" + PASSWORD_KEY,
            nativeQuery = true
    )
    PandoroUser getUserByEmailAndPassword(
            @Param(EMAIL_KEY) String email,
            @Param(PASSWORD_KEY) String password
    );

    /**
     * Method to execute the query to select a {@link PandoroUser} by its identifier and its token
     *
     * @param userId: the user identifier
     * @param token: the user token
     * @return the user, if exists, as {@link PandoroUser}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + TOKEN_KEY + "=:" + TOKEN_KEY,
            nativeQuery = true
    )
    PandoroUser getAuthorizedUser(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token
    );

    /**
     * Method to execute the query to select a {@link PandoroUser} by its email
     *
     * @param email The user email
     * @return the user, if exists, as {@link PandoroUser}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE "
                    + EMAIL_KEY + "=:" + EMAIL_KEY
                    + " AND dtype='PandoroUser'",
            nativeQuery = true
    )
    PandoroUser getUserByEmail(
            @Param(EMAIL_KEY) String email
    );

}
