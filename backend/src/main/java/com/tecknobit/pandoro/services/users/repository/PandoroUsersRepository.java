package com.tecknobit.pandoro.services.users.repository;

import com.tecknobit.equinoxbackend.environment.services.users.repository.EquinoxUsersRepository;
import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;

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
     * Method to get the candidates user list
     *
     * @param userId The user identifier
     * @param pageable  The parameters to paginate the query
     * @return the candidates list as {@link PaginatedResponse} of {@link PandoroUser}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY + " WHERE " + IDENTIFIER_KEY + "!=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    List<PandoroUser> getCandidates(
            @Param(IDENTIFIER_KEY) String userId,
            Pageable pageable
    );

}
