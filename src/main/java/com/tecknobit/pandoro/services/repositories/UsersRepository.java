package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.users.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.*;

@Service
public interface UsersRepository extends JpaRepository<User, String> {

    @Query(
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + EMAIL_KEY + "=:email AND " + PASSWORD_KEY + "=:password",
            nativeQuery = true
    )
    User getUserByEmailAndPassword(
            @Param(EMAIL_KEY) String email,
            @Param(PASSWORD_KEY) String password
    );

    @Query(
            value = "SELECT * FROM " + USERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:id AND " + TOKEN_KEY + "=:token",
            nativeQuery = true
    )
    User getAuthorizedUser(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token
    );

    // TODO: 01/11/2023 PASS CORRECT PROFILE PIC
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + USERS_TABLE + " SET " + PROFILE_PIC_KEY + "=:profile_pic WHERE " + IDENTIFIER_KEY
                    + "=:id AND " + TOKEN_KEY + "=:token",
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
            value = "UPDATE " + USERS_TABLE + " SET " + EMAIL_KEY + "=:email WHERE " + IDENTIFIER_KEY + "=:id AND "
                    + TOKEN_KEY + "=:token",
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
            value = "UPDATE " + USERS_TABLE + " SET " + PASSWORD_KEY + "=:password WHERE " + IDENTIFIER_KEY + "=:id AND "
                    + TOKEN_KEY + "=:token",
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
            value = "DELETE FROM " + USERS_TABLE + " WHERE " + IDENTIFIER_KEY + "=:id AND " + TOKEN_KEY + "=:token",
            nativeQuery = true
    )
    void deleteAccount(
            @Param(IDENTIFIER_KEY) String userId,
            @Param(TOKEN_KEY) String token
    );

}
