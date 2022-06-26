package com.Bytepad.server.DTOs.heirarchicalPaperStructure;

import com.Bytepad.server.DTOs.DocumentDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor
@Getter
@Setter
public class PaperTypeDTO {

    private String paperType;
    private List<DocumentDTO> documents;
}
