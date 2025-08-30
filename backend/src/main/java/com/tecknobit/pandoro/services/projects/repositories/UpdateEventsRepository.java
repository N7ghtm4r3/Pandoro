package com.tecknobit.pandoro.services.projects.repositories;

import com.tecknobit.pandoro.services.projects.entities.UpdateEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@code UpdatesRepository} interface is useful to handle the queries for the updates events
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see UpdateEvent
 * @since 1.2.0
 */
@Repository
public interface UpdateEventsRepository extends JpaRepository<UpdateEvent, String> {
}
