package com.Bytepad.server.DTOs;

import com.Bytepad.server.DTOs.heirarchicalPaperStructure.ExamDTO;
import com.Bytepad.server.DTOs.heirarchicalPaperStructure.SessionDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor
public class SessionExamFields {

    private List<SessionDTO> sessions;
    private List<ExamDTO> exams;
}
