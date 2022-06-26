package com.Bytepad.server.repositories;

import com.Bytepad.server.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin, String> {

    Admin findByUsername(String username);
}
