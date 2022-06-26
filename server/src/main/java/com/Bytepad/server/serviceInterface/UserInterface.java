package com.Bytepad.server.serviceInterface;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface UserInterface {

    ResponseEntity<?> previewDocument(HttpServletResponse response, String id);

    ResponseEntity<?> downloadDocument(HttpServletResponse response, String id);

    ResponseEntity<?> getDocumentsByCourseAndBranch(String course, List<String> branches);

    ResponseEntity<?> getCourseAndBranch();

}
