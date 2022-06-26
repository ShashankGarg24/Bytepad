package com.Bytepad.server.Utilities;

import com.Bytepad.server.DTOs.CourseBranchDTO;
import com.Bytepad.server.DTOs.DocumentDTO;
import com.Bytepad.server.models.Branch;
import com.Bytepad.server.models.Course;
import com.Bytepad.server.models.Document;
import com.Bytepad.server.repositories.CourseBranchRepo;
import com.Bytepad.server.repositories.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DTOconverters {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private CourseBranchRepo courseBranchRepo;


    /**
     * this method convert a document into its DTO
     * @param documents
     * @return
     */
    public List<DocumentDTO> convertToDocumentDTO(List<Document> documents){

        List<DocumentDTO> documentDTOs = new ArrayList<>();

        Comparator<Document> compareByDocumentName = Comparator.comparing(Document::getSubjectName);
        documents.sort(compareByDocumentName);


        for(Document document1: documents){
            documentDTOs.add(new DocumentDTO(document1.getDocumentId(), document1.getSubjectName(), document1.getPaperType(), document1.getSemesterType().toString(),  document1.getDocumentURI(), document1.getCourseBranch().getBranch().getBranch(), document1.getExam().getExamType().toString(), document1.getSession().getSession(), document1.getCourseBranch().getCourse().getCourse()));
        }

        return documentDTOs;
    }


    /**
     * fetches all courses and forms a DTO along with their respective branches
     * @return
     */
    public List<CourseBranchDTO> getCourseBranchDTO(){

        List<Course> distinctCourses = courseRepo.findAll();

        Comparator<Course> compareByCourseName = Comparator.comparing(Course::getCourse);
        distinctCourses.sort(compareByCourseName);

        List<CourseBranchDTO> courseBranchDTOS = new ArrayList<>();

        for(Course distinctCourse : distinctCourses){

            List<Branch> courseBranch = courseBranchRepo.findBranchesFromCourse(distinctCourse);
            List<String> branches = courseBranch.stream().map(Branch::getBranch).sorted().collect(Collectors.toList());

            courseBranchDTOS.add(new CourseBranchDTO(distinctCourse.getCourse(), branches));
        }

        return courseBranchDTOS;
    }
}
