package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupsRepository extends JpaRepository<Group, String> {
}
