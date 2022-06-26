package com.Bytepad.server.repositories;

import com.Bytepad.server.models.AndroidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AndroidStatusRepo extends JpaRepository<AndroidStatus, String> {

    @Transactional
    @Modifying
    @Query("UPDATE AndroidStatus a SET a.androidVersion = ?1 WHERE a.id = ?2")
    void updateStatus(String androidVersion, Integer id);
}
