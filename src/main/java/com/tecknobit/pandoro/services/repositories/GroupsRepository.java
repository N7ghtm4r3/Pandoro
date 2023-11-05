package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_DESCRIPTION_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.GROUPS_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.NAME_KEY;

@Service
public interface GroupsRepository extends JpaRepository<Group, String> {

    @Query(
            value = "SELECT * FROM " + GROUPS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
            //+ " OR " + ,
            ,
            nativeQuery = true
    )
        // TODO: 05/11/2023 FETCH ALSO GROUPS WHERE ARE MEMBER
    List<Group> getGroups(@Param(AUTHOR_KEY) String userId);

    @Query(
            value = "SELECT * FROM " + GROUPS_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + NAME_KEY + "=:" + NAME_KEY,
            nativeQuery = true
    )
    Group getGroupByName(
            @Param(AUTHOR_KEY) String userId,
            @Param(NAME_KEY) String name
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + GROUPS_KEY
                    + "( "
                    + IDENTIFIER_KEY + ","
                    + NAME_KEY + ","
                    + GROUP_DESCRIPTION_KEY + ","
                    + AUTHOR_KEY + ") VALUES "
                    + "( "
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + NAME_KEY + ","
                    + ":" + GROUP_DESCRIPTION_KEY + ","
                    + ":" + AUTHOR_KEY + ")",
            nativeQuery = true
    )
    void createGroup(
            @Param(AUTHOR_KEY) String userId,
            @Param(IDENTIFIER_KEY) String groupId,
            @Param(NAME_KEY) String groupName,
            @Param(GROUP_DESCRIPTION_KEY) String groupDescription
    );

}
