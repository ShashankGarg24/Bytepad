package com.Bytepad.server.controllers;

import com.Bytepad.server.DTOs.AddFieldDTO;
import com.Bytepad.server.serviceInterface.AdminInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminInterface adminInterface;

    @RequestMapping(method = RequestMethod.POST, path = "/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> loginCredentials){

        return adminInterface.adminLogin(loginCredentials.get("username"), loginCredentials.get("password"));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/forgotPassword")
    public ResponseEntity<?> forgotPassword(){

        return adminInterface.forgotPassword();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/resetPassword/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestBody Map<String, String> request){

        return adminInterface.resetPassword(token, request.get("newPassword"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/admin/upload")
    public ResponseEntity<?> getRequiredFields() {

        return adminInterface.getRequiredFields();
    }


    @RequestMapping(method = RequestMethod.POST, path = "/admin/upload")
    public ResponseEntity<?> uploadPaper(@RequestParam("files") List<MultipartFile> files, @RequestParam("course") String course, @RequestParam("branch") String branch, @RequestParam("session") String session, @RequestParam("examType") String examType, @RequestParam("paperType") String paperType, @RequestParam("semesterType") String semesterType) {

        return adminInterface.uploadDocument(files, course, branch, session, examType, paperType, semesterType);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/addFields")
    public ResponseEntity<?> addFields(@RequestHeader("Authorization") String token, @RequestBody AddFieldDTO fields) {

        return adminInterface.addFields(token.substring(7),fields.getPassword(), fields.getExamTypes(), fields.getSessions(), fields.getCourseBranch());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/deleteFields")
    public ResponseEntity<?> deleteFields(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request){
        return adminInterface.deleteFields(token.substring(7), request.get("id"), request.get("password"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/admin/deleteFields")
    public ResponseEntity<?> getSessionAndExamList(){
        return adminInterface.getSessionAndExamList();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/admin/viewDocs")
    public ResponseEntity<?> getAllDocuments(){

        return adminInterface.getAllDocuments();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/deleteDocs")
    public ResponseEntity<?> deleteDocuments(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {

        return adminInterface.deleteDocuments(token.substring(7), request.get("password"), request.get("courseId"), request.get("branchId"), request.get("sessionId"), request.get("examId"), request.get("paperType"), request.get("documentId"));

    }

    @RequestMapping(method = RequestMethod.GET, path = "/admin/getAndroidStatus")
    public ResponseEntity<?> getAndroidStatus(){
        return adminInterface.getAndroidStatus();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/updateAndroidStatus")
    public ResponseEntity<?> updateAndroidStatus(@RequestBody Map<String, String> status){
        return adminInterface.updateAndroidStatus(status.get("current_android_version"));
    }



}
