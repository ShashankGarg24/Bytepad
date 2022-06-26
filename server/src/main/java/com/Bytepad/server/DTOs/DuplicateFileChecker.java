package com.Bytepad.server.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DuplicateFileChecker {

    private String filePath;
    private boolean isPresent;
}
