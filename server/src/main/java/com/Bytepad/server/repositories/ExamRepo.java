package com.Bytepad.server.repositories;

import com.Bytepad.server.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExamRepo extends JpaRepository<Exam, UUID> {

    Exam findByExamType(String examType);

    Exam findByExamTypeId(UUID id);

    @Query("Select distinct e.examType from Exam e")
    List<String> findDistinctExams();
}
