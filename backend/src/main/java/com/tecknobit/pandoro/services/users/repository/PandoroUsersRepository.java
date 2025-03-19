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

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.IDENTIFIER_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.USERS_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.GROUP_MEMBERS_KEY;

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
     * @param pageable  The parameters to paginate the query
     * @param membersToExclude The list of the members already joined or invited to exclude from the count
     * @return the candidates list as {@link PaginatedResponse} of {@link PandoroUser}
     */
    @Query(
            value = "SELECT * FROM " + USERS_KEY +
                    " WHERE " +
                    IDENTIFIER_KEY + " NOT IN (:" + GROUP_MEMBERS_KEY + ")",
            nativeQuery = true
    )
    List<PandoroUser> getCandidates(
            @Param(GROUP_MEMBERS_KEY) List<String> membersToExclude,
            Pageable pageable
    );

}
