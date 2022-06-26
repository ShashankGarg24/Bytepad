package com.Bytepad.server.serviceImplementation;

import com.Bytepad.server.DTOs.CourseBranchDTO;
import com.Bytepad.server.DTOs.PaperResponse;
import com.Bytepad.server.Utilities.DTOconverters;
import com.Bytepad.server.models.*;
import com.Bytepad.server.repositories.*;
import com.Bytepad.server.serviceInterface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class UserService implements UserInterface {

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private ExamRepo examRepo;

    @Autowired
    private CourseBranchRepo courseBranchRepo;

    @Autowired
    private DTOconverters dto_converters;


    /**
     * method to preview pdf in browser and downloads word documents
     * @param response
     * @param id
     * @return
     */
    public ResponseEntity<?> previewDocument(HttpServletResponse response, String id){
        try{
            Document document = documentRepo.findByDocumentId(UUID.fromString(id));

            if(document == null){
                return new ResponseEntity<>("No such Document found!", HttpStatus.EXPECTATION_FAILED);
            }

            File file = new File(document.getDocumentURI());
            InputStream inputStream = new FileInputStream(file);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            if(file.getName().substring(file.getName().lastIndexOf('.') + 1).equals("pdf")){
                response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            }

            response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
            response.setHeader("Content-Length", String.valueOf(file.length()));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

            return null;
        }
        catch (Exception e){
            return new ResponseEntity<>("Downloading failed", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * downloads both pdf and word documents
     * @param response
     * @param id
     * @return
     */
    public ResponseEntity<?> downloadDocument(HttpServletResponse response, String id){
        try{
            Document document = documentRepo.findByDocumentId(UUID.fromString(id));

            if(document == null){
                return new ResponseEntity<>("No such Document  found!", HttpStatus.EXPECTATION_FAILED);
            }

            File file = new File(document.getDocumentURI());
            InputStream inputStream = new FileInputStream(file);

            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setHeader("Content-Length", String.valueOf(file.length()));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

            return null;
        }
        catch (Exception e){
            return new ResponseEntity<>("Downloading failed", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * get a list of available course and their branches
     * @return
     */
    public  ResponseEntity<?> getCourseAndBranch(){
        try{

            List<CourseBranchDTO> courseBranchDTOS = new ArrayList<>();
            List<Course> availableCourses = courseRepo.findAll();

            Comparator<Course> compareByCourseName = Comparator.comparing(Course::getCourse);
            availableCourses.sort(compareByCourseName);

            for(Course course : availableCourses){

                List<String> courseBranches = new ArrayList<>();
                List<Branch> branches = courseBranchRepo.findBranchesFromCourse(course);

                Comparator<Branch> compareByBranchName = Comparator.comparing(Branch::getBranch);
                branches.sort(compareByBranchName);

                for(Branch branch : branches){

                    courseBranches.add(branch.getBranch());
                }


                courseBranchDTOS.add(new CourseBranchDTO(course.getCourse(), courseBranches));

            }

            return new ResponseEntity<>(courseBranchDTOS, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * get documents related to the particular course and its branches
     * @param course
     * @param branches
     * @return
     */
    public ResponseEntity<?> getDocumentsByCourseAndBranch(String course, List<String> branches){
        try{

            Course course1 = courseRepo.findByCourse(course);
            List<Document> documents = new ArrayList<>();

            for (String branch : branches){
                Branch branch1 = branchRepo.findByBranch(branch);

                CourseBranch courseBranch = courseBranchRepo.findByCourseAndBranch(course1, branch1);

                if(courseBranch != null){
                    documents.addAll(documentRepo.findByCourseBranch(courseBranch));
                }
            }

            List<String> exams = examRepo.findDistinctExams();
            Collections.sort(exams);


            if(documents.isEmpty()){

                PaperResponse paperResponse = new PaperResponse(sessionRepo.findDistinctSessions(), exams);
                return new ResponseEntity<>(paperResponse, HttpStatus.OK);
            }

            Comparator<Document> compareBySubjectName = Comparator.comparing(Document::getSubjectName);
            documents.sort(compareBySubjectName);

            Set<String> distinctSubjects = new LinkedHashSet<>();


            for(Document document : documents){
                distinctSubjects.add(document.getSubjectName());
            }


            PaperResponse paperResponse = new PaperResponse(dto_converters.convertToDocumentDTO(documents), distinctSubjects, sessionRepo.findDistinctSessions(), exams);

            return new ResponseEntity<>(paperResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
