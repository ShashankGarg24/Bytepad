package com.Bytepad.server.repositories;

import com.Bytepad.server.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SessionRepo extends JpaRepository<Session, UUID> {

    Session findBySession(String session);

    Session findBySessionId(UUID id);

    @Query("Select distinct s.session from Session s order by s.session desc ")
    List<String> findDistinctSessions();

}
