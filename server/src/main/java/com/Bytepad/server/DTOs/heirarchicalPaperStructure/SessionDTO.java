package com.Bytepad.server.DTOs.heirarchicalPaperStructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor
@Getter
@Setter
public class SessionDTO {

    private UUID sessionId;
    private String session;
    private List<ExamDTO> exams;

    public SessionDTO(UUID sessionId, String session){
        this.sessionId = sessionId;
        this.session = session;
    }

}
