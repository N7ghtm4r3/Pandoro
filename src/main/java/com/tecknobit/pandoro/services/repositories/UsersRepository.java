package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static com.tecknobit.pandoro.services.UsersHelper.*;

public interface UsersRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM " + USERS_TABLE + " WHERE " + EMAIL_KEY + " ='u' AND " + PASSWORD_KEY + "='u'")
    User getUserByEmailAndPassword(
            @Param(EMAIL_KEY) String email,
            @Param(PASSWORD_KEY) String password
    );

}
