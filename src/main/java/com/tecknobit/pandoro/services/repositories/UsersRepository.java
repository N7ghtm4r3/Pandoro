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

@Service
@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    @Query(
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY
                    + " AND " + PASSWORD_KEY + "=:" + PASSWORD_KEY,
            nativeQuery = true
    )
    User getUserByEmailAndPassword(
            @Param(EMAIL_KEY) String email,
            @Param(PASSWORD_KEY) String password
    );

    @Query(
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + TOKEN_KEY + "=:" + TOKEN_KEY,
            nativeQuery = true
    )
    User getAuthorizedUser(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token
    );

    @Query(
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + EMAIL_KEY + "=:" + EMAIL_KEY,
            nativeQuery = true
    )
    User getUserByEmail(@Param(EMAIL_KEY) String email);

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
