package com.Bytepad.server.DTOs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DocumentDTO {

    private UUID documentId;
    private String subjectName;
    private String paperType;
    private String semesterType;
    private String documentURI;
    private String branch;
    private String exam;
    private String session;
    private String course;
}
