package com.mindhub.homebanking.repository;

import com.mindhub.homebanking.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
}
