package com.hospital.app.repositories;

import com.hospital.app.entities.account.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentistRepository extends JpaRepository<Dentist,Long> {
}
