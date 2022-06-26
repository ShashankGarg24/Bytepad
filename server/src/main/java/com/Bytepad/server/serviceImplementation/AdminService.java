package com.Bytepad.server.serviceImplementation;

import com.Bytepad.server.DTOs.*;
import com.Bytepad.server.DTOs.heirarchicalPaperStructure.*;
import com.Bytepad.server.Utilities.*;
import com.Bytepad.server.configuration.JWTUtils;
import com.Bytepad.server.exceptions.EmptyField;
import com.Bytepad.server.exceptions.UsernameAlreadyExist;
import com.Bytepad.server.models.*;
import com.Bytepad.server.repositories.*;
import com.Bytepad.server.serviceInterface.AdminInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AdminService implements AdminInterface {

    //variable used to check if deletion of empty folder got completed or not
    private boolean deletionFinished;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("adminDetails")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private CourseBranchRepo courseBranchRepo;

    @Autowired
    private ExamRepo examRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private AndroidStatusRepo androidStatusRepo;

    @Autowired
    private FileUploadingService fileUploadingService;

    @Autowired
    private FileDeletingService fileDeletingService;

    @Autowired
    private DTOconverters dto_converters;

    @Autowired
    private Validators validators;

    @Autowired
    private MailUtilities mailUtilities;


    /**
     * creates an admin account
     * @param username
     * @param password
     * @param email
     * @return
     */
    public ResponseEntity<?> registerAdmin(String username, String password, String email){

        try{
            if(username.isEmpty()){
                throw new EmptyField();
            }

            if(adminRepo.findByUsername(username) != null){
                throw new UsernameAlreadyExist();
            }

            if(password.isEmpty()){
                throw new EmptyField();
            }

            if(email.isEmpty()){
                throw new EmptyField();
            }

            validators.emailValidator(email);
            validators.passwordValidator(password);

            Admin admin = new Admin(username, new BCryptPasswordEncoder().encode(password), email);
            adminRepo.save(admin);

            return new ResponseEntity<>("Admin account created", HttpStatus.ACCEPTED);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    /**
     * method for authentication of admin(login feature)
     * @param username
     * @param password
     * @return
     */
    public ResponseEntity<?> adminLogin(String username, String password) {

        try {
            Admin admin = adminRepo.findByUsername(username);

            if (admin == null) {
                return new ResponseEntity<>("Username is incorrect!", HttpStatus.NOT_FOUND);
            }

            if (!validators.verifyPassword(admin, password)) {
                return new ResponseEntity<>("Password is incorrect!", HttpStatus.NOT_ACCEPTABLE);
            }

            String accessToken = createLoginToken(username, password);

            return new ResponseEntity<>(new LoginResponse(accessToken), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * creates JWT token after user authentication
     * @param username
     * @param password
     * @return
     * @throws AuthenticationException
     */
    public String createLoginToken(String username, String password) throws AuthenticationException{

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String accessToken = jwtUtils.generateToken(userDetails);

        return accessToken;
    }

    /**
     * sends a mail to the admin registered email for reset password link
     * @return
     */
    public ResponseEntity<?> forgotPassword(){
        try{
            Admin admin = adminRepo.findAll().get(0);

            if(admin == null){
                return new ResponseEntity<>("No admin available!", HttpStatus.NOT_FOUND);
            }

            String verificationToken = jwtUtils.createVerificationToken(admin.getUsername());
            String verificationUrl = "https://bytepad.silive.in/#/api/Bytepad-siadmin/resetPassword/" + verificationToken;

            String mailContent ="<p>Dear admin, </p>";
            mailContent += "<p>Please click the link below to change password</p>";
            mailContent += "<a href=\"" + verificationUrl + "\">RESET PASSWORD</a><br>";

            String subject = "Reset Password";

            mailUtilities.sendMail(admin.getEmail(), mailContent, subject);

            return new ResponseEntity<>("Mail sent successfully!", HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }


    /**
     * takes token from the route and verify the identity of admin and then change its password
     * @param token
     * @param newPassword
     * @return
     */
    public ResponseEntity<?> resetPassword(String token, String newPassword){

        try{

            if(jwtUtils.isTokenExpired(token)){
                return new ResponseEntity<>("Link expired", HttpStatus.EXPECTATION_FAILED);
            }

            String username = jwtUtils.extractUsername(token);
            Admin admin = adminRepo.findByUsername(username);

            if(admin == null){
                return new ResponseEntity<>("Invalid access", HttpStatus.FORBIDDEN);
            }

            validators.passwordValidator(newPassword);

            admin.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            adminRepo.save(admin);


            String mailcontent = "Password successfully changed";
            String subject = "Password Changed";

            mailUtilities.sendMail(admin.getEmail(), mailcontent, subject);

            return new ResponseEntity<>("Password changed", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * method to create fields required for document upload
     * @param token
     * @param password
     * @param examTypes
     * @param sessions
     * @param courseBranches
     * @return
     */
    public ResponseEntity<?> addFields(String token, String password, List<String> examTypes, List<String> sessions, List<CourseBranchDTO> courseBranches){
        try{

            Admin admin = adminRepo.findByUsername(jwtUtils.extractUsername(token));

            if (!validators.verifyPassword(admin, password)){
                return new ResponseEntity<>("Incorrect password!", HttpStatus.NOT_ACCEPTABLE);
            }

            for(Object session:  checkNull(sessions)){

                String sessionValue = session.toString();

                if(sessionValue.isEmpty()){
                    continue;
                }

                Session session1 = sessionRepo.findBySession(sessionValue);

                if(session1 == null){
                    Session session2 = new Session(session.toString());
                    sessionRepo.save(session2);
                }

            }

            for(Object examType: checkNull(examTypes)){

                String examValue = examType.toString();

                if(examValue.isEmpty()){
                    continue;
                }

                Exam exam = examRepo.findByExamType(examValue);

                if(exam == null){
                    Exam exam1 = new Exam(examType.toString());
                    examRepo.save(exam1);
                }

            }

            for(Object courseBranch: checkNull(courseBranches)){

                CourseBranchDTO courseBranchDTO = (CourseBranchDTO)courseBranch;

                String courseValue = ((CourseBranchDTO) courseBranch).getCourse();

                if(courseValue.isEmpty()){
                    continue;
                }

                Course course = courseRepo.findByCourse(courseValue);

                if(course == null){
                    Course newCourse = new Course(courseBranchDTO.getCourse());
                    courseRepo.save(newCourse);
                    course = newCourse;
                }


                for(String branch: courseBranchDTO.getBranches()){

                    if(branch.isEmpty()){
                        continue;
                    }

                    Branch branch1 = branchRepo.findByBranch(branch);

                    if(branch1 == null){
                        Branch newBranch = new Branch(branch);
                        branchRepo.save(newBranch);
                        branch1 = newBranch;
                    }

                    CourseBranch courseBranch1 = courseBranchRepo.findByCourseAndBranch(course, branch1);

                    if(courseBranch1 == null){
                        CourseBranch newCourseBranch = new CourseBranch(course, branch1);
                        courseBranchRepo.save(newCourseBranch);
                    }

                }

            }

            return new ResponseEntity<>("Fields created.", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * deletes sessions and exam related fields
     * @param token
     * @param id
     * @param password
     * @return
     */
    public ResponseEntity<?> deleteFields(String token, String id, String password){
        try{

            Admin admin = adminRepo.findByUsername(jwtUtils.extractUsername(token));

            if (!validators.verifyPassword(admin, password)){
                return new ResponseEntity<>("Incorrect password!", HttpStatus.NOT_ACCEPTABLE);
            }

            Session session = sessionRepo.findBySessionId(UUID.fromString(id));
            Exam exam = examRepo.findByExamTypeId(UUID.fromString(id));

            if(session == null && exam == null){
                return new ResponseEntity<>("no field available!", HttpStatus.NOT_FOUND);
            }

            if(session != null){

                deleteDocumentList(documentRepo.findAllBySession(session));
                sessionRepo.delete(session);

                return new ResponseEntity<>("session deleted.", HttpStatus.OK);
            }
            else {

                deleteDocumentList(documentRepo.findAllByExam(exam));
                examRepo.delete(exam);

                return new ResponseEntity<>("exam deleted.", HttpStatus.OK);
            }

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * method to check if a list is null, if it is then it returns an empty list
     * @param list
     * @return
     */
    private static List checkNull(List list){
        return list == null ? Collections.EMPTY_LIST : list;
    }


    /**
     * generated pre existing exams, sessions, course and their branches
     * @return
     */
    public ResponseEntity<?> getRequiredFields(){
        try {
            FieldsDTO uploadRequest = new FieldsDTO(examRepo.findDistinctExams(), sessionRepo.findDistinctSessions(), dto_converters.getCourseBranchDTO());

            return new ResponseEntity<>(uploadRequest, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * returns a list of existing sessions and exams
     * @return
     */
    public ResponseEntity<?> getSessionAndExamList(){
        try{
            List<SessionDTO> sessionDTOS = new ArrayList<>();
            List<ExamDTO> examDTOS = new ArrayList<>();

            for(Session session: sessionRepo.findAll()){
                sessionDTOS.add(new SessionDTO(session.getSessionId(), session.getSession()));
            }

            for(Exam exam: examRepo.findAll()){
                examDTOS.add(new ExamDTO(exam.getExamTypeId(), exam.getExamType()));
            }

            SessionExamFields sessionExamFields = new SessionExamFields(sessionDTOS, examDTOS);

            return new ResponseEntity<>(sessionExamFields, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * method to upload documents
     * checks if the directory for that particular paper set exists or not
     * if not then it creates the directory first and then stores the papers in it
     * it also retuns a list of name of files which have already been uploaded on the server.
     * @param files
     * @param course
     * @param branch
     * @param session
     * @param examType
     * @param paperType
     * @param semesterType
     * @return
     */
    public ResponseEntity<?> uploadDocument(List<MultipartFile> files, String course, String branch, String session, String examType, String paperType, String semesterType){

        try{

            Session session1 = sessionRepo.findBySession(session);
            Exam exam = examRepo.findByExamType(examType);

            Course courseForDoc = courseRepo.findByCourse(course);
            Branch branchForDoc = branchRepo.findByBranch(branch);

            if(exam == null || session1 == null || courseForDoc == null || branchForDoc == null || paperType.isEmpty() || semesterType.isEmpty()){
                return new ResponseEntity<>("Fields cannot be null/empty", HttpStatus.NOT_ACCEPTABLE);
            }

            CourseBranch courseBranch = courseBranchRepo.findByCourseAndBranch(courseForDoc, branchForDoc);

            if(courseBranch == null){
                return new ResponseEntity<>("invalid course branch", HttpStatus.EXPECTATION_FAILED);
            }

            String paperDirectory = ".\\src\\main\\resources\\papers\\" + course + "\\" + branch + "\\" + session + "\\" + semesterType + "\\" + examType + "\\" + paperType + "\\";
            File f = new File(paperDirectory);

            /*this condition checks if the above directory exists or not
            if not then creates it.
             */
            if(!f.exists()){
                Path path = Paths.get(paperDirectory);
                Files.createDirectories(path);
            }

            ArrayList<String> filesNotUploaded = new ArrayList<>();

            for(MultipartFile file : files){
                int indexBeforePeriod = file.getOriginalFilename().lastIndexOf('.');
                String subjectName = file.getOriginalFilename().substring(0, indexBeforePeriod);
                DuplicateFileChecker duplicateFileChecker = fileUploadingService.fileUpload(file, paperDirectory);

                if(duplicateFileChecker.isPresent()){
                    int lastIndexOfSlash = duplicateFileChecker.getFilePath().lastIndexOf('\\');
                    filesNotUploaded.add(duplicateFileChecker.getFilePath().substring(lastIndexOfSlash + 1));
                    continue;
                }

                Document document = new Document(subjectName.toUpperCase(), Document.PaperType.valueOf(paperType),
                        Document.SemesterType.valueOf(semesterType), duplicateFileChecker.getFilePath());

                document.setCourseBranch(courseBranch);
                document.setSession(session1);
                document.setExam(exam);
                documentRepo.save(document);

            }

            sessionRepo.save(session1);
            examRepo.save(exam);

            return new ResponseEntity<>(new UploadResponse(filesNotUploaded), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * this method is used to retrieve all documents for admin panel, in hierarchical structure
     * it returns only those fields which will have documents in them
     * except courses and branches, All courses and branches will be returned
     * @return
     */
    public ResponseEntity<?> getAllDocuments(){
        try{

            List<Course> courses = courseRepo.findAll();

            Comparator<Course> compareByCourseName = Comparator.comparing(Course::getCourse);
            courses.sort(compareByCourseName);

            List<CourseDTO> courseDTOS = new ArrayList<>();

            for(Course course: courses){
                List<Branch> branches = courseBranchRepo.findBranchesFromCourse(course);

                Comparator<Branch> compareByBranchName = Comparator.comparing(Branch::getBranch);
                branches.sort(compareByBranchName);

                List<BranchDTO> branchDTOs = new ArrayList<>();

                for(Branch branch : branches){
                    List<Session> sessions = sessionRepo.findAll();

                    Comparator<Session> compareBySessionName = Comparator.comparing(Session::getSession).reversed();
                    sessions.sort(compareBySessionName);

                    List<SessionDTO> sessionDTOs = new ArrayList<>();

                    for(Session session : sessions){

                        List<Exam> exams = examRepo.findAll();
                        Comparator<Exam> compareByExamName = Comparator.comparing(Exam::getExamType);
                        exams.sort(compareByExamName);

                        List<ExamDTO> examDTOs = new ArrayList<>();

                        for(Exam exam:exams){

                            Document.PaperType[] paperTypes = Document.PaperType.values();
                            List<PaperTypeDTO> paperTypeDTOs = new ArrayList<>();

                            for(Document.PaperType paperType : paperTypes){

                                List<Document> documents = documentRepo.findAllDocs(courseBranchRepo.findByCourseAndBranch(course, branch), session, exam, paperType);

                                if(documents.size() == 0){
                                    continue;
                                }

                                PaperTypeDTO paperTypeDTO = new PaperTypeDTO(paperType.toString(), dto_converters.convertToDocumentDTO(documents));
                                paperTypeDTOs.add(paperTypeDTO);
                            }

                            if(paperTypeDTOs.size() == 0){
                                continue;
                            }

                            ExamDTO examDTO = new ExamDTO(exam.getExamTypeId(), exam.getExamType(), paperTypeDTOs);
                            examDTOs.add(examDTO);

                        }

                        if(examDTOs.size() == 0){
                            continue;
                        }

                        SessionDTO sessionDTO = new SessionDTO(session.getSessionId(), session.getSession(), examDTOs);
                        sessionDTOs.add(sessionDTO);
                    }


                    BranchDTO branchDTO = new BranchDTO(branch.getBranchId(), branch.getBranch(), sessionDTOs);
                    branchDTOs.add(branchDTO);
                }

                CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourse(), branchDTOs);
                courseDTOS.add(courseDTO);
            }

            return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * deletes documents from sessions, exams, paper types and even a single document
     * deletes course or their branches completely along with their documents
     * @param token
     * @param password
     * @param courseId
     * @param branchId
     * @param sessionId
     * @param examId
     * @param paperType
     * @param documentId
     * @return
     */
    public ResponseEntity<?> deleteDocuments(String token, String password, String courseId, String branchId, String sessionId, String examId, String paperType, String documentId){

        try{
            UUID courseBranchId = null;

            Admin admin = adminRepo.findByUsername(jwtUtils.extractUsername(token));


            if (!courseId.isEmpty() && !branchId.isEmpty()) {

                courseBranchId = courseBranchRepo.findByCourse_CourseIdAndBranch_BranchId(UUID.fromString(courseId), UUID.fromString(branchId)).getCourseBranchId();
            }

            if (!documentId.isEmpty()) {

                Document document = documentRepo.findByDocumentId(UUID.fromString(documentId));
                documentRepo.deleteById(document.getDocumentId());
                fileDeletingService.deleteDocument(document.getDocumentURI());

                return new ResponseEntity<>("document deleted.", HttpStatus.OK);

            }

            if (!validators.verifyPassword(admin, password)){
                return new ResponseEntity<>("Incorrect password!", HttpStatus.NOT_ACCEPTABLE);
            }

            if (!paperType.isEmpty()) {

                List<Document> documents = documentRepo.findDocsByID(courseBranchId, UUID.fromString(sessionId), UUID.fromString(examId), Document.PaperType.valueOf(paperType));
                deleteDocumentList(documents);

                return new ResponseEntity<>("paperType documents deleted.", HttpStatus.OK);

            } else if (!examId.isEmpty()) {

                List<Document> documents = documentRepo.findDocsByID(courseBranchId, UUID.fromString(sessionId), UUID.fromString(examId));
                deleteDocumentList(documents);

                return new ResponseEntity<>("Exam documents deleted.", HttpStatus.OK);

            } else if (!sessionId.isEmpty()) {

                List<Document> documents = documentRepo.findDocsByID(courseBranchId, UUID.fromString(sessionId));
                deleteDocumentList(documents);

                return new ResponseEntity<>("session documents deleted.", HttpStatus.OK);

            } else if (!branchId.isEmpty()) {

                if (courseBranchId == null){
                    return new ResponseEntity<>("no such branch available!", HttpStatus.NOT_FOUND);
                }

                deleteDocumentList( documentRepo.findDocsByID(courseBranchId) );
                courseBranchRepo.deleteById(courseBranchId);

                UUID branchUUID = UUID.fromString(branchId);
                if(courseBranchRepo.findAllByBranch_BranchId(branchUUID).isEmpty()){
                    branchRepo.deleteById(branchUUID);
                }

                return new ResponseEntity<>("branch deleted for the course", HttpStatus.OK);

            } else if (!courseId.isEmpty()) {

                UUID courseUUID = UUID.fromString(courseId);

                for (CourseBranch courseBranch : courseBranchRepo.findAllByCourse_CourseId(courseUUID)) {

                    deleteDocumentList(documentRepo.findByCourseBranch(courseBranch));

                    UUID branchUUID = courseBranch.getBranch().getBranchId();
                    courseBranchRepo.delete(courseBranch);

                    if(courseBranchRepo.findAllByBranch_BranchId(branchUUID).isEmpty()){
                        branchRepo.deleteById(branchUUID);
                    }
                }

                courseRepo.deleteById(courseUUID);

                return new ResponseEntity<>("course deleted.", HttpStatus.OK);
            }


            return new ResponseEntity<>("Nothing selected", HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * sends android version data to frontent
     * @return
     */
    public ResponseEntity<?> getAndroidStatus(){
        try{

            if(androidStatusRepo.findAll().isEmpty()){
                return new ResponseEntity<>("no status", HttpStatus.OK);
            }

            AndroidStatus currentAndroidStatus = androidStatusRepo.findAll().get(0);

            return new ResponseEntity<>(currentAndroidStatus, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * receives android version data from frontend
     * @param androidVersion
     * @return
     */
    public ResponseEntity<?> updateAndroidStatus(String androidVersion){
        try{

            if(androidStatusRepo.findAll().isEmpty()){

                AndroidStatus newAndroidStatus = new AndroidStatus(androidVersion);
                androidStatusRepo.save(newAndroidStatus);
            }else {

                androidStatusRepo.updateStatus(androidVersion, 1);

            }

            return new ResponseEntity<>("Status saved", HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * deletes list of documents and calls function to delete empty directory
     * @param documents
     * @throws IOException
     */
    private void deleteDocumentList(List<Document> documents) throws IOException {

        for(Document document: documents){

            fileDeletingService.deleteDocument(document.getDocumentURI());
            documentRepo.deleteById(document.getDocumentId());
        }

        deleteEmptyDirectories();
    }


    /**
     * the below two functions deletes empty folders from the directory recursively
     */
    private void deleteEmptyDirectories(){

        String paperFolder = ".\\src\\main\\resources\\papers";
        do{
            deletionFinished = true;
            visitDirectory(paperFolder);
        }while (!deletionFinished);

    }

    /**
     * utility function for deleteEmptyDirectories function
     * @param directoryPath
     */
    private void visitDirectory(String directoryPath){

        File folder = new File(directoryPath);
        File[] files = folder.listFiles();

        //returns the control back to the calling function if specified folder doesn't exists
        if(files == null){
            return;
        }

        if(files.length == 0){
            System.out.println(directoryPath + " deleted!");
            folder.delete();
            deletionFinished = false;
        }else {
            for(int i=0; i<files.length; i++){

                if(files[i].isDirectory()){
                    visitDirectory(files[i].getAbsolutePath());
                }
            }
        }
    }
}
