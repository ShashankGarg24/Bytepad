package com.Bytepad.server.DTOs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddFieldDTO {

    private String password;
    private List<String> examTypes;
    private List<String> sessions;
    private List<CourseBranchDTO> courseBranch;
}
