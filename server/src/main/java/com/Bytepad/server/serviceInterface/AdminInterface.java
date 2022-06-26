package com.Bytepad.server.serviceInterface;

import com.Bytepad.server.DTOs.CourseBranchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public interface AdminInterface {

    ResponseEntity<?> registerAdmin(String username, String password, String email);

    ResponseEntity<?> adminLogin(String username, String password);

    ResponseEntity<?> uploadDocument(List<MultipartFile> files, String course, String branch, String session, String examType, String paperType, String semesterType);

    ResponseEntity<?> getAllDocuments();

    ResponseEntity<?> forgotPassword();

    ResponseEntity<?> resetPassword(String token, String newPassword);

    ResponseEntity<?> getRequiredFields();

    ResponseEntity<?> getSessionAndExamList();

    ResponseEntity<?> addFields(String token, String password, List<String> examTypes, List<String> sessions, List<CourseBranchDTO> courseBranch);

    ResponseEntity<?> deleteFields(String token, String id, String password);

    ResponseEntity<?> deleteDocuments(String token, String password, String courseId, String branchId, String sessionId, String examId, String paperType, String documentId);

    ResponseEntity<?> getAndroidStatus();

    ResponseEntity<?> updateAndroidStatus(String androidVersion);
}
