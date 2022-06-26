package com.Bytepad.server.repositories;

import com.Bytepad.server.models.CourseBranch;
import com.Bytepad.server.models.Document;
import com.Bytepad.server.models.Exam;
import com.Bytepad.server.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DocumentRepo extends JpaRepository<Document, UUID> {

    Document findByDocumentId(UUID documentId);

    List<Document> findByCourseBranch(CourseBranch courseBranch);

    /**
     * find all documents by courseBranch , session , exam and paperType
     * @param courseBranch
     * @param session
     * @param exam
     * @param paperType
     * @return
     */
    @Query("Select d from Document d where d.courseBranch = ?1 and d.session = ?2 and d.exam = ?3 and d.paperType = ?4")
    List<Document> findAllDocs(CourseBranch courseBranch, Session session, Exam exam, Document.PaperType paperType);


    /**
     * find all documents by courseBranch ID, session ID, exam ID and paperType
     * @param courseBranchId
     * @param sessionId
     * @param examId
     * @param paperType
     * @return
     */
    @Query("Select d from Document d where d.courseBranch.courseBranchId = ?1 and d.session.sessionId = ?2 and d.exam.examTypeId = ?3 and d.paperType = ?4")
    List<Document> findDocsByID(UUID courseBranchId, UUID sessionId, UUID examId, Document.PaperType paperType);

    /**
     * find all documents by courseBranch ID, session ID and exam ID
     * @param courseBranchId
     * @param sessionId
     * @param examId
     * @return
     */
    @Query("Select d from Document d where d.courseBranch.courseBranchId = ?1 and d.session.sessionId = ?2 and d.exam.examTypeId = ?3")
    List<Document> findDocsByID(UUID courseBranchId, UUID sessionId, UUID examId);

    /**
     * find all documents by courseBranch ID and session ID
     * @param courseBranchId
     * @param sessionId
     * @return
     */
    @Query("Select d from Document d where d.courseBranch.courseBranchId = ?1 and d.session.sessionId = ?2")
    List<Document> findDocsByID(UUID courseBranchId, UUID sessionId);

    /**
     * find all documents by courseBranch ID
     * @param courseBranchId
     * @return
     */
    @Query("Select d from Document d where d.courseBranch.courseBranchId = ?1")
    List<Document> findDocsByID(UUID courseBranchId);

    List<Document> findAllBySession(Session session);

    List<Document> findAllByExam(Exam exam);

}
