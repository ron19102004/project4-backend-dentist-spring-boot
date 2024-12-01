package com.hospital.core.repositories;

import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DentistRepository extends JpaRepository<Dentist,Long> {
    @Query("SELECT d FROM Dentist d WHERE d.user.role = :role")
    List<Dentist> findAllWithRoleRole(@Param("role")Role role);
    @Query("SELECT d FROM Dentist d WHERE d.user.role = :role AND d.id = :id")
    Dentist findByIdWithRole(@Param("id") Long id,
                             @Param("role")Role role);
}
