package com.Bytepad.server.controllers;

import com.Bytepad.server.DTOs.CourseBranchDTO;
import com.Bytepad.server.serviceInterface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserInterface userInterface;

    @RequestMapping(value = "/user/preview/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> previewDocument(HttpServletResponse response, @PathVariable("id") String id){

        return userInterface.previewDocument(response, id);
    }

    @RequestMapping(value = "/user/download/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadDocument(HttpServletResponse response, @PathVariable("id") String id){

        return userInterface.downloadDocument(response, id);
    }


    @RequestMapping(value = "/user/getPapers", method = RequestMethod.POST)
    public ResponseEntity<?> getDocumentsByCourseAndBranch(@RequestBody CourseBranchDTO request){

        return userInterface.getDocumentsByCourseAndBranch(request.getCourse(), request.getBranches());
    }

    @RequestMapping(value = "/user/home", method = RequestMethod.GET)
    public ResponseEntity<?> getCourseAndBranch(){

        return userInterface.getCourseAndBranch();
    }


}
