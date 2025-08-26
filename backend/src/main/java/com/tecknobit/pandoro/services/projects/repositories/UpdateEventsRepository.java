package com.tecknobit.pandoro.services.projects.repositories;

import com.tecknobit.pandoro.services.projects.entities.UpdateEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: 26/08/2025 TO DOCU 1.2.0
@Repository
public interface UpdateEventsRepository extends JpaRepository<UpdateEvent, String> {
}
