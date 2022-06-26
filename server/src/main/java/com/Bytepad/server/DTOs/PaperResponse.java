package com.Bytepad.server.DTOs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PaperResponse {

    private List<DocumentDTO> documents = new ArrayList<>();
    private Set<String> subjects = new LinkedHashSet<>();
    private List<String> sessions = new ArrayList<>();
    private List<String> exams = new ArrayList<>();

    public PaperResponse(List<String> sessions, List<String> exams){
        this.sessions = sessions;
        this.exams = exams;
    }

}

