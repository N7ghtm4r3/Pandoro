package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.Changelog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangelogsRepository extends JpaRepository<Changelog, String> {


}
