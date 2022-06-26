package com.Bytepad.server.DTOs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UploadResponse {

    private ArrayList<String> remainingFiles;
}

